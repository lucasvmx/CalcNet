namespace CalcNetServer
{
    partial class frmMain
    {
        /// <summary>
        /// Variável de designer necessária.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Limpar os recursos que estão sendo usados.
        /// </summary>
        /// <param name="disposing">true se for necessário descartar os recursos gerenciados; caso contrário, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Código gerado pelo Windows Form Designer

        /// <summary>
        /// Método necessário para suporte ao Designer - não modifique 
        /// o conteúdo deste método com o editor de código.
        /// </summary>
        private void InitializeComponent()
        {
            this.botaoIniciarServidor = new System.Windows.Forms.Button();
            this.botaoPararServidor = new System.Windows.Forms.Button();
            this.botaoExibirLogs = new System.Windows.Forms.Button();
            this.tabControl1 = new System.Windows.Forms.TabControl();
            this.tabPage1 = new System.Windows.Forms.TabPage();
            this.label16 = new System.Windows.Forms.Label();
            this.label15 = new System.Windows.Forms.Label();
            this.label10 = new System.Windows.Forms.Label();
            this.label9 = new System.Windows.Forms.Label();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.label_tab1_message2 = new System.Windows.Forms.Label();
            this.textBox_tab1_ip = new System.Windows.Forms.TextBox();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.label_tab1_message1 = new System.Windows.Forms.Label();
            this.comboBox_tab1_porta = new System.Windows.Forms.ComboBox();
            this.tabPage2 = new System.Windows.Forms.TabPage();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.label_tab2_usuario = new System.Windows.Forms.Label();
            this.label13 = new System.Windows.Forms.Label();
            this.label_tab2_os = new System.Windows.Forms.Label();
            this.label_tab2_porta = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.label11 = new System.Windows.Forms.Label();
            this.label_tab2_ip = new System.Windows.Forms.Label();
            this.tabPage3 = new System.Windows.Forms.TabPage();
            this.treeView1 = new System.Windows.Forms.TreeView();
            this.tabPage4 = new System.Windows.Forms.TabPage();
            this.richTextBox_output = new System.Windows.Forms.RichTextBox();
            this.statusStrip1 = new System.Windows.Forms.StatusStrip();
            this.toolStripStatusLabel1 = new System.Windows.Forms.ToolStripStatusLabel();
            this.menuStrip1 = new System.Windows.Forms.MenuStrip();
            this.arquivoToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.sairToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.ajudaToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.manualDoUsuárioToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.sobreOCalcNetToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.tabControl1.SuspendLayout();
            this.tabPage1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.groupBox1.SuspendLayout();
            this.tabPage2.SuspendLayout();
            this.tableLayoutPanel1.SuspendLayout();
            this.tabPage3.SuspendLayout();
            this.tabPage4.SuspendLayout();
            this.statusStrip1.SuspendLayout();
            this.menuStrip1.SuspendLayout();
            this.SuspendLayout();
            // 
            // botaoIniciarServidor
            // 
            this.botaoIniciarServidor.Font = new System.Drawing.Font("Microsoft JhengHei UI", 15.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.botaoIniciarServidor.Location = new System.Drawing.Point(12, 368);
            this.botaoIniciarServidor.Name = "botaoIniciarServidor";
            this.botaoIniciarServidor.Size = new System.Drawing.Size(171, 53);
            this.botaoIniciarServidor.TabIndex = 0;
            this.botaoIniciarServidor.Text = "Iniciar servidor";
            this.botaoIniciarServidor.UseVisualStyleBackColor = true;
            this.botaoIniciarServidor.Click += new System.EventHandler(this.botaoIniciarServidor_Click);
            // 
            // botaoPararServidor
            // 
            this.botaoPararServidor.Font = new System.Drawing.Font("Microsoft JhengHei UI", 15.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.botaoPararServidor.Location = new System.Drawing.Point(189, 368);
            this.botaoPararServidor.Name = "botaoPararServidor";
            this.botaoPararServidor.Size = new System.Drawing.Size(171, 53);
            this.botaoPararServidor.TabIndex = 1;
            this.botaoPararServidor.Text = "Parar servidor";
            this.botaoPararServidor.UseVisualStyleBackColor = true;
            // 
            // botaoExibirLogs
            // 
            this.botaoExibirLogs.Font = new System.Drawing.Font("Microsoft JhengHei UI", 15.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.botaoExibirLogs.Location = new System.Drawing.Point(366, 368);
            this.botaoExibirLogs.Name = "botaoExibirLogs";
            this.botaoExibirLogs.Size = new System.Drawing.Size(171, 53);
            this.botaoExibirLogs.TabIndex = 2;
            this.botaoExibirLogs.Text = "Exibir logs";
            this.botaoExibirLogs.UseVisualStyleBackColor = true;
            // 
            // tabControl1
            // 
            this.tabControl1.Controls.Add(this.tabPage1);
            this.tabControl1.Controls.Add(this.tabPage2);
            this.tabControl1.Controls.Add(this.tabPage3);
            this.tabControl1.Controls.Add(this.tabPage4);
            this.tabControl1.Font = new System.Drawing.Font("Microsoft JhengHei UI", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.tabControl1.Location = new System.Drawing.Point(12, 27);
            this.tabControl1.Name = "tabControl1";
            this.tabControl1.SelectedIndex = 0;
            this.tabControl1.Size = new System.Drawing.Size(797, 335);
            this.tabControl1.TabIndex = 11;
            this.tabControl1.SelectedIndexChanged += new System.EventHandler(this.tabControl1_SelectedIndexChanged);
            // 
            // tabPage1
            // 
            this.tabPage1.Controls.Add(this.label16);
            this.tabPage1.Controls.Add(this.label15);
            this.tabPage1.Controls.Add(this.label10);
            this.tabPage1.Controls.Add(this.label9);
            this.tabPage1.Controls.Add(this.groupBox2);
            this.tabPage1.Controls.Add(this.groupBox1);
            this.tabPage1.Location = new System.Drawing.Point(4, 33);
            this.tabPage1.Name = "tabPage1";
            this.tabPage1.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage1.Size = new System.Drawing.Size(789, 298);
            this.tabPage1.TabIndex = 0;
            this.tabPage1.Text = "1. Configurações";
            this.tabPage1.UseVisualStyleBackColor = true;
            // 
            // label16
            // 
            this.label16.AutoSize = true;
            this.label16.ForeColor = System.Drawing.Color.Blue;
            this.label16.Location = new System.Drawing.Point(27, 195);
            this.label16.Name = "label16";
            this.label16.Size = new System.Drawing.Size(93, 24);
            this.label16.TabIndex = 6;
            this.label16.Text = "Opcional";
            // 
            // label15
            // 
            this.label15.AutoSize = true;
            this.label15.ForeColor = System.Drawing.Color.Blue;
            this.label15.Location = new System.Drawing.Point(12, 195);
            this.label15.Name = "label15";
            this.label15.Size = new System.Drawing.Size(19, 24);
            this.label15.TabIndex = 5;
            this.label15.Text = "*";
            // 
            // label10
            // 
            this.label10.AutoSize = true;
            this.label10.ForeColor = System.Drawing.Color.Red;
            this.label10.Location = new System.Drawing.Point(27, 162);
            this.label10.Name = "label10";
            this.label10.Size = new System.Drawing.Size(118, 24);
            this.label10.TabIndex = 4;
            this.label10.Text = "Obrigatório";
            // 
            // label9
            // 
            this.label9.AutoSize = true;
            this.label9.ForeColor = System.Drawing.Color.Red;
            this.label9.Location = new System.Drawing.Point(12, 162);
            this.label9.Name = "label9";
            this.label9.Size = new System.Drawing.Size(19, 24);
            this.label9.TabIndex = 3;
            this.label9.Text = "*";
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.label4);
            this.groupBox2.Controls.Add(this.label_tab1_message2);
            this.groupBox2.Controls.Add(this.textBox_tab1_ip);
            this.groupBox2.ForeColor = System.Drawing.Color.Black;
            this.groupBox2.Location = new System.Drawing.Point(350, 20);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(433, 118);
            this.groupBox2.TabIndex = 1;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Endereço de IP:";
            // 
            // label_tab1_message2
            // 
            this.label_tab1_message2.AutoSize = true;
            this.label_tab1_message2.Font = new System.Drawing.Font("Microsoft JhengHei UI", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label_tab1_message2.Location = new System.Drawing.Point(12, 81);
            this.label_tab1_message2.Name = "label_tab1_message2";
            this.label_tab1_message2.Size = new System.Drawing.Size(69, 17);
            this.label_tab1_message2.TabIndex = 2;
            this.label_tab1_message2.Text = "$message";
            // 
            // textBox_tab1_ip
            // 
            this.textBox_tab1_ip.Location = new System.Drawing.Point(15, 31);
            this.textBox_tab1_ip.Name = "textBox_tab1_ip";
            this.textBox_tab1_ip.Size = new System.Drawing.Size(240, 32);
            this.textBox_tab1_ip.TabIndex = 2;
            this.textBox_tab1_ip.TextChanged += new System.EventHandler(this.textBox_tab1_ip_TextChanged);
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.label3);
            this.groupBox1.Controls.Add(this.label_tab1_message1);
            this.groupBox1.Controls.Add(this.comboBox_tab1_porta);
            this.groupBox1.ForeColor = System.Drawing.Color.Black;
            this.groupBox1.Location = new System.Drawing.Point(16, 20);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(328, 118);
            this.groupBox1.TabIndex = 0;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Escolha a porta de conexão:";
            // 
            // label_tab1_message1
            // 
            this.label_tab1_message1.AutoSize = true;
            this.label_tab1_message1.Font = new System.Drawing.Font("Microsoft JhengHei UI", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label_tab1_message1.Location = new System.Drawing.Point(12, 81);
            this.label_tab1_message1.Name = "label_tab1_message1";
            this.label_tab1_message1.Size = new System.Drawing.Size(69, 17);
            this.label_tab1_message1.TabIndex = 3;
            this.label_tab1_message1.Text = "$message";
            // 
            // comboBox_tab1_porta
            // 
            this.comboBox_tab1_porta.FormattingEnabled = true;
            this.comboBox_tab1_porta.Items.AddRange(new object[] {
            "1701",
            "1702",
            "1703",
            "1704",
            "1705",
            "1706",
            "55432"});
            this.comboBox_tab1_porta.Location = new System.Drawing.Point(15, 31);
            this.comboBox_tab1_porta.Name = "comboBox_tab1_porta";
            this.comboBox_tab1_porta.Size = new System.Drawing.Size(240, 32);
            this.comboBox_tab1_porta.TabIndex = 0;
            // 
            // tabPage2
            // 
            this.tabPage2.Controls.Add(this.tableLayoutPanel1);
            this.tabPage2.Location = new System.Drawing.Point(4, 33);
            this.tabPage2.Name = "tabPage2";
            this.tabPage2.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage2.Size = new System.Drawing.Size(789, 298);
            this.tabPage2.TabIndex = 1;
            this.tabPage2.Text = "2. Dados da sessão";
            this.tabPage2.UseVisualStyleBackColor = true;
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.CellBorderStyle = System.Windows.Forms.TableLayoutPanelCellBorderStyle.Single;
            this.tableLayoutPanel1.ColumnCount = 2;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 26.67526F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 73.32474F));
            this.tableLayoutPanel1.Controls.Add(this.label_tab2_usuario, 1, 3);
            this.tableLayoutPanel1.Controls.Add(this.label13, 0, 3);
            this.tableLayoutPanel1.Controls.Add(this.label_tab2_os, 1, 2);
            this.tableLayoutPanel1.Controls.Add(this.label_tab2_porta, 1, 1);
            this.tableLayoutPanel1.Controls.Add(this.label2, 0, 1);
            this.tableLayoutPanel1.Controls.Add(this.label1, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.label11, 0, 2);
            this.tableLayoutPanel1.Controls.Add(this.label_tab2_ip, 1, 0);
            this.tableLayoutPanel1.Location = new System.Drawing.Point(6, 6);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 4;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(777, 131);
            this.tableLayoutPanel1.TabIndex = 10;
            // 
            // label_tab2_usuario
            // 
            this.label_tab2_usuario.AutoSize = true;
            this.label_tab2_usuario.Font = new System.Drawing.Font("Microsoft JhengHei UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label_tab2_usuario.ForeColor = System.Drawing.Color.Blue;
            this.label_tab2_usuario.Location = new System.Drawing.Point(211, 97);
            this.label_tab2_usuario.Name = "label_tab2_usuario";
            this.label_tab2_usuario.Size = new System.Drawing.Size(37, 24);
            this.label_tab2_usuario.TabIndex = 12;
            this.label_tab2_usuario.Text = "???";
            // 
            // label13
            // 
            this.label13.AutoSize = true;
            this.label13.Font = new System.Drawing.Font("Microsoft JhengHei UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label13.ForeColor = System.Drawing.Color.Black;
            this.label13.Location = new System.Drawing.Point(4, 97);
            this.label13.Name = "label13";
            this.label13.Size = new System.Drawing.Size(170, 24);
            this.label13.TabIndex = 11;
            this.label13.Text = "Nome de usuário:";
            // 
            // label_tab2_os
            // 
            this.label_tab2_os.AutoSize = true;
            this.label_tab2_os.Font = new System.Drawing.Font("Microsoft JhengHei UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label_tab2_os.ForeColor = System.Drawing.Color.Blue;
            this.label_tab2_os.Location = new System.Drawing.Point(211, 65);
            this.label_tab2_os.Name = "label_tab2_os";
            this.label_tab2_os.Size = new System.Drawing.Size(236, 24);
            this.label_tab2_os.TabIndex = 11;
            this.label_tab2_os.Text = "Microsoft Windows 7 x64";
            // 
            // label_tab2_porta
            // 
            this.label_tab2_porta.AutoSize = true;
            this.label_tab2_porta.Font = new System.Drawing.Font("Microsoft JhengHei UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label_tab2_porta.ForeColor = System.Drawing.Color.Blue;
            this.label_tab2_porta.Location = new System.Drawing.Point(211, 33);
            this.label_tab2_porta.Name = "label_tab2_porta";
            this.label_tab2_porta.Size = new System.Drawing.Size(159, 24);
            this.label_tab2_porta.TabIndex = 6;
            this.label_tab2_porta.Text = "Não configurada";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Microsoft JhengHei UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.ForeColor = System.Drawing.Color.Black;
            this.label2.Location = new System.Drawing.Point(4, 33);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(169, 24);
            this.label2.TabIndex = 4;
            this.label2.Text = "Porta de conexão:";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft JhengHei UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.ForeColor = System.Drawing.Color.Black;
            this.label1.Location = new System.Drawing.Point(4, 1);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(147, 24);
            this.label1.TabIndex = 3;
            this.label1.Text = "Endereço de IP:";
            // 
            // label11
            // 
            this.label11.AutoSize = true;
            this.label11.Font = new System.Drawing.Font("Microsoft JhengHei UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label11.ForeColor = System.Drawing.Color.Black;
            this.label11.Location = new System.Drawing.Point(4, 65);
            this.label11.Name = "label11";
            this.label11.Size = new System.Drawing.Size(195, 24);
            this.label11.TabIndex = 9;
            this.label11.Text = "Sistema operacional:";
            // 
            // label_tab2_ip
            // 
            this.label_tab2_ip.AutoSize = true;
            this.label_tab2_ip.Font = new System.Drawing.Font("Microsoft JhengHei UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label_tab2_ip.ForeColor = System.Drawing.Color.Blue;
            this.label_tab2_ip.Location = new System.Drawing.Point(211, 1);
            this.label_tab2_ip.Name = "label_tab2_ip";
            this.label_tab2_ip.Size = new System.Drawing.Size(161, 24);
            this.label_tab2_ip.TabIndex = 7;
            this.label_tab2_ip.Text = "Não configurado";
            // 
            // tabPage3
            // 
            this.tabPage3.Controls.Add(this.treeView1);
            this.tabPage3.Location = new System.Drawing.Point(4, 33);
            this.tabPage3.Name = "tabPage3";
            this.tabPage3.Size = new System.Drawing.Size(789, 298);
            this.tabPage3.TabIndex = 2;
            this.tabPage3.Text = "3. Dados de monitoramento";
            this.tabPage3.UseVisualStyleBackColor = true;
            // 
            // treeView1
            // 
            this.treeView1.Location = new System.Drawing.Point(3, 3);
            this.treeView1.Name = "treeView1";
            this.treeView1.Size = new System.Drawing.Size(783, 292);
            this.treeView1.TabIndex = 0;
            // 
            // tabPage4
            // 
            this.tabPage4.Controls.Add(this.richTextBox_output);
            this.tabPage4.Location = new System.Drawing.Point(4, 33);
            this.tabPage4.Name = "tabPage4";
            this.tabPage4.Size = new System.Drawing.Size(789, 298);
            this.tabPage4.TabIndex = 3;
            this.tabPage4.Text = "4. Mensagens";
            this.tabPage4.UseVisualStyleBackColor = true;
            // 
            // richTextBox_output
            // 
            this.richTextBox_output.BackColor = System.Drawing.Color.Black;
            this.richTextBox_output.Font = new System.Drawing.Font("Microsoft JhengHei UI", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.richTextBox_output.ForeColor = System.Drawing.Color.Lime;
            this.richTextBox_output.Location = new System.Drawing.Point(3, 3);
            this.richTextBox_output.Name = "richTextBox_output";
            this.richTextBox_output.ReadOnly = true;
            this.richTextBox_output.Size = new System.Drawing.Size(783, 292);
            this.richTextBox_output.TabIndex = 0;
            this.richTextBox_output.Text = "";
            // 
            // statusStrip1
            // 
            this.statusStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.toolStripStatusLabel1});
            this.statusStrip1.Location = new System.Drawing.Point(0, 428);
            this.statusStrip1.Name = "statusStrip1";
            this.statusStrip1.Size = new System.Drawing.Size(821, 22);
            this.statusStrip1.TabIndex = 12;
            this.statusStrip1.Text = "statusStrip1";
            // 
            // toolStripStatusLabel1
            // 
            this.toolStripStatusLabel1.Name = "toolStripStatusLabel1";
            this.toolStripStatusLabel1.Size = new System.Drawing.Size(103, 17);
            this.toolStripStatusLabel1.Text = "CalcNet - Servidor";
            // 
            // menuStrip1
            // 
            this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.arquivoToolStripMenuItem,
            this.ajudaToolStripMenuItem});
            this.menuStrip1.Location = new System.Drawing.Point(0, 0);
            this.menuStrip1.Name = "menuStrip1";
            this.menuStrip1.Size = new System.Drawing.Size(821, 24);
            this.menuStrip1.TabIndex = 13;
            this.menuStrip1.Text = "menuStrip1";
            // 
            // arquivoToolStripMenuItem
            // 
            this.arquivoToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.sairToolStripMenuItem});
            this.arquivoToolStripMenuItem.Name = "arquivoToolStripMenuItem";
            this.arquivoToolStripMenuItem.Size = new System.Drawing.Size(61, 20);
            this.arquivoToolStripMenuItem.Text = "Arquivo";
            // 
            // sairToolStripMenuItem
            // 
            this.sairToolStripMenuItem.Name = "sairToolStripMenuItem";
            this.sairToolStripMenuItem.Size = new System.Drawing.Size(93, 22);
            this.sairToolStripMenuItem.Text = "Sair";
            // 
            // ajudaToolStripMenuItem
            // 
            this.ajudaToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.manualDoUsuárioToolStripMenuItem,
            this.sobreOCalcNetToolStripMenuItem});
            this.ajudaToolStripMenuItem.Name = "ajudaToolStripMenuItem";
            this.ajudaToolStripMenuItem.Size = new System.Drawing.Size(50, 20);
            this.ajudaToolStripMenuItem.Text = "Ajuda";
            // 
            // manualDoUsuárioToolStripMenuItem
            // 
            this.manualDoUsuárioToolStripMenuItem.Name = "manualDoUsuárioToolStripMenuItem";
            this.manualDoUsuárioToolStripMenuItem.Size = new System.Drawing.Size(173, 22);
            this.manualDoUsuárioToolStripMenuItem.Text = "Manual do usuário";
            // 
            // sobreOCalcNetToolStripMenuItem
            // 
            this.sobreOCalcNetToolStripMenuItem.Name = "sobreOCalcNetToolStripMenuItem";
            this.sobreOCalcNetToolStripMenuItem.Size = new System.Drawing.Size(173, 22);
            this.sobreOCalcNetToolStripMenuItem.Text = "Sobre o CalcNet";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.ForeColor = System.Drawing.Color.Blue;
            this.label3.Location = new System.Drawing.Point(261, 28);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(19, 24);
            this.label3.TabIndex = 6;
            this.label3.Text = "*";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.ForeColor = System.Drawing.Color.Blue;
            this.label4.Location = new System.Drawing.Point(261, 28);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(19, 24);
            this.label4.TabIndex = 7;
            this.label4.Text = "*";
            // 
            // frmMain
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(821, 450);
            this.Controls.Add(this.statusStrip1);
            this.Controls.Add(this.menuStrip1);
            this.Controls.Add(this.tabControl1);
            this.Controls.Add(this.botaoExibirLogs);
            this.Controls.Add(this.botaoPararServidor);
            this.Controls.Add(this.botaoIniciarServidor);
            this.MainMenuStrip = this.menuStrip1;
            this.MaximizeBox = false;
            this.Name = "frmMain";
            this.Text = "CalcNet - Servidor";
            this.tabControl1.ResumeLayout(false);
            this.tabPage1.ResumeLayout(false);
            this.tabPage1.PerformLayout();
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.tabPage2.ResumeLayout(false);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel1.PerformLayout();
            this.tabPage3.ResumeLayout(false);
            this.tabPage4.ResumeLayout(false);
            this.statusStrip1.ResumeLayout(false);
            this.statusStrip1.PerformLayout();
            this.menuStrip1.ResumeLayout(false);
            this.menuStrip1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button botaoIniciarServidor;
        private System.Windows.Forms.Button botaoPararServidor;
        private System.Windows.Forms.Button botaoExibirLogs;
        private System.Windows.Forms.TabControl tabControl1;
        private System.Windows.Forms.TabPage tabPage1;
        private System.Windows.Forms.TabPage tabPage3;
        private System.Windows.Forms.StatusStrip statusStrip1;
        private System.Windows.Forms.ToolStripStatusLabel toolStripStatusLabel1;
        private System.Windows.Forms.MenuStrip menuStrip1;
        private System.Windows.Forms.ToolStripMenuItem arquivoToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem sairToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem ajudaToolStripMenuItem;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.ComboBox comboBox_tab1_porta;
        private System.Windows.Forms.TreeView treeView1;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.Label label_tab1_message2;
        private System.Windows.Forms.TextBox textBox_tab1_ip;
        private System.Windows.Forms.Label label_tab1_message1;
        private System.Windows.Forms.Label label10;
        private System.Windows.Forms.Label label9;
        private System.Windows.Forms.TabPage tabPage2;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.Label label_tab2_porta;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label11;
        private System.Windows.Forms.Label label_tab2_ip;
        private System.Windows.Forms.Label label_tab2_usuario;
        private System.Windows.Forms.Label label13;
        private System.Windows.Forms.Label label_tab2_os;
        private System.Windows.Forms.Label label16;
        private System.Windows.Forms.Label label15;
        private System.Windows.Forms.ToolStripMenuItem manualDoUsuárioToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem sobreOCalcNetToolStripMenuItem;
        private System.Windows.Forms.TabPage tabPage4;
        private System.Windows.Forms.RichTextBox richTextBox_output;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
    }
}

