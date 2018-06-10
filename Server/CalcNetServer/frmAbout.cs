using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Windows.Forms;
using AutoRevision;
using MetroFramework.Forms;
using System.Resources;
using System.Reflection;
using System.IO;

namespace CalcNetServer
{
    public partial class frmAbout : MetroForm
    {
        public frmAbout()
        {
            string about_rtf_res = "";
            string filename = "about.rtf";
            InitializeComponent();

            Assembly assembly = Assembly.GetCallingAssembly();
            string[] resources = assembly.GetManifestResourceNames();
            foreach(string s in resources)
            {
                if(s.Contains(filename))
                {
                    about_rtf_res = s;
                    break;
                }
            }

            FileStream file = File.Create(filename);

            Stream stream = assembly.GetManifestResourceStream(about_rtf_res);

            if (stream != null)
            {
                byte[] data = new byte[1024];
                while (stream.Read(data, 0, 1024) > 0)
                {
                    file.Write(data, 0, 1024);
                }
                stream.Close();
                file.Close();

                string rtf = File.ReadAllText(filename);
                rtf = rtf.Replace("$calcnet_version$", VersionInfo.VcsTag);
                rtf = rtf.Replace("$calcnet_build_number$", VersionInfo.VcsNum);
                rtf = rtf.Replace("$calcnet_version_id$", VersionInfo.VcsFullHash);
                rtf = rtf.Replace("$calcnet_build_date$", VersionInfo.VcsDate);
                richTextBox1.Rtf = rtf;
                
                if (File.Exists(filename))
                    File.Delete(filename);
            }
        }

        private void richTextBox1_LinkClicked(object sender, LinkClickedEventArgs e)
        {
            Process process = new Process();
            ProcessStartInfo psi = new ProcessStartInfo(e.LinkText);
            psi.UseShellExecute = true;
            process.StartInfo = psi;
            process.Start();
        }
    }
}
