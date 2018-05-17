using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Windows.Forms;
using System.Net;
using System.Net.Sockets;

namespace CalcNetServer
{
    public partial class frmMain : Form
    {
        internal static string ip = "";
        internal static int porta = -1;
        public static bool temos_ip
        {
            get
            {
                if (string.IsNullOrEmpty(ip) || string.IsNullOrWhiteSpace(ip))
                    return false;

                return true;
            }
        }
        public static bool temos_porta
        {
            get
            {
                if (porta == -1)
                    return false;

                return true;
            }
        }

        /* Variáveis */
        BackgroundWorker serverWorker = null;
        public static frmMain Log;

        /* Aqui o código realmente começa */

        public frmMain()
        {
            InitializeComponent();
            Text = $"{AutoRevision.VersionInfo.VcsBasename} {AutoRevision.VersionInfo.VcsTag} build {VcsNum}";
            label_tab1_message1.Text = "";
            label_tab1_message2.Text = "";

            Log = this; /* Necessário para que outro arquivo-fonte acesse a função outputLog definida aqui */

            /* Selecionar a porta padrão */
            comboBox_tab1_porta.SelectedIndex = 0;
            porta = Convert.ToInt32(comboBox_tab1_porta.Text);

            /* Escolher o endereço de IP padrão */
            IPHostEntry iPHostEntry = Dns.GetHostEntry(Dns.GetHostName());
            IPAddress ip_padrao = IPAddress.Loopback;

            foreach(IPAddress ip in iPHostEntry.AddressList)
            {
                if (ip.AddressFamily == AddressFamily.InterNetwork)
                {
                    ip_padrao = ip;
                    break;
                }
            }

            textBox_tab1_ip.Text = ip_padrao.ToString();

            serverWorker = new BackgroundWorker();
            serverWorker.WorkerReportsProgress = true;
            serverWorker.WorkerSupportsCancellation = true;
            serverWorker.DoWork += ServerWorker_DoWork;
        }

        private void ServerWorker_DoWork(object sender, DoWorkEventArgs e)
        {
            /* Este é o código da thread principal de gerenciamento de conexões */

            Connections conexaoServidor = new Connections(ip, porta);

            try
            {
                OutputLog($"O Servidor do CalcNet está aguardando por usuários em: {ip}:{porta}");
                conexaoServidor.EscutarConexoes(); // Esta função possui um loop "infinito" dentro dela
            } catch(Exception E)
            {
                MessageBox.Show("Erro ao inicar escuta de conexões\n\n" + E.Message, "Erro",MessageBoxButtons.OK,MessageBoxIcon.Error);
            }
        }

        private void tabControl1_SelectedIndexChanged(object sender, EventArgs e)
        {
            int index_atual = tabControl1.SelectedIndex;
            
            switch(index_atual)
            {
                case 0:
                    break;

                case 1:
                    if(temos_ip)
                    {
                        label_tab2_ip.Text = ip;
                    } else
                    {
                        label_tab2_ip.Text = "Insira um endereço de ip válido!!!";
                    }
                    if(temos_porta)
                    {
                        label_tab2_porta.Text = comboBox_tab1_porta.Text;
                    } else
                    {
                        label_tab2_porta.Text = "Selecione uma porta de conexão!!!";
                    }

                    OperatingSystem os = Environment.OSVersion;

                    label_tab2_usuario.Text = Environment.UserName;
                    label_tab2_os.Text = os.VersionString + " " + ((Environment.Is64BitOperatingSystem) ? "x64":"x86");
                    
                    break;
            }
        }

        private void textBox_tab1_ip_TextChanged(object sender, EventArgs e)
        {
            IPAddress ip_addr;

            try
            {
                ip_addr = IPAddress.Parse(textBox_tab1_ip.Text);
                label_tab1_message2.Text = $"Endereço válido: {ip_addr.ToString()}";
                label_tab1_message2.ForeColor = Color.Green;
                ip = ip_addr.ToString();
            } catch(FormatException)
            {
                label_tab1_message2.ForeColor = Color.Red;
                label_tab1_message2.Text = "Endereço de IP inválido";
            }
        }

        private void botaoIniciarServidor_Click(object sender, EventArgs e)
        {
            /* Verificar se o usuário escolher um IP e porta adequadamente */
            if(!temos_ip || !temos_porta)
            {
                MessageBox.Show("Erro ao iniciar servidor:\n\nVocê precisa escolher uma porta e um endereço de IP válidos.", "Erro", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            if (tabControl1.SelectedIndex != 3)
                tabControl1.SelectedIndex = 3;

            /* Iniciamos o servidor */
            try
            {
                serverWorker.RunWorkerAsync();
            } catch(InvalidOperationException IOE)
            {
                MessageBox.Show("Um erro ocorreu ao iniciar o servidor\n\n" + IOE.Message, "Erro", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        public void OutputLog(string texto)
        {   
            if(!richTextBox_output.InvokeRequired)
            {
                richTextBox_output.Text += texto + "\n";
            } else
            {
                richTextBox_output.Invoke(new Action(() =>
                {
                    richTextBox_output.Text += texto + "\n";
                }));
            }
        }
    }
}
