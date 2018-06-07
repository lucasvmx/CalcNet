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

        private SoundPlayer player = null;
        string audios_dir = "";
        private frmMain fm = null;
        private static int users = 0;
        
        public Connections()
        {
            audios_dir = Environment.CurrentDirectory + "\\audios";
            fm = frmMain.fMain;
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
                        }

                        users++;
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

            user.Client.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.KeepAlive, true);
            Thread.Sleep(1000);

            fm.writeLog($"Usuário conectado: {user.Client.RemoteEndPoint.ToString()}\n", frmMain.INFO);
            frmMain.log.Write($"Usuário conectado: {user.Client.RemoteEndPoint.ToString()}\n");
            frmMain.log.Write($"KeepAlive: {user.Client.GetSocketOption(SocketOptionLevel.Socket, SocketOptionName.KeepAlive).ToString()}\n");

            userStream = user.GetStream();
            while (user.Connected)
            {
                /* 
                    Primeiramente, eu preciso verificar se o cliente enviou um JSON contendo os seguintes dados:
                    Nome e MAC address
                */

                if (frmMain.bStopServer)
                {
                    Debug.WriteLine($"Desconectando usuário {user.Client.RemoteEndPoint.ToString()}");
                    fm.writeLog($"Desconectando usuário {user.Client.RemoteEndPoint.ToString()}",frmMain.INFO);
                    user.Client.Close();
                    users--;
                    break;
                }

                if(userStream.CanRead && userStream.DataAvailable)
                {
                    try
                    {
                        bytes_lidos = userStream.Read(memoria, 0, 512);

                        json_request = Encoding.ASCII.GetString(memoria);

                        try
                        {
                            user_data = JsonConvert.DeserializeObject<User>(json_request);
                            user_data.ip = user.Client.RemoteEndPoint.ToString();

                            if (!username_showed)
                            {
                                fm.writeLog($"O Ip do usuario {user_data.nome} é: {user_data.ip}\n", frmMain.INFO);
                                username_showed = true;
                                fm.addUserToTree(user_data, users - 1);
                            }
                            Debug.WriteLine($"Modo aviao: {user_data.modo_aviao}\nBluetooth: {user_data.bluetooth}\nNome: {user_data.nome}\nIp: {user_data.ip}\nSerial: {user_data.serial}");
                        } catch(Exception e)
                        {
                            Debug.WriteLine($"failed to parse json request: {e.Message}");
                        }

                        /* Se chegamos até aqui, então o json enviado está correto */

                        /*
                            Exemplo de JSON:

                            "nome"  :   "carlos ferreira",
                            "serial"   : "200fe766",
                            "ip"    :   "192.168.3.32",
                            "bluetooth" : 0,
                            "modo_aviao" : 1

                            Se modo avião for 0 ou se bluetooth for 1, então o usuário é suspeito
                        */

                        if (user_data.modo_aviao == 0 || user_data.bluetooth == 1)
                        {
                            /* Usuário está utilizando a calculadora incorretamente */

                            fm.writeLog($"O usuário {user_data.nome}, {user_data.serial} está utilizando a calculadora incorretamente.\n", frmMain.ERROR);                           
                            frmMain.log.Write($"O usuário {user_data.nome}, {user_data.serial} está utilizando a calculadora incorretamente\n");
                            fm.showWarningGif(true);
                            player = new SoundPlayer($"{audios_dir}\\Red Alert-SoundBible.com-108009997.wav");
                            player.Play();
                        } else
                        {
                            fm.showWarningGif(false);
                        }
                    } catch(IOException IOE)
                    {
                        Debug.WriteLine($"Erro ao ler dados:\n\n{IOE.Message}\n");
                        frmMain.log.Write($"Erro ao ler dados:\n\n{IOE.Message}\n");
                    }
                }
            }
        }
    }
}
