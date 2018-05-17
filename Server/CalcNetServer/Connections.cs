using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Windows.Forms;

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
            
            while(user.Connected)
            {
                /* Aqui nós realizamos toda a parte de monitoramento de usuário individualmente */
            }
        }
    }
}
