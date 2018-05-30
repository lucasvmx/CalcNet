/*
    Rede.java
    Autor: Lucas Vieira de Jesus

    Este arquivo contém os métodos relacionados com as operacoes em rede
 */

package unb.fga.calcnet;

import android.annotation.TargetApi;
import android.app.ApplicationErrorReport;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.os.*;
import android.app.*;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import junit.runner.Version;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class Rede extends AppCompatActivity
{
    public static int MODO_AVIAO_ON = 1;
    public static int MODO_AVIAO_OFF = 0;
    public static Socket netSocket = null;
    public static volatile boolean isConnected = false; /* Flag para armazenar se a calculadora está conectada ao servidor */
    public static Thread connThread;

    public static Runnable networkThread = new Runnable()
    {
        @Override
        public void run()
        {
            do
            {
                connectToServer(ActivityDadosUsuario.ip, ActivityDadosUsuario.porta);
            } while(isConnected == false);
            Log.d("[networkThread]", "isConnected: " + isConnected);
        }
    };

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

    public static Socket connectToServer(String ip, int porta)
    {
        if(netSocket == null)
        {
            try {
                netSocket = new Socket(ip, porta);
                SocketAddress addr = netSocket.getRemoteSocketAddress();
                netSocket.connect(addr, 60);
            } catch (IOException IE) {
                Log.e("[ERROR-IE]", "Cannot connect to host: " + IE.getMessage());
            }
        } else {
            if(netSocket.isConnected())
            {
                Log.i("[SUCCESS]", "Connected to server");
                isConnected = true;
            }
        }

        return netSocket;
    }
}
