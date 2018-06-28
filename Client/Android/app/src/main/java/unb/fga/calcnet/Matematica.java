package unb.fga.calcnet;

import android.app.Activity;

public final class Matematica
{
    public static final double PI = Math.PI;
    public static final double NUMERO_EULER = Math.E;

    public static class Expressao
    {
        private String mathExpr;
        private Activity am;

        public Expressao()
        {
            mathExpr = "";
        }

        public Expressao(String expr, Activity activity)
        {
            mathExpr = expr;
            am = activity;
        }

        public String corrigir()
        {
            /* Corrigir a expressão matemática */
            mathExpr = mathExpr.replace(am.getString(R.string.multiplicacao),"*");
            mathExpr = mathExpr.replace(am.getString(R.string.divisao),"/");
            mathExpr = mathExpr.replace(am.getString(R.string.pi), String.valueOf(Matematica.PI));
            mathExpr = mathExpr.replace(am.getString(R.string.numero_de_euler),String.valueOf(Matematica.NUMERO_EULER));

            return mathExpr;
        }
    };

    public static long fatorial(long n)
    {
        long result = 1;

        if(n == 0)
            return 1;

        if(n < 0)
            n *= -1;

        for(int k = 1; k <= n; k++)
            result *= k;

        return result;
    }

    public static double fatorial(double x)
    {
        /* método usando a série de stirling */
        double raiz = Math.sqrt(2*Math.PI*x);
        double potencia = Math.pow(x/Math.E,x);
        double soma = 1 + (1/(12.0 * x)) + (1/(288 * Math.pow(x,2.0))) +
                (1/(51840 * Math.pow(x,3.0))) - (571.0/(2488320 * Math.pow(x,4.0))) + (163879/(209018880.0 * Math.pow(x,5.0)));

        return raiz * potencia * soma;
    }

    public static double exponencial(double x)
    {
        return Math.exp(x);
    }

    public static double seno(double angulo_radianos)
    {
        return(Math.sin(angulo_radianos));
    }

    public static double cosseno(double angulo_radianos)
    {
        return(Math.cos(angulo_radianos));
    }

    public static double tangente(double angulo_radianos)
    {
        return(Math.tan(angulo_radianos));
    }


    public static double arcoseno(double angulo_radianos)
    {
        return(Math.asin((angulo_radianos)));
    }

    public static double arcocoseno(double angulo_radianos)
    {
        return(Math.acos(angulo_radianos));
    }

    public static double arcotangente(double angulo_radianos)
    {
        return(Math.atan(angulo_radianos));
    }

    public static double raiz_quadrada(double n)
    {
        return(Math.sqrt(n));
    }

    public static double raiz_cubica(double n)
    {
        return(Math.cbrt(n));
    }

    public static double raiz_n(double n, int indice) throws Exception
    {
        if(indice <= 0)
            throw new Exception("Raíz com índice inválido");

        return(Math.pow(n,(1/indice)));
    }

    public static double logaritmo10(double n) throws Exception
    {
        if(n <= (double)0)
            throw new Exception("O logaritmo só é definido no domínio dos reais positivos");

        return(Math.log(n) / Math.log(10.0));
    }

    public static double ln(double n) throws Exception
    {
        if((n - 1) <= 0)
            throw new Exception("O ln só é definido no domínio dos reais positivos");

        return(Math.log(n));
    }
}
