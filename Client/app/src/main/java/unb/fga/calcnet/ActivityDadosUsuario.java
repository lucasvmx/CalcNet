package unb.fga.calcnet;

import android.accounts.NetworkErrorException;
import android.graphics.Color;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.system.ErrnoException;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import android.system.OsConstants;
import android.widget.Button;

public class ActivityDadosUsuario extends AppCompatActivity
{
    public static String nome = "";
    public static String ip = "";
    public static int porta = 1701;    /* Porta padrão de conexão */
    public InetAddress ipServidor = null;

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

        Rede.connThread = new Thread(Rede.networkThread);
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

        if(v.getId() == R.id.botaoOK)
        {
            Intent intent = new Intent(this,MainActivity.class);
            Bundle b = new Bundle();

            intent.putExtras(b);

            this.startActivity(intent);
            this.finish();
            return;

            /*
            Button button = (Button)findViewById(R.id.botaoOK);
            if(!Rede.connThread.isAlive())
                Rede.connThread.start();

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

                txError.setTextColor(Color.BLUE);
                return;
            }

            Rede.connThread.interrupt();
            */
        }
    }
}
