/*
    User.cs

    Possui atributos relacionados ao usuário

    Autor: Lucas Vieira de Jesus
*/

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CalcNetServer
{
    public class User
    {
        public string nome;
        public string serial;
        public string ip;
        public int bluetooth;  /* 0 = desligado, 1 = ligado */
        public int modo_aviao; /* 0 = desligado, 1 = ligado */
        public bool saiu;

        public User()
        {
            nome = "";
            serial = "";
            ip = "";
            bluetooth = -1;
            modo_aviao = -1;
        }
    }
}
