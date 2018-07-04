/*
    Rede.java
    Autor: Lucas Vieira de Jesus

    Este arquivo contém os métodos relacionados com as operacoes em rede
 */

package unb.fga.calcnet;

import android.Manifest.permission;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.system.ErrnoException;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.charset.Charset;

public class Rede extends AsyncTask<String, Void, Boolean>
{
    public static int MODO_AVIAO_ON = 1;
    public static int MODO_AVIAO_OFF = 0;
    public static Socket netSocket = null;
    public static volatile boolean isConnected = false; /* Flag para armazenar se a calculadora está conectada ao servidor */
    public static Thread netThread;
    public static Thread statusThread;
    public static boolean stopThread = false;
    public static boolean ban = false;
    public static Context ctx;
    Common cm;

    @Override
    protected Boolean doInBackground(String... args)
    {
        RClient.run();
        RClientIsConnected.run();

        return true;
    }

    @Override
    protected void onCancelled(Boolean result)
    {
        try {
            super.finalize();
        } catch (Throwable throwable) {
            Log.e("[ERRO]", "Falha ao finalizar thread: " + throwable.getMessage());
        }
    }

    public static Runnable RClientIsConnected = new Runnable() {
        @Override
        public void run()
        {
            for(;;)
            {
                synchronized (this)
                {
                    try {
                        this.wait(3000);
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

    public static Runnable RClient = new Runnable()
    {
        @Override
        public void run()
        {
            boolean keep_alive = false;
            Rede k = new Rede();
            int tentativas = 0;
            boolean stop = false;
            isConnected = false;

            if(ban)
            {
                try {
                    Log.e("[BANIDO]", "Você está banido do servidor");
                    super.finalize();
                } catch(Throwable t)
                {
                    Log.e("[ERRO]", "Falha ao finalizar thread de conexão");
                }
            } else {
                while (!stopThread && !ban)
                {
                    while (connectToServer(ActivityDadosUsuario.ip, ActivityDadosUsuario.porta) == null) {
                        try {
                            synchronized (this) {
                                this.wait(2000);
                            }
                            tentativas++;
                            if (tentativas > 4) {
                                Log.i("[INFO]", tentativas + " tentativas sem sucesso foram realizadas.");
                                stop = true;
                                break;
                            }

                        } catch (InterruptedException ie) {
                            Log.e("[ERRO]", "Erro ao aguardar: " + ie.getMessage());
                        }
                    }

                    if (stop)
                        break;

                    Log.i("[INFO]", "Estamos conectados com: " + netSocket.getRemoteSocketAddress().toString());
                    Log.i("[NUM-SERIAL] ", getSerial(ctx));

                    try {
                        keep_alive = netSocket.getKeepAlive();
                    } catch (Exception error) {
                        Log.e("[ERRO]", "Falha ao pegar flag de keep-alive: " + error.getMessage());
                    }

                    try {
                        netSocket.setKeepAlive(true);
                    } catch (SocketException SE) {
                        Log.e("[ERRO]", "Não foi possível mudar flag de keep-alive: " + SE.getMessage());
                    }

                    Log.i("[INFO]", "Keep-alive: " + keep_alive);

                    while (netSocket.isConnected()) {
                        isConnected = netSocket.isConnected();
                        int kmu = k.monitorarUsuario(netSocket);

                        if (kmu == 7) {
                            /* Você levou ban do servidor */
                            Log.e("[BAN]", "Você foi banido do servidor temporariamente");
                            ban = true;
                            break;
                        }

                        if (kmu != 0)
                            Log.e("[ERRO]", "monitorarUsuario retornou " + kmu);
                    }

                    isConnected = false;
                    Log.i("[INFO]", "Você foi desconectado do servidor");
                    tentativas++;
                }

                try {
                    super.finalize();
                    Log.i("[INFO]", "Thread de conexão finalizada");
                } catch (Throwable error) {
                    Log.e("[ERRO]", "Falha ao finalizar thread: " + error.getMessage());
                }
            }
        }
    };

    /* Retorna 1 se o modo avião estiver ligado */
    public static int modoAviaoLigado(ContentResolver cr)
    {
        int settings;
        String config;

        if(cr == null)
            return 2;

        if(Build.VERSION.SDK_INT > 17)
            config = Settings.Global.AIRPLANE_MODE_ON;
        else
            config = Settings.System.AIRPLANE_MODE_ON;

        Log.i("[AIRPLANE-MODE]", "config is: " + config);
        if(Build.VERSION.SDK_INT > 17)
            settings = Settings.Global.getInt(cr,config,0);
        else
            settings = Settings.System.getInt(cr,config,0);

        return(settings);
    }

    /* Retorna true se o bluetooth estiver ligado */
    public static boolean bluetoothLigado()
    {
        BluetoothAdapter adapter = null;

        try {
            adapter = BluetoothAdapter.getDefaultAdapter();
        } catch(Exception e)
        {
            Log.e("[ERROR-BLUETOTH]", e.getMessage());
        }

        if(adapter == null)
            return false;

        return(adapter.isEnabled());
    }

    public static int wifiLigado(Context ctx)
    {
        ContentResolver cr = ctx.getContentResolver();
        int mState;
        String config = "";

        if(cr == null)
            return 2;

        if(Build.VERSION.SDK_INT > 17) {
            config = Settings.Global.WIFI_ON;
            mState = Settings.Global.getInt(cr, config, 0);
        }
        else {
            config = Settings.System.WIFI_ON;
            mState = Settings.System.getInt(cr, config, 0);
        }

        Log.v("[wifiLigado]", "int:" + mState);

        return mState;
    }

    /* FIXME: Adicionar código para verificar o status do wifi antes da API 23 */

    public static String getSerial(Context ctx)
    {
        String serial = "-1";

        try {
            if(Build.VERSION.SDK_INT >= 26)
            {
                if(ctx.checkSelfPermission(permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    serial = Build.getSerial();
                } else {
                    Log.e("[ERRO]", "getSerial: failed to get device serial number");
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

    private static Socket connectToServer(String ip, int porta)
    {
        try
        {
            netSocket = new Socket(ip, porta);
            isConnected = netSocket.isConnected();
            if(netSocket.isConnected())
                return netSocket;

            //netSocket.connect(addr, 60);
        } catch (Exception IE) {
            Log.e("[ERRO]", "Falha ao conectar: " + IE.getMessage());
        }

        return null;
    }

    private int monitorarUsuario(Socket socket)
    {
        String sJson;
        JSONObject jsonObject;
        InputStream is;
        OutputStream os;

        if(!socket.isConnected())
        {
            Log.e("[DESCONECTADO]", "Você foi desconectado");
            return 0;
        }

        sJson = "{\"nome\":\"" + ActivityDadosUsuario.nome + "\",";
        sJson += "\"serial\":\"" + getSerial(ctx) + "\",";
        sJson += "\"ip\":\"" + ActivityDadosUsuario.ip + "\",";
        sJson += "\"bluetooth\":" + ((bluetoothLigado()) ? 1 : 0) + ",";
        sJson += "\"modo_aviao\":" + modoAviaoLigado(ctx.getContentResolver()) + ",";
        sJson += "\"saiu\":" + (MainActivity.MainActivityStopped ? 1:0) + "}";

        Log.i("[STRING-JSON]", sJson);

        try {
            jsonObject = new JSONObject(sJson);

            Log.d("[DEBUG]", "Objeto JSON criado com sucesso: " + jsonObject.toString());
        } catch (JSONException e) {
            Log.e("[ERRO-JSON]", e.getMessage());
            return 2;
        }

        try
        {
            os = socket.getOutputStream();
            is = socket.getInputStream();

            /* Verificar se ainda temos permissão para permanecer conectados */
            if(is.available() > 0)
            {
                byte[] server_data = new byte[512];

                try {
                    is.read(server_data,0,server_data.length);

                    Log.e("[SERVER-PAYLOAD]", server_data.toString() + "==> size: " + server_data.length);
                    return 7;
                } catch(Exception e)
                {
                    Log.e("[SERVER-ERROR]", e.getMessage());
                }
            }

            byte data[] = jsonObject.toString().getBytes();
            os.flush();
            os.write(data, 0, data.length);

        } catch (IOException e) {
            Log.e("[ERRO]", e.getMessage());
            try {
                socket.close();
            } catch (IOException e1) {
                Log.e("[ERRO]", e1.getMessage());
                return 3;
            }
            return 4;
        } catch(NullPointerException npe)
        {
            Log.e("[ERRO]", npe.getMessage());
            return 5;
        }

        synchronized (this)
        {
            try {
                wait(1000);
            } catch (InterruptedException ie) {
                Log.e("[ERRO]", "Erro ao aguardar com wait: " + ie.getMessage());
                return 6;
            }
        }

        return 0;
    }
}
