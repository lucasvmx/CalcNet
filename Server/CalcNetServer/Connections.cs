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

namespace CalcNetServer
{
    class Connections : frmMain
    {
        public Connections()
        {

        }

        public void EscutarConexoes()
        {
            TcpClient usuario;
            TcpListener tcpListener = new TcpListener(IPAddress.Parse(ip), porta);

            try
            {
                tcpListener.Start();
                bServerIsRunning = true;
            } catch(SocketException)
            {
                throw;
            }

            Debug.WriteLine("tcpListener started");
            

            while (true)
            {
                Debug.WriteLine($"bStopServer {bStopServer}");
                if (bStopServer)
                {
                    tcpListener.Stop();
                    fMain.UI_OutputLog("A escuta de conexões foi terminada", VERBOSE);
                    break;
                }

                if (tcpListener.Pending())
                {
                    try
                    {
                        Debug.WriteLine("Wait for tcpClient");
                        usuario = tcpListener.AcceptTcpClient();
                        fMain.UI_OutputLog($"Usuário conectado: {usuario.Client.RemoteEndPoint.ToString()}", frmMain.VERBOSE);
                    }
                    catch (SocketException)
                    {
                        throw;
                    }


                    /* Para cada cliente conectado, é necessário fazer um processamento paralelo */

                    Thread sThread = new Thread(new ParameterizedThreadStart(GerenciarConexao))
                    {
                        IsBackground = true
                    };

                    try
                    {
                        sThread.Start(usuario);
                    }
                    catch (OutOfMemoryException)
                    {
                        throw;
                    }
                }
            }

            bServerIsRunning = false;
            bStopServer = false;
        }

        private void GerenciarConexao(object usuario)
        {
            /* usuario é uma classe do tipo TcpClient e por isso precisa ser convertida */
            TcpClient user = usuario as TcpClient;
            NetworkStream userStream;
            byte[] memoria = new byte[512];
            int bytes_lidos = 0;
            string json_request = "";
            User user_data = null;

            try
            {
                user.Client.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.KeepAlive, true);
            } catch(SocketException SE)
            {
                MessageBox.Show($"Falha ao mudar flag de conexão:\n\n{SE.Message}", "Erro crítico", MessageBoxButtons.OK, MessageBoxIcon.Error);
                Application.Exit();
            } catch(ObjectDisposedException ODE)
            {
                MessageBox.Show($"Falha ao mudar flag de conexão:\n\n{ODE.Message}", "Erro crítico", MessageBoxButtons.OK, MessageBoxIcon.Error);
                Application.Exit();
            }

            userStream = user.GetStream();
            while (user.Connected)
            {
                /* 
                    Primeiramente, eu preciso verificar se o cliente enviou um JSON contendo os seguintes dados:
                    Nome e MAC address
                */

                frmMain.fMain.UI_OutputLog("Usuário online", frmMain.VERBOSE);
                if(userStream.CanRead)
                {
                    try
                    {
                        bytes_lidos = userStream.Read(memoria, 0, 512);
                        json_request = bytes_lidos.ToString();
                        user_data = JsonConvert.DeserializeObject<User>(json_request);

                        /* Se chegamos até aqui, então o json enviado está correto */

                        /*
                            Exemplo de JSON:

                            "nome"  :   "carlos ferreira",
                            "mac"   :   "ab:cd:ef:12:34:56",
                            "ip"    :   "192.168.3.32",
                            "bluetooth" : 0,
                            "modo_aviao" : 1

                            Se modo avião for 0 ou se bluetooth for 1, então o usuário é suspeito
                        */

                        if(user_data.modo_aviao == 0 || user_data.bluetooth == 1)
                        {
                            /* Usuário está utilizando a calculadora incorretamente */
                            log.Write($"O usuário {user_data.nome}, {user_data.mac} está utilizando a calculadora incorretamente\n");
                        }
                    } catch(IOException IOE)
                    {
                        MessageBox.Show($"Erro ao ler dados:\n\n{IOE.Message}");
                    }
                }
            }

            fMain.UI_OutputLog($"O Aluno do endereço {user.Client.RemoteEndPoint.ToString()} se desconectou", ERROR);
        }
    }
}
