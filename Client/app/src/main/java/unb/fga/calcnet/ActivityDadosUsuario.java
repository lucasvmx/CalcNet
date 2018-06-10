package unb.fga.calcnet;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telecom.RemoteConnection;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import java.net.InetAddress;
import android.widget.Button;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Random;

public class ActivityDadosUsuario extends Activity
{
    public static String nome = "";
    public static String ip = "";
    public static int porta = 1701;    /* Porta padrão de conexão */
    public InetAddress ipServidor = null;
    public static String status = "";

    private int CorOriginalBackground = Color.WHITE;
    private int CorOriginalForeground = Color.BLACK;

    private EditText txNome;
    private EditText txIp;
    private EditText txPorta;
    private TextView txError;
    private Button botaoConectar;
    private String botaoConectarTitle;
    private Common common;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_usuario);
        txNome = findViewById(R.id.editTextNome);
        txIp = findViewById(R.id.editTextIp);
        txPorta = findViewById(R.id.editTextPorta);
        txError = findViewById(R.id.textViewError);
        botaoConectar = findViewById(R.id.botaoConectar);
        botaoConectarTitle = botaoConectar.getText().toString();

        txPorta.setText("1701");
        txIp.setText("192.168.0.3");
        txNome.setText("teste");

        Rede.ctx = this.getApplicationContext();
        Rede.netThread = new Thread(Rede.RClient);
        common = new Common(this,this);
    }

    public void OnClick(View v)
    {
        txError.setTextColor(Color.BLACK);
        txError.setText("");

        /* Salvar os dados do usuário */
        try
        {
            nome = txNome.getText().toString();
            ip = txIp.getText().toString();
            porta = Integer.parseInt(txPorta.getText().toString());
        } catch(Exception e)
        {
            common.showMessage("Verifique se os dados foram inseridos corretamente", Toast.LENGTH_LONG);
            return;
        }

        try {
            ipServidor = InetAddress.getByName(ip);
        } catch(Exception e)
        {
            txError.setTextColor(Color.RED);
            txError.setText("Endereço de IP inválido");
            return;
        }

        if(v.getId() == R.id.botaoConectar)
        {
            botaoConectar.setText("CONECTANDO...");
            try {
                if(Rede.netThread.getState() == Thread.State.NEW)
                    Rede.execute(Rede.netThread);
                else
                    Log.i("[INFO]", "Network thread already running");
            } catch(Exception e)
            {
                Log.e("[ERRO]", e.getMessage());
                botaoConectar.setText(botaoConectarTitle);
                return;
            }

            if(porta <= 0 || porta > 65535)
            {
                txError.setTextColor(Color.RED);
                txError.setText("Porta de conexão inválida");
                botaoConectar.setText(botaoConectarTitle);
                return;
            }

            /* Aguarda 1 segundo para verificar se estamos conectados */
            synchronized (this)
            {
                try {
                    wait(1000);
                } catch(InterruptedException ie)
                {
                    Log.e("[ERROR]", ie.getMessage());
                }
            }

            if(!Rede.isConnected)
            {
                int mWifiState = Rede.wifiLigado(getApplicationContext());

                if(Rede.modoAviaoLigado(getContentResolver()) == Rede.MODO_AVIAO_OFF) {
                    common.showMessage("Cuidado", "Conectar-se com o modo avião desligado, fará o CalcNet entender que você está utilizando o software incorretamente.");
                } else {
                    if (mWifiState == WifiManager.WIFI_STATE_DISABLED)
                    {
                        common.showMessage("Erro", "O wifi está desligado");
                    } else
                    {
                        common.showMessage("Não foi possível conectar-se ao servidor. Tente novamente",Toast.LENGTH_LONG);
                    }
                }

                botaoConectar.setText(botaoConectarTitle);
                return;
            }

            Intent intent = new Intent(this,MainActivity.class);
            MainActivity.mainSocket = Rede.netSocket;
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        }
    }
}
