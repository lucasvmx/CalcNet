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
        internal string bug_filename = "";
        private string extensao = "log";   /* calcnet log */
        private Encoding systemEncoding;
        private string logs_dirname = "logs";
        private string bugs_dirname = "bugs";

        public Logger(bool stack_trace)
        {
            if (!stack_trace)
            {
                /* Criar o arquivo de log */
                systemEncoding = Encoding.UTF8;
                if (!Directory.Exists(logs_dirname))
                    Directory.CreateDirectory(logs_dirname);

                logname = $"{logs_dirname}\\calcnetLog-{DateTime.Now.Day.ToString("00")}-{DateTime.Now.Month.ToString("00")}-{DateTime.Now.Year}.{extensao}";
                if (!File.Exists(logname))
                {
                    File.WriteAllText(logname, $"Arquivo de Log - {VersionInfo.VcsBasename} {VersionInfo.VcsTag} build {VersionInfo.VcsNum}\nCodificação: {systemEncoding.EncodingName}\n");
                    File.AppendAllText(logname, $"Hora de início: {DateTime.Now.Hour.ToString("00")}:{DateTime.Now.Minute.ToString("00")}:{DateTime.Now.Second.ToString("00")}\n\n");
                }
            } else
            {
                systemEncoding = Encoding.UTF8;
                if (!Directory.Exists(bugs_dirname))
                    Directory.CreateDirectory(bugs_dirname);

                bug_filename = $"{bugs_dirname}\\calcnet-bug-{DateTime.Now.Day.ToString("00")}-{DateTime.Now.Month.ToString("00")}-{DateTime.Now.Year}.{extensao}";
                if (!File.Exists(bug_filename))
                {
                    File.WriteAllText(bug_filename, $"Relatório de bug do CalcNet - {VersionInfo.VcsBasename} {VersionInfo.VcsTag} build {VersionInfo.VcsNum}\nCodificação: {systemEncoding.EncodingName}\n");
                    File.AppendAllText(bug_filename, $"Hora de início: {DateTime.Now.Hour.ToString("00")}:{DateTime.Now.Minute.ToString("00")}:{DateTime.Now.Second.ToString("00")}\n\n");
                }
            }
        }

        public void Write(string text)
        {
            string date = "";

            date = $"[{DateTime.Now.Day.ToString("00")}/{DateTime.Now.Month.ToString("00")}/{DateTime.Now.Year} {DateTime.Now.Hour.ToString("00")}:" +
                $"{DateTime.Now.Minute.ToString("00")}:{DateTime.Now.Second.ToString("00")}] ";

            try
            {
                File.AppendAllText(logname, date + text, systemEncoding);
            } catch(Exception e)
            {
                Debug.WriteLine($"Failed to append text to file\n\n{e.StackTrace}");
            }
        }

        public void WriteStackTrace(Exception e)
        {
            string date = "";

            date = $"[{DateTime.Now.Day}/{DateTime.Now.Month}/{DateTime.Now.Year} {DateTime.Now.Hour}:" +
                $"{DateTime.Now.Minute.ToString("00")}:{DateTime.Now.Second.ToString("00")}] ";

            File.AppendAllText(bug_filename, date + e.StackTrace, systemEncoding);
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
