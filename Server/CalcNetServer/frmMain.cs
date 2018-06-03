/*
    frmMain.cs

    Interação lógica para a janela principal do programa.

    Autor: Lucas Vieira de Jesus
*/

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
using System.Diagnostics;

namespace CalcNetServer
{
    public partial class frmMain : Form
    {
        internal static Logger log;
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

        public static int DEBUG = 1;
        public static int VERBOSE = 2;
        public static int ERROR = 3;
        public static int EMPTY = 4;
        
        /* Variáveis */
        BackgroundWorker serverWorker = null;
        BackgroundWorker serverStatusWorker = null;
        protected static volatile frmMain fMain = null;
        Color defaultForeColor, defaultBackColor;
        internal static volatile bool bServerIsRunning;
        internal static volatile bool bStopServer;
        protected Image okImg;
        protected Image errImg;

        /* Aqui o código realmente começa */

        public frmMain()
        {
            log = new Logger();
            InitializeComponent();

            Text = $"{AutoRevision.VersionInfo.VcsBasename} {AutoRevision.VersionInfo.VcsTag} build {AutoRevision.VersionInfo.VcsNum}";
            label_tab1_message1.Text = "";
            label_tab1_message2.Text = "";
            
            /* Selecionar a porta padrão */
            comboBox_tab1_porta.SelectedIndex = 0;
            porta = Convert.ToInt32(comboBox_tab1_porta.Text);

            log.Write($"Porta escolhida (padrão): {porta}\n");

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

            log.Write($"IP escolhido (padrão): {ip_padrao.ToString()}\n");

            bStopServer = false;

            defaultForeColor = richTextBox_output.ForeColor;
            defaultBackColor = richTextBox_output.BackColor;

            textBox_tab1_ip.Text = ip_padrao.ToString();

            serverStatusWorker = new BackgroundWorker();
            serverStatusWorker.WorkerReportsProgress = false;
            serverStatusWorker.WorkerSupportsCancellation = true;
            serverStatusWorker.DoWork += ServerStatusWorker_DoWork;

            serverWorker = new BackgroundWorker();
            serverWorker.WorkerReportsProgress = true;
            serverWorker.WorkerSupportsCancellation = true;
            serverWorker.DoWork += ServerWorker_DoWork;

            serverStatusWorker.RunWorkerAsync();
            fMain = this; /* Necessário para que outro arquivo-fonte acesse a função outputLog definida aqui */
        }

        private void ServerStatusWorker_DoWork(object sender, DoWorkEventArgs e)
        {
            while(true)
            {
                if(bServerIsRunning)
                {
                    if (InvokeRequired)
                    {
                        try
                        {
                            Invoke(new Action(() =>
                            {
                                statusStrip1.BackColor = Color.Green;
                                toolStripStatusLabel1.BackColor = Color.Green;
                                toolStripStatusLabel1.Text = "Status: online";
                            }));
                        } catch(Exception)
                        {
                            // TODO: adicionar tratamento de exceção aqui
                        }
                    }
                } else
                {
                    if (InvokeRequired)
                    {
                        try
                        {
                            Invoke(new Action(() =>
                            {
                                statusStrip1.BackColor = Color.Red;
                                toolStripStatusLabel1.BackColor = Color.Red;
                                toolStripStatusLabel1.Text = "Status: offline";
                            }));
                        } catch(Exception)
                        {
                            // TODO: adicionar tratamento de exceção aqui
                        }
                    }
                }
                Thread.Sleep(1000);
            }
        }

        private void ServerWorker_DoWork(object sender, DoWorkEventArgs e)
        {
            /* Este é o código da thread principal de gerenciamento de conexões */

            Connections conexaoServidor = new Connections();

            try
            {
                if (serverWorker.CancellationPending)
                {
                    MessageBox.Show("A escuta de conexões foi cancelada", "Informação", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
                else
                {
                    conexaoServidor.EscutarConexoes(); // Esta função possui um loop "infinito" dentro dela
                }
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

        public void UI_OutputLog(string texto, int tipo)
        {   
            if(!richTextBox_output.InvokeRequired)
            {
                if (tipo == DEBUG)
                    richTextBox_output.ForeColor = Color.Yellow;
                else if (tipo == ERROR)
                    richTextBox_output.ForeColor = Color.Red;
                else if (tipo == VERBOSE)
                    richTextBox_output.ForeColor = Color.White;
                else
                    richTextBox_output.ForeColor = Color.Lavender;

                richTextBox_output.AppendText($"{texto}\n");
                richTextBox_output.ForeColor = DefaultForeColor;
            } else
            {
                richTextBox_output.Invoke(new Action(() =>
                {
                    if (tipo == DEBUG)
                        richTextBox_output.ForeColor = Color.Yellow;
                    else if (tipo == ERROR)
                        richTextBox_output.ForeColor = Color.Red;
                    else if (tipo == VERBOSE)
                        richTextBox_output.ForeColor = Color.White;
                    else
                        richTextBox_output.ForeColor = Color.Lavender;

                    richTextBox_output.AppendText($"{texto}\n");
                    richTextBox_output.ForeColor = DefaultForeColor;
                }));
            }
        }

        private void OnBotaoPararServer_Clicked(object sender, EventArgs e)
        {
            if (serverWorker.IsBusy)
            {
                toolStripStatusLabel1.Text = "Parando ...";
                bStopServer = true;
                serverWorker.CancelAsync();
            } else
            {

                MessageBox.Show("O servidor não está em execução!", "Erro", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void sairToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void OnBotaoExibirLogs_Clicked(object sender, EventArgs e)
        {
            ProcessStartInfo psi = new ProcessStartInfo();
            Process p = new Process();

            try
            {
                psi.UseShellExecute = true;
                psi.FileName = $"notepad.exe";
                psi.LoadUserProfile = true;
                psi.Arguments = log.logname;

                p.StartInfo = psi;
                p.Start();
            } catch(Exception E)
            {
                MessageBox.Show($"Erro ao exibir logs:\n\n{E.Message}", "Um erro ocorreu", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }
}
