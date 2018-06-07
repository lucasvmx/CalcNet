/*
    Logger.cs

    Possui métodos necessários para registrar logs em um arquivo em disco

    Autor: Lucas Vieira de Jesus
*/

using System;
using System.IO;
using AutoRevision;
using System.Text;
using System.Diagnostics;

namespace CalcNetServer
{
    class Logger
    {
        internal string logname = "";
        private string extensao = "log";   /* calcnet log */
        private Encoding systemEncoding;
        private string logs_dirname = "logs";

        public Logger()
        {
            /* Criar o arquivo de log */
            systemEncoding = Encoding.UTF8;
            if (!Directory.Exists(logs_dirname))
                Directory.CreateDirectory(logs_dirname);

            logname = $"{logs_dirname}\\calcnet_log_{DateTime.Now.Day}-{DateTime.Now.Month}-{DateTime.Now.Year}-{DateTime.Now.Hour}{DateTime.Now.Minute}{DateTime.Now.Second}.{extensao}";
            if(!File.Exists(logname))
                File.WriteAllText(logname, $"Arquivo de Log - {VersionInfo.VcsBasename} {VersionInfo.VcsTag} build {VersionInfo.VcsNum}\nCodificação: {systemEncoding.EncodingName}\n\n");
        }

        public void Write(string text)
        {
            string date = "";

            date = $"[{DateTime.Now.Day}/{DateTime.Now.Month}/{DateTime.Now.Year} {DateTime.Now.Hour}:" +
                $"{DateTime.Now.Minute}:{DateTime.Now.Second}] ";


            try
            {
                File.AppendAllText(logname, date + text, systemEncoding);
            } catch(Exception e)
            {
                Debug.WriteLine($"Failed to append text to file\n\n{e.StackTrace}");
            }
        }

        public string[] Read()
        {
            string[] lines = { "" };

            try
            {
                lines = File.ReadAllLines(logname);
            } catch(Exception e)
            {
                Debug.WriteLine($"Failed to read lines from {logname}\n\n{e.StackTrace}");
            }

            return lines;
        }
    }
}
