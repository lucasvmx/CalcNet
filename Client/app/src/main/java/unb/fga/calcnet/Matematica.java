package unb.fga.calcnet;

import android.app.Activity;

public final class Matematica
{
    public static final double PI = 3.1415926536;
    public static final double NUMERO_EULER = 2.7182818284;

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
            mathExpr = mathExpr.replace(am.getString(R.string.seno), "sin");

            return mathExpr;
        }

        private int countCloseParenthesis()
        {
            int count = 0;

            for(char c : mathExpr.toCharArray())
            {
                if(c == ')') {
                    count++;
                }
            }

            return count;
        }

        private int countOpenParenthesis()
        {
            int count = 0;

            for(char c : mathExpr.toCharArray())
            {
                if(c == '(') {
                    count++;
                }
            }

            return count;
        }

        public boolean isValid()
        {
            boolean condition = false;

            condition = countOpenParenthesis() == countCloseParenthesis();

            return condition;
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

    public static double fatorial(double n)
    {
        /* TODO: ver função gama */

        return 0;
    }

    public static double exponencial(double x)
    {
        return Math.exp(x);
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
