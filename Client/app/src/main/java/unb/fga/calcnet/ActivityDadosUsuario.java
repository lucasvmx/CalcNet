package unb.fga.calcnet;

import android.graphics.Color;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ActivityDadosUsuario extends AppCompatActivity
{
    public String nome = "";
    public String ip = "";
    public int porta = 1701;    /* Porta padrão de conexão */
    public InetAddress ipServidor = null;

    private int CorOriginalBackground = Color.WHITE;
    private int CorOriginalForeground = Color.BLACK;

    private EditText txNome;
    private EditText txIp;
    private EditText txPorta;
    private TextView txError;
    private Thread connectThread = null;
    private static volatile boolean isConnected;

    Runnable run = new Runnable() {
        @Override
        public void run()
        {
            Socket s;

            isConnected = false;
            do {
                s = connectToServer();
            } while(s == null);

            isConnected = true;
            Log.d("[THREAD]", "Fim da thread de conexão");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_usuario);
        txNome = findViewById(R.id.editTextNome);
        txIp = findViewById(R.id.editTextIp);
        txPorta = findViewById(R.id.editTextPorta);
        txError = findViewById(R.id.textViewError);

        connectThread = new Thread(run);
    }

    public Socket connectToServer()
    {
        Socket sock = null;

        try {
            sock = new Socket(ip, porta);
            SocketAddress addr = sock.getRemoteSocketAddress();
            sock.setSoTimeout(1000);
            sock.connect(addr);
        } catch (Exception e) {
            Log.e("[ERROR] ", e.getMessage());
            wait(1000);
        } finally {
            return sock;
        }
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
            error.setMessage("Falha ao guardar dados inseridos. Tente novamente");
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
            if(porta <= 0 || porta > 65535)
            {
                txError.setTextColor(Color.RED);
                txError.setText("Porta de conexão inválida");
                return;
            }

            Intent intent = new Intent(this,MainActivity.class);
            Bundle b = new Bundle();

            intent.putExtras(b);

            if(!connectThread.isAlive())
                connectThread.start();

            if(!isConnected)
            {
                int mWifiState = Rede.wifiLigado(getApplicationContext());

                if(mWifiState == WifiManager.WIFI_STATE_DISABLED || mWifiState == WifiManager.WIFI_STATE_DISABLING)
                    txError.setText("Ative o WiFi");
                else
                    txError.setText("Você não está conectado ao servidor");

                /* FIXME: o código de rede não está funcionando corretamente */

                txError.setTextColor(Color.BLUE);
                return;
            } else {
                connectThread.interrupt();
            }

            this.startActivity(intent);
            this.finish();
        }
    }
}
