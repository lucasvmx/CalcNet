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
using System.Drawing;
using System.Media;
using System.Text;

namespace CalcNetServer
{
    class Connections
    {
        public static int MAX_CONNECTIONS = 250;

        string audios_dir = "";
        private frmMain fm = null;
        public static volatile int users = 0;
        private string blacklist_file = "blacklist.db";
        StreamWriter sw;

        public Connections()
        {
            audios_dir = Environment.CurrentDirectory + "\\audios";
            fm = frmMain.fMain;
            users = 0;
            sw = File.CreateText(blacklist_file);
            sw.WriteLine($"# Usuários temporariamente bloqueados do CalcNet");
            sw.Close();
        }

        public void EscutarConexoes()
        {
            TcpClient usuario;
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

            fm.writeLog($"Aguardando conexões no IP {frmMain.ip} => Porta {frmMain.porta}\n", frmMain.INFO);
            Debug.WriteLine($"Aguardando conexões no IP {frmMain.ip} => Porta {frmMain.porta}");
            
            while (true)
            {
                if (frmMain.bStopServer)
                {
                    try
                    {
                        tcpListener.Server.Close();
                        tcpListener.Stop();
                        fm.writeLog("A escuta de conexões foi terminada\n", frmMain.INFO);
                    } catch(Exception e)
                    {
                        fm.writeLog($"{e.Message}",frmMain.ERROR);
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
                }
                Thread.Sleep(1000);
            }

            frmMain.bServerIsRunning = false;
            frmMain.bStopServer = false;
        }

        private void GerenciarConexao(object usuario, int id)
        {
            /* usuario é uma classe do tipo TcpClient e por isso precisa ser convertida */
            TcpClient user = usuario as TcpClient;
            NetworkStream userStream;
            byte[] memoria = new byte[512];
            int bytes_lidos = 0;
            string json_request = "";
            User user_data = null;
            bool username_showed = false;
            bool usuario_errou = false;
            bool blacklisted = false;
            string clean_ip = "";
            string[] blacklisted_user_data = { "" };

            user.Client.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.KeepAlive, true);
            Thread.Sleep(500);

            clean_ip = getIpFromRemoteEndPointString(user.Client.RemoteEndPoint.ToString());
            fm.writeLog($"Usuário conectado: {user.Client.RemoteEndPoint.ToString()}, id {users}\n", frmMain.INFO);
            frmMain.log.Write($"Usuário conectado: {user.Client.RemoteEndPoint.ToString()}\n");

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
                //Debug.WriteLine($"O usuário {blacklisted_user_data[1]}, id {blacklisted_user_data[2]} está bloqueado temporariamente.\n",frmMain.WARNING);
                user.Client.Close();
                users--;
                return;
            }

            userStream = user.GetStream();
            
            while (user.Connected && !blacklisted)
            {
                if (frmMain.bStopServer || usuario_errou)
                {
                    Debug.WriteLine($"Desconectando usuário {user.Client.RemoteEndPoint.ToString()}");
                    fm.writeLog($"Desconectando usuário {user.Client.RemoteEndPoint.ToString()}\n",frmMain.INFO);
                    user.Client.Close();
                    if (usuario_errou)
                    {
                        fm.writeLog($"O usuario {user_data.nome} estava utilizando a calculadora incorretamente\n", frmMain.ERROR);
                        Thread.Sleep(10000);
                    }
                    break;
                }

                if(userStream.CanRead && userStream.DataAvailable)
                {
                    try
                    {
                        memoria.Initialize();
                        bytes_lidos = userStream.Read(memoria, 0, 512);

                        json_request = "";
                        json_request = Encoding.ASCII.GetString(memoria, 0, bytes_lidos);

                        Debug.WriteLine($"Json: {json_request}");
                        try
                        {
                            user_data = JsonConvert.DeserializeObject<User>(json_request);
                            user_data.ip = user.Client.RemoteEndPoint.ToString();

                            if (!username_showed)
                            {
                                fm.writeLog($"O Ip do usuario {user_data.nome} é: {user_data.ip}\n", frmMain.INFO);
                                username_showed = true;
                            }

                            //fm.addUserToTree(user_data, users - 1);
                        } catch(Exception e)
                        {
                            Debug.WriteLine($"failed to parse json request: {e.Message}");
                        }

                        // Se chegamos até aqui, então o json enviado está correto

                        if (user_data.modo_aviao == 0 || user_data.bluetooth == 1 || user_data.saiu == 1)
                        {
                            // Usuário está utilizando a calculadora incorretamente
                            if (!usuario_errou)
                            {
                                frmMain.log.Write($"O usuário {user_data.nome} ({user_data.serial}, {user_data.ip}) está utilizando a calculadora incorretamente.\n");
                                if(user_data.modo_aviao == 0)
                                    frmMain.log.Write($"Razao: {user_data.nome} desligou o modo avião.\n");
                                else if(user_data.bluetooth == 1)
                                    frmMain.log.Write($"Razao: {user_data.nome} desligou o bluetooth.\n");
                                else
                                    frmMain.log.Write($"Razao: {user_data.nome} saiu da janela do aplicativo.\n");

                                // WARNING: se o nome de usuário tiver vírgula, o funcionamento do sistema pode ser comprometido
                                File.AppendAllText(blacklist_file, $"{clean_ip},{user_data.nome},{user_data.serial}\n");

                                fm.showBalooonTip("Alerta", $"{user_data.nome} está utilizando a calculadora incorretamente", ToolTipIcon.Warning, 2000);
                                usuario_errou = true;
                            }

                            fm.writeLog($"O usuário {user_data.nome} ({user_data.serial}) está utilizando a calculadora incorretamente.\n", frmMain.ERROR);
                        }
                    } catch(IOException IOE)
                    {
                        Debug.WriteLine($"Erro ao ler dados:\n\n{IOE.Message}\n");
                        frmMain.log.Write($"Erro ao ler dados:\n\n{IOE.Message}\n");
                    }
                }
                Thread.Sleep(2000);
            }

            users--;
            fm.writeLog($"O usuário {user_data.nome} foi desconectado\n", frmMain.WARNING);
        }

        private string getIpFromRemoteEndPointString(string rmt = "")
        {
            int index = -1;

            if (string.IsNullOrEmpty(rmt))
                return "";

            index = rmt.IndexOf(':');
            return (rmt.Substring(0, index));
        }
    }
}
