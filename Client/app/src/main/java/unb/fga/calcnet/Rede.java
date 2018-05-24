/*
    Rede.java
    Autor: Lucas Vieira de Jesus

    Este arquivo contém os métodos relacionados com as operacoes em rede
 */

package unb.fga.calcnet;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import junit.runner.Version;

public class Rede extends AppCompatActivity
{
    public static int MODO_AVIAO_ON = 1;
    public static int MODO_AVIAO_OFF = 0;

    /* Retorna true se houve conexão bluetooth ou dados móveis */
    public static int modoAviaoLigado(ContentResolver cr)
    {
        if(cr == null)
            return 2;

        ContentResolver resolver = cr;
        String config = Settings.Global.AIRPLANE_MODE_ON;

        int mIsOn = Settings.Global.getInt(resolver,config,0);

        return mIsOn;
    }

    /* Retorna true se o bluetooth estiver ligado */
    public static boolean bluetoothLigado()
    {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null)
            return false;

        return(adapter.isEnabled());
    }

    @TargetApi(23)
    public static int wifiLigado(Context ctx)
    {
        ContentResolver cr = ctx.getContentResolver();

        if(cr == null)
            return 2;

        ContentResolver resolver = cr;
        String config = Settings.Global.WIFI_ON;

        int mState = Settings.Global.getInt(resolver,config,0);
        Log.v("[wifiLigado]", "int:" + mState);

        return mState;
    }

    /* FIXME: Adicionar código para verificar o status do wifi antes da API 23 */
}
