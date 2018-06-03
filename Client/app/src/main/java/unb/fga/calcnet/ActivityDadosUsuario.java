package unb.fga.calcnet;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_usuario);
        txNome = findViewById(R.id.editTextNome);
        txIp = findViewById(R.id.editTextIp);
        txPorta = findViewById(R.id.editTextPorta);
        txError = findViewById(R.id.textViewError);

        txPorta.setText("1701");
        txIp.setText("192.168.0.3");
        txNome.setText("lucas");

        Rede.ctx = this.getApplicationContext();
        Rede.netThread = new Thread(Rede.RConnnect);
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
            AlertDialog.Builder error = new AlertDialog.Builder(this);
            error.setTitle("Erro Crítico");
            error.setMessage("Os dados foram inseridos? Se sim, estão corretos? Tente novamente");
            error.show();
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
            try {
                if(Rede.netThread.getState() == Thread.State.NEW)
                    Rede.execute(Rede.netThread);
                else
                    Log.i("[INFO]", "Network thread already running");
            } catch(Exception e)
            {
                e.printStackTrace();
            }

            if(porta <= 0 || porta > 65535)
            {
                txError.setTextColor(Color.RED);
                txError.setText("Porta de conexão inválida");
                return;
            }

            if(!Rede.isConnected)
            {
                int mWifiState = Rede.wifiLigado(getApplicationContext());

                if(mWifiState == WifiManager.WIFI_STATE_DISABLED || mWifiState == WifiManager.WIFI_STATE_DISABLING) {
                    txError.setText("Ative o WiFi");
                } else
                    txError.setText("Não foi possível se conectar ao servidor. Tente novamente");

                int colors[] = { Color.RED, Color.CYAN, Color.WHITE};
                int random = (int)(Math.round(Math.random())) % colors.length;

                txError.setTextColor(colors[random]);
                return;
            } else
            {
                try {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext(), R.style.Theme_AppCompat);
                    dialog.setMessage("Você agora está conectado ao servidor. Aperte em OK ou aguarde alguns segundos ...");
                    dialog.setTitle("Informação");
                    dialog.setPositiveButton("OK", null);
                    dialog.show();
                } catch(Exception s)
                {
                    Log.e("[ERROR]", "Failed to display dialog: " + s.getMessage());
                }
            }

            Intent intent = new Intent(this,MainActivity.class);
            MainActivity.mainSocket = Rede.netSocket;
            startActivity(intent);
            finish();
        }
    }
}
