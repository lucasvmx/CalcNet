/*
    Connections.cs

    Possui atributos e métodos relacionados com as operações de rede.

    Autor: Lucas Vieira de Jesus
*/

using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Windows.Forms;
using System.IO;
using Newtonsoft.Json;
using System.Diagnostics;
using System.Net.NetworkInformation;
using System.Text;

namespace CalcNetServer
{
    class Connections
    {
        public static int MAX_CONNECTIONS = 250;

        string audios_dir = "";
        private frmMain fm = null;
        public static volatile int users = 0;
        private string blacklist_file = "lista_negra.dat";
        StreamWriter sw;

        public Connections()
        {
            audios_dir = Environment.CurrentDirectory + "\\audios";
            fm = frmMain.fMain;
            users = 0;

            /* Isso impede que os usuários fiquem permanentemente bloqueados */
            if (File.Exists(blacklist_file))
                File.Delete(blacklist_file);

            sw = File.CreateText(blacklist_file);
            sw.WriteLine($"# Usuários temporariamente bloqueados do CalcNet");
            sw.Close();
        }

        public void EscutarConexoes()
        {
            TcpClient usuario = null;
            TcpListener tcpListener = new TcpListener(IPAddress.Parse(frmMain.ip), frmMain.porta);

            try
            {
                tcpListener.Start();
                frmMain.bServerIsRunning = true;
            } catch(SocketException)
            {
                frmMain.bServerIsRunning = false;
                throw;
            }

            fm.WriteLog($"Aguardando usuários se conectarem ...\n");
            Debug.WriteLine($"Aguardando conexões no IP {frmMain.ip} => Porta {frmMain.porta}");
            
            while (true)
            {
                if (frmMain.bStopServer)
                {
                    try
                    {
                        tcpListener.Server.Close();
                        tcpListener.Stop();
                        fm.WriteLog("A escuta de conexões foi terminada\n");
                    } catch(Exception e)
                    {
                        fm.WriteLog($"{e.Message}");
                    }
                    
                    break;
                }

                if (tcpListener.Pending())
                {
                    try
                    {
                        if (users == 250)
                        {
                            MessageBox.Show("A quantidade máxima de usuários simultâneos já foi atingida");
                        }
                        else
                        {
                            usuario = tcpListener.AcceptTcpClient();

                            Debug.WriteLine($"Conexão aceita: {usuario.Client.RemoteEndPoint.ToString()}");

                            /* Para cada cliente conectado, é necessário fazer um processamento paralelo */

                            Thread sThread = new Thread(unused =>
                            {
                                GerenciarConexao(usuario, users);
                            })
                            {
                                IsBackground = true
                            };

                            /* Aqui inicia-se o processamento paralelo */
                            sThread.Start();
                            users++;
                        }
                    }
                    catch (SocketException)
                    {
                        throw;
                    }
                    catch(OutOfMemoryException)
                    {
                        throw;
                    }
                    catch(ThreadAbortException tae)
                    {
                        if(usuario != null)
                            fm.WriteLog($"A thread de conexao do usuário {usuario.Client.RemoteEndPoint.ToString()} foi abortada <==\n");

                        Logger stack = new Logger(true);
                        stack.WriteStackTrace(tae);
                    }
                }
            }

            frmMain.bServerIsRunning = false;
            frmMain.bStopServer = false;
        }

        private void GerenciarConexao(object usuario, int id)
        {
            TcpClient user = usuario as TcpClient;
            NetworkStream userStream;
            byte[] memoria = new byte[8192];
            int bytes_lidos = 0;
            string json_request = "";
            User user_data = null;
            bool username_showed = false;
            bool usuario_errou = false;
            bool blacklisted = false;
            string clean_ip = "";
            string[] blacklisted_user_data = { "" };

            user.Client.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.KeepAlive, true);
            Thread.Sleep(1000);

            clean_ip = getIpFromRemoteEndPointString(user.Client.RemoteEndPoint.ToString());
            fm.WriteLog($"Usuário conectado: {clean_ip}, id {users}\n");
            frmMain.log.Write($"Usuário conectado: {clean_ip}\n");

            if(File.Exists(blacklist_file))
            {
                /* Verificar se o ip em questão não está bloqueado */
                string[] lines = File.ReadAllLines(blacklist_file);
                foreach(string line in lines)
                {
                    if (line.StartsWith("#"))
                        continue;

                    blacklisted_user_data = line.Split(',');
                    if (blacklisted_user_data[0] == clean_ip)
                    {                        
                        blacklisted = true;
                        break;
                    }
                }
            }

            if(blacklisted)
            {
                Debug.WriteLine($"Usuário na lista negra: {clean_ip}");
                user.Client.Close();
                user.Client.Dispose();
                users--;
                return;
            }

            userStream = user.GetStream();
            userStream.ReadTimeout = 100;   /* 100 milissegundos */
            userStream.WriteTimeout = 100;  /* 100 milissegundos */

            if(!userStream.CanRead)
            {
                Debug.WriteLine($"Canal não permite leitura");
                MessageBox.Show("Não é possível ler dados por este canal de rede", "Erro crítico", MessageBoxButtons.OK, MessageBoxIcon.Error);
                Thread.CurrentThread.Abort();
            }

            while (user.Connected && !blacklisted)
            {
                if (frmMain.bStopServer || usuario_errou)
                {
                    Debug.WriteLine($"Desconectando usuário {clean_ip}");
                    fm.WriteLog($"Desconectando usuário {clean_ip}\n");
                    if (usuario_errou)
                    {
                        /* Enviar a ele uma mensagem avisando que ele foi detectado */
                        byte[] payload;
                        string json_payload = "{\"connection_blocked\":true}";
                        payload = Encoding.UTF8.GetBytes(json_payload);

                        userStream.Write(payload, 0, payload.Length);
                        frmMain.log.Write($"O usuário {user_data.nome} estava utilizando a calculadora incorretamente <==\n");
                        Thread.Sleep(10000);
                    }

                    user.Client.Close();
                    break;
                }

                if(userStream.DataAvailable)
                {
                    Debug.WriteLine($"Data avaiable: {clean_ip}");
                    try
                    {
                        bytes_lidos = userStream.Read(memoria, 0, memoria.Length);

                        json_request = "";
                        json_request = Encoding.UTF8.GetString(memoria,0,bytes_lidos);
                        int len = json_request.IndexOf("}");
                        json_request = json_request.Substring(0, len+1);

                        Debug.WriteLine($"Json decodificado: {json_request}");
                        try
                        {
                            JsonSerializerSettings jss = new JsonSerializerSettings();
                            jss.Formatting = Formatting.Indented;
                            jss.CheckAdditionalContent = true;

                            user_data = JsonConvert.DeserializeObject<User>(json_request,jss);
                            user_data.ip = user.Client.RemoteEndPoint.ToString();

                            if (!username_showed)
                            {
                                fm.WriteLog($"IP do usuário {user_data.nome}: {clean_ip}\n");
                                username_showed = true;
                            }

                            // Se chegamos até aqui, então o json enviado está correto

                            if (user_data.modo_aviao == 0 || user_data.bluetooth == 1 || user_data.saiu == 1)
                            {
                                // Usuário está utilizando a calculadora incorretamente
                                if (!usuario_errou)
                                {
                                    frmMain.log.Write($"O usuário {user_data.nome} ({user_data.serial}, {user_data.ip}) está utilizando a calculadora incorretamente. <==\n");
                                    if (user_data.modo_aviao == 0)
                                        frmMain.log.Write($"Razão: {user_data.nome} desligou o modo avião. <==\n");
                                    else if (user_data.bluetooth == 1)
                                        frmMain.log.Write($"Razao: {user_data.nome} ligou o bluetooth. <==\n");
                                    else
                                        frmMain.log.Write($"Razão: {user_data.nome} saiu da janela do aplicativo. <==\n");

                                    // WARNING: se o nome de usuário tiver vírgula, o funcionamento do sistema pode ser comprometido
                                    File.AppendAllText(blacklist_file, $"{clean_ip},{user_data.nome},{user_data.serial}\n");

                                    fm.showBalooonTip("Alerta", $"{user_data.nome} está utilizando a calculadora incorretamente", ToolTipIcon.Warning, 2000);
                                    usuario_errou = true;
                                }

                                fm.WriteLog($"O usuário {user_data.nome} ({user_data.serial}) está utilizando a calculadora incorretamente. <==\n");
                            }
                        } catch(Exception jse)
                        {
                            Debug.WriteLine($"Erro: {jse.Message}");
                        }

                    } catch(IOException IOE)
                    {
                        Debug.WriteLine($"Erro ao ler dados:\n\n{IOE.Message}\n");
                        frmMain.log.Write($"Erro ao ler dados:\n\n{IOE.Message}\n");
                    }
                }
                Thread.Sleep(5000);
            }

            users--;
            fm.WriteLog($"O usuário {user_data.nome} foi desconectado\n");
            Debug.WriteLine($"O IP{clean_ip} foi desconectado\n");
        }
		
        private string getIpFromRemoteEndPointString(string rmt = "")
        {
            int index = -1;

            if (string.IsNullOrEmpty(rmt))
                return "";

            index = rmt.IndexOf(':');
            return (rmt.Substring(0, index));
        }

        public static bool isConnectedToInternet()
        {
            Ping ping = new Ping();
            bool ok = true;

            try
            {
                PingReply pr = ping.Send("www.google.com.br");
                if (pr.Status == IPStatus.Success)
                    ok = true;
                else
                    ok = false;

            } catch(PingException e)
            {
                Logger logger = new Logger(true);
                logger.WriteStackTrace(e);
                ok = false;
            }

            return ok;
        }
    }
}
