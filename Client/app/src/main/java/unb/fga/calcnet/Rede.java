/*
    Rede.java
    Autor: Lucas Vieira de Jesus

    Este arquivo contém os métodos relacionados com as operacoes em rede
 */

package unb.fga.calcnet;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.TargetApi;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.net.wifi.WifiInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

public class Rede extends AsyncTask<String, Void, Boolean>
{
    public static int MODO_AVIAO_ON = 1;
    public static int MODO_AVIAO_OFF = 0;
    public static Socket netSocket = null;
    public static volatile boolean isConnected = false; /* Flag para armazenar se a calculadora está conectada ao servidor */
    public static Thread netThread;
    public static Thread statusThread;
    public static boolean stopThread = false;
    public static Context ctx;

    @Override
    protected Boolean doInBackground(String... args)
    {
        RConnnect.run();
        return true;
    }

    public static Runnable RConnected = new Runnable() {
        @Override
        public void run()
        {
            for(;;)
            {
                synchronized (this)
                {
                    try {
                        this.wait(2000);
                    } catch(InterruptedException ie)
                    {
                        Log.e("[ERROR]", ie.getMessage());
                    }
                }

                if (!isConnected)
                    ActivityDadosUsuario.status = "Conectando-se ...";
            }
        }
    };

    public static Runnable RConnnect = new Runnable() {
        @Override
        public void run()
        {
            String sJson = "";
            JSONObject jsonObject = null;

            while(connectToServer(ActivityDadosUsuario.ip, ActivityDadosUsuario.porta) == null)
            {
                try {
                    synchronized (this) {
                        this.wait(4000);
                    }
                } catch(InterruptedException ie)
                {
                    Log.e("[ERROR]", "wait: " + ie.getMessage());
                }
            }

            Log.i("[INFO]", "Estamos conectados");
            Log.i("[SERIAL] ", getSerial(ctx));

            try {
                netSocket.setKeepAlive(true);
            } catch(SocketException SE)
            {
                SE.printStackTrace();
            }

            sJson = "{\"nome\":\"" + ActivityDadosUsuario.nome + "\",";
            sJson += "\"serial\":\"" + getSerial(ctx) + "\",";
            sJson += "\"ip\":\"" + ActivityDadosUsuario.ip + "\",";
            sJson += "\"bluetooth\":" + ((bluetoothLigado()) ? 1:0) + ",";
            sJson += "\"modo_aviao\":" + wifiLigado(ctx) + "}";

            Log.d("[JSON]", sJson);

            try {
                new JSONObject(sJson);

                Log.d("[DEBUG]", "Objeto JSON criado com sucesso");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                InputStream is = netSocket.getInputStream();
                OutputStream os = netSocket.getOutputStream();

                byte data[] = new String("lucas").getBytes();
                os.write(data,0,data.length);
                Log.d("[WRITE]", "Json enviado ao servidor");
            } catch (IOException e)
            {
                e.printStackTrace();
                try {
                    netSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            sJson = "sss";
        }
    };

    /* Retorna 1 se o modo avião estiver ligado */
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

    public static String getSerial(Context ctx)
    {
        String serial = "-1";

        try {
            if(Build.VERSION.SDK_INT == 26) {
                if(ctx.checkSelfPermission(permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    serial = Build.getSerial();
                }
            }
            else {
                serial = Build.SERIAL;
            }
        } catch(Exception x)
        {
            Log.e("[ERROR]", x.getMessage());
        }

        return serial;
    }

    public static Socket connectToServer(String ip, int porta)
    {
        try
        {
            Log.i("[INFO]", "Criando socket");
            netSocket = new Socket(ip, porta);
            Log.i("[INFO]", "Socket criado");

            isConnected = netSocket.isConnected();

            Log.i("[INFO]", "Isconnected: " + isConnected);

            if(netSocket.isConnected())
                return netSocket;

            //SocketAddress addr = netSocket.getRemoteSocketAddress();
            //netSocket.connect(addr, 60);
        } catch (Exception IE) {
            Log.e("[ERROR-IE]", "Cannot connect to host: " + IE.getMessage());
            IE.printStackTrace();
        }

        return null;
    }
}
