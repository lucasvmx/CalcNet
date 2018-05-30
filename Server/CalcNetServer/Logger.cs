/*
    Logger.cs

    Possui métodos necessários para registrar logs em um arquivo em disco

    Autor: Lucas Vieira de Jesus
*/

using System;
using System.IO;
using AutoRevision;
using System.Text;

namespace CalcNetServer
{
    class Logger
    {
        internal string logname = "";
        private string extensao = "cnl";   /* calcnet log */
        private Encoding systemEncoding;

        public Logger()
        {
            /* Criar o arquivo de log */
            systemEncoding = Encoding.GetEncoding(0);
            logname = $"calcnet_log_{DateTime.Now.Day}{DateTime.Now.Year}{DateTime.Now.Month}{DateTime.Now.Hour}{DateTime.Now.Minute}{DateTime.Now.Second}.{extensao}";
            if(!File.Exists(logname))
                File.WriteAllText(logname, $"Arquivo de Log - {VersionInfo.VcsBasename} {VersionInfo.VcsTag} build {VersionInfo.VcsNum}\nCodificação do sistema: {systemEncoding.EncodingName}\n\n");
        }

        public void Write(string text)
        {
            string date = "";

            date = $"[{DateTime.Now.Day}/{DateTime.Now.Month}/{DateTime.Now.Year} {DateTime.Now.Hour}:" +
                $"{DateTime.Now.Minute}:{DateTime.Now.Second}] ";

            
            File.AppendAllText(logname, date + text, systemEncoding);
        }

        public string[] Read()
        {
            return File.ReadAllLines(logname);
        }
    }
}
