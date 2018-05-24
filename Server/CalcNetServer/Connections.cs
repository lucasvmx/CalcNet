using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Windows.Forms;
[using System.IO;

namespace CalcNetServer
{
    class Connections
    {
        private string ip;
        private int porta;

        public Connections(string ip, int porta)
        {
            this.ip = ip;
            this.porta = porta;
        }

        public void EscutarConexoes()
        {
            TcpClient usuario;
            TcpListener tcpListener = new TcpListener(IPAddress.Parse(ip), porta);

            try
            {
                tcpListener.Start();
            } catch(SocketException)
            {
                throw;
            }

            while (true)
            {
                try
                {
                    usuario = tcpListener.AcceptTcpClient();
                    frmMain.Log.OutputLog($"Usuário conectado: {usuario.Client.RemoteEndPoint.ToString()}");

                } catch(SocketException)
                {
                    throw;
                }

                /* Para cada cliente conectado, é necessário fazer um processamento paralelo */
                
                Thread sThread = new Thread(new ParameterizedThreadStart(GerenciarConexao));
                sThread.IsBackground = true;

                try
                {
                    sThread.Start(usuario);
                } catch(OutOfMemoryException)
                {
                    throw;
                }
            }
        }

        private void GerenciarConexao(object usuario)
        {
            /* usuario é uma classe do tipo TcpClient e por isso precisa ser convertida */
            TcpClient user = usuario as TcpClient;
            NetworkStream userStream;
            byte[] memoria = new byte[512];
            int bytes_lidos = 0;

            while (user.Connected)
            {
                /* 
                    Primeiramente, eu preciso verificar se o cliente enviou um JSON contendo os seguintes dados:
                    Nome e MAC address
                */

                userStream = user.GetStream();
                if(userStream.CanRead)
                {
                    try
                    {
                        bytes_lidos = userStream.Read(memoria, 0, 512);
                    } catch(IOException IOE)
                    {
                        MessageBox.Show($"Erro ao ler dados:\n\n{IOE.Message}");
                    }
                }
            }
        }
    }
}
