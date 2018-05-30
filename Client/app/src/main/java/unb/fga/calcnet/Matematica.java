package unb.fga.calcnet;

public final class Matematica
{
    public static long fatorial(long n)
    {
        long result = 1;

        if(n == 0)
            return 1;

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

    public static double logaritmo(double n) throws Exception
    {
        if(n == (double)0)
            throw new Exception("O logaritmo de zero não é definido");

        return(Math.log(n));
    }

    public static double ln(double n) throws Exception
    {
        if((n - 1) <= 0)
            throw new Exception("O ln de 0 e de números negativos não existem");

        return(Math.log1p(n - 1));
    }
}
