/*
    frmMain.cs

    Interação lógica para a janela principal do programa.

    Autor: Lucas Vieira de Jesus
*/

using System;
using System.ComponentModel;
using System.Drawing;
using System.Threading;
using System.Windows.Forms;
using System.Net;
using System.Net.Sockets;
using System.Diagnostics;
using System.Net.NetworkInformation;

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

        public static int OK = 1;
        public static int ERROR = 2;
        public static int INFO = 3;
        public static int WARNING = 4;

        /* Variáveis */
        protected delegate void set_rtf_text(string text);
        BackgroundWorker serverWorker = null;
        BackgroundWorker serverStatusWorker = null;
        public static frmMain fMain = null;
        Color defaultForeColor, defaultBackColor;
        public static volatile bool bServerIsRunning;
        public static volatile bool bStopServer;
        public static volatile string nomeRedeWifi = "";
        protected Image okImg;
        protected Image errImg;

        /* Aqui o código realmente começa */

        public frmMain()
        {
            log = new Logger();
            InitializeComponent();

            fMain = this; /* Necessário para que outro arquivo-fonte acesse a função outputLog definida aqui */

            Text = $"{AutoRevision.VersionInfo.VcsBasename} {AutoRevision.VersionInfo.VcsTag} compilãção {AutoRevision.VersionInfo.VcsNum}";
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
            notifyIcon.ShowBalloonTip(3000, "CalcNet - Servidor", "Seja bem vindo ao CalcNet", ToolTipIcon.Info);
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
                                toolStripStatusLabel1.Text = "Status: offline";
                            }));
                        } catch(Exception)
                        {
                            // TODO: adicionar tratamento de exceção aqui
                        }
                    }
                }
                Thread.Sleep(5000);
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
                    bServerIsRunning = false;
                    bStopServer = false;
                    conexaoServidor.EscutarConexoes(); // Esta função possui um loop "infinito" dentro dela
                }
            } catch(Exception E)
            {
                MessageBox.Show("Erro ao inicar escuta de conexões\n\n" + E.Message, "Erro",MessageBoxButtons.OK,MessageBoxIcon.Error);
                bServerIsRunning = false;
                bStopServer = true;
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
                    label_users_online.Text = $"{Connections.users}";
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
            if(!temos_ip || !temos_porta)
            {
                MessageBox.Show(this, "Erro ao iniciar servidor:\n\nVocê precisa escolher uma porta e um endereço de IP válidos.", "Erro", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            if (tabControl1.SelectedIndex != 2)
                tabControl1.SelectedIndex = 2;

            /* Iniciamos o servidor */
            try
            {
                richTextBox_output.ResetText();
                serverWorker.RunWorkerAsync();
            } catch(InvalidOperationException IOE)
            {
                MessageBox.Show("Não foi possível iniciar o servidor\n\n" + IOE.Message, "Erro", MessageBoxButtons.OK, MessageBoxIcon.Error);
                bStopServer = true;
            }
        }

        public void writeLog(string texto, int type)
        {
            if (richTextBox_output.InvokeRequired)
            {
                richTextBox_output.Invoke(new Action(() =>
                {
                    if (type == ERROR)
                    {
                        richTextBox_output.ForeColor = Color.Red;
                        richTextBox_output.AppendText("[!] ");
                    } else if(type == WARNING)
                    {
                        richTextBox_output.ForeColor = Color.DarkGoldenrod;
                        richTextBox_output.AppendText("[!] ");
                    } else if(type == INFO)
                    {
                        richTextBox_output.ForeColor = Color.DarkBlue;
                        richTextBox_output.AppendText("[i] ");
                    } else if(type == OK)
                    {
                        richTextBox_output.ForeColor = Color.DarkGreen;
                        richTextBox_output.AppendText("[+] ");
                    }

                    richTextBox_output.AppendText(texto);
                }));
            }
            else
            {
                if (type == ERROR)
                {
                    richTextBox_output.ForeColor = Color.Red;
                    richTextBox_output.AppendText("[!] ");
                }
                else if (type == WARNING)
                {
                    richTextBox_output.ForeColor = Color.DarkGoldenrod;
                    richTextBox_output.AppendText("[!] ");
                }
                else if (type == INFO)
                {
                    richTextBox_output.ForeColor = Color.DarkBlue;
                    richTextBox_output.AppendText("[i] ");
                }
                else if (type == OK)
                {
                    richTextBox_output.ForeColor = Color.DarkGreen;
                    richTextBox_output.AppendText("[+] ");
                }
                else
                {
                    richTextBox_output.ForeColor = Color.Pink;
                }

                richTextBox_output.AppendText(texto);
            }
        }

        private void OnBotaoPararServer_Clicked(object sender, EventArgs e)
        {
            if (serverWorker.IsBusy)
            {
                toolStripStatusLabel1.Text = "Parando ...";
                bStopServer = true;
                serverWorker.CancelAsync();
                richTextBox_output.Clear();
                Connections.users = 0;
            } else
            {

                MessageBox.Show("O servidor não está em execução", "Informação", MessageBoxButtons.OK, MessageBoxIcon.Information);
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

        public void showBalooonTip(string titulo, string texto, ToolTipIcon icone, int timeout)
        {
            notifyIcon.ShowBalloonTip(timeout, titulo, texto, ToolTipIcon.Info);
        }

        private static frmAbout frm = null;

        private void manualDoUsuárioToolStripMenuItem_Click(object sender, EventArgs e)
        {
            MessageBox.Show(this, "Ainda não implementado", "Informação", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        private void sobreOCalcNetToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (frm != null && frm.Visible)
            {
                MessageBox.Show(this, "A janela já está aberta", "Erro", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            else
            {
                if (frm == null)
                {
                    frm = new frmAbout();
                    frm.Show(this);
                    frm = null;
                }
            }
        }
    }
}
