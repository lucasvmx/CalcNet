/*
    MainActivity.java
    Autor: Lucas Vieira de Jesus

    Este arquivo contém os métodos relacionados com as operacoes na activity MainActivity
 */

package unb.fga.calcnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import java.net.Socket;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import android.support.v7.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener
{
    public static Socket mainSocket = null;
    private EditText mathText;
    private EditText resultText;
    private LinearLayout linearLayout;
    private GridLayout gridLayout;
    private TextView tv;
    private boolean radianos;
    private Button botaoSwitch;
    private DoubleEvaluator dv;
    private Common common;
    public static volatile boolean MainActivityStopped = false;
    private Handler handler;
    private SensorManager sensor_manager;
    private Sensor sensorProximidade;
    private boolean flag;
    private TextView tvStatus;

    // FIXME: Corrigir funções trigonométricas inversas
    // FIXME: Thread continua rodando mesmo após a activity sair

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        dv = new DoubleEvaluator();
        common = new Common(this,this);

        mathText = findViewById(R.id.editText_Math);
        resultText = findViewById(R.id.editTextResult);
        tvStatus = findViewById(R.id.textView_status);

        /* Apagar os textos que já aparecem */
        mathText.setText("");
        resultText.setText("");

        botaoSwitch = findViewById(R.id.botaoSwitchRadDeg);
        botaoSwitch.setTextColor(Color.GREEN);

        radianos = botaoSwitch.getText().toString().equals("Rad");

        //  Ajustar os itens
        linearLayout = findViewById(R.id.mainLayout);
        gridLayout = findViewById(R.id.mainGrid);

        tv = findViewById(R.id.textView_status);

        handler = new Handler();

        sensor_manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        try {
            sensorProximidade = sensor_manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        } catch(NullPointerException npe)
        {
            Log.e("[ERROR]", npe.getMessage());
        }

        if(Rede.isConnected) {
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundColor(getResources().getColor(R.color.holo_green));
            tvStatus.setText("ONLINE");
        } else {
            tvStatus.setBackgroundColor(Color.RED);
            tvStatus.setText("OFFLINE");
        }
    }

    Runnable batterySaver = new Runnable() {
        @Override
        public void run() {
            flag = true;
            handler.post(this);
        }
    };

    @Override
    protected void onResume()
    {
        sensor_manager.registerListener(this,sensorProximidade,SensorManager.SENSOR_DELAY_NORMAL);
        handler.post(batterySaver);
        if(Rede.ban) {
            Rede.stopThread = true;
            if(Build.VERSION.SDK_INT > 21)
                finishAndRemoveTask();
            else
                finish();
        }

        super.onResume();
    }

    @Override
    public void onAccuracyChanged(Sensor s, int v)
    {

    }

    private void mudarBrilhoTela(float brilho)
    {
        try {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.screenBrightness = brilho;

            getWindow().setAttributes(lp);
        } catch(Exception e)
        {
            Log.e("[ERROR]", "Falha ao mudar brilho da tela: " + e.getMessage());
        }
    }

    @Override
    public void onSensorChanged(SensorEvent se)
    {
        if(se.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            if(flag)
            {
                if (se.values[0] == sensorProximidade.getMaximumRange()) {
                    Log.i("[SENSOR]", "Longe da tela");
                    mudarBrilhoTela(0f);
                } else {
                    Log.i("[SENSOR]", "Perto da tela");
                    mudarBrilhoTela(100f);
                }

                flag = false;
            }
        }
    }

    @Override
    public void onUserInteraction()
    {
        handler = new Handler();

        if(Rede.ban)
        {
            Rede.stopThread = true;
            tvStatus.setText("VOCÊ FOI BANIDO");
            tvStatus.setBackgroundColor(Color.CYAN);
            tvStatus.setTextColor(Color.BLACK);

            /*
            try {
                synchronized (this) {
                    wait(4000);
                }
            } catch(InterruptedException ie)
            {

            } finally {
                if(Build.VERSION.SDK_INT > 21) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            }
            */
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if(Rede.isConnected) {
                    tv.setTextColor(Color.BLACK);
                    tv.setBackgroundColor(getResources().getColor(R.color.holo_green));
                    tv.setText("ONLINE");
                } else {
                    tv.setText("OFFLINE");
                    tv.setBackgroundColor(Color.RED);
                    tv.setTextColor(Color.WHITE);
                }
            }
        },1000);
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == AlertDialog.BUTTON_POSITIVE)
                {
                    MainActivityStopped  = true;
                    if(Build.VERSION.SDK_INT > 21)
                        finishAndRemoveTask();
                    else
                        finish();
                }
            }
        });
        alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == AlertDialog.BUTTON_NEGATIVE) {
                    dialog.cancel();
                }
            }
        });
        alert.setTitle("Cuidado");
        alert.setMessage("Você quer mesmo sair do aplicativo? Isso pode fazer o aplicador de prova zerar a sua nota");
        alert.show();
    }

    @Override
    public void onPause()
    {
        MainActivityStopped = true;
        sensor_manager.unregisterListener(this);
        handler.removeCallbacks(batterySaver);
        super.onPause();
    }

    @Override
    public void onStop()
    {
        MainActivityStopped = true;
        super.onStop();
    }

    public void OnClick(View b)
    {
        double x;
        double result;
        String text;

        switch(b.getId())
        {
            case R.id.botao_divisao:
                mathText.append(getString(R.string.divisao));
                break;

            case R.id.botao_igual:
                /* Avaliar expressão matemática e realizar o devido cálculo */

                //text = text.replace(getString(R.string.numero_de_euler),String.valueOf(Matematica.NUMERO_EULER));

                /* Corrigir a expressão matemática */

                text = mathText.getText().toString();
                text = new Matematica.Expressao(text,this).corrigir();

                if(!text.isEmpty()) {
                    text = text.toLowerCase();
                    Log.i("[EXPRESSAO]", text);
                }

                try
                {
                    result = dv.evaluate(text);

                    if(Double.isNaN(result))
                    {
                        resultText.setText("Erro");
                    }
                    else if(Double.isInfinite(result))
                    {
                        resultText.setText(R.string.infinito);
                    } else {
                        resultText.setText(String.valueOf(result));
                    }
                } catch(IllegalArgumentException iae)
                {
                    resultText.setText("Expressão não reconhecida");
                }
                break;

            case R.id.botao_multiplicacao:
                mathText.append(getString(R.string.multiplicacao));
                break;

            case R.id.botao_soma:
                mathText.append(getString(R.string.soma));
                break;

            case R.id.botao_subtracao:
                mathText.append(getString(R.string.subtracao));
                break;

            case R.id.botao_zero:
                mathText.append(getString(R.string.zero));
                break;

            case R.id.botao_um:
                mathText.append(getString(R.string.um));
                break;

            case R.id.botao_dois:
                mathText.append(getString(R.string.dois));
                break;

            case R.id.botao_tres:
                mathText.append(getString(R.string.tres));
                break;

            case R.id.botao_quatro:
                mathText.append(getString(R.string.quatro));
                break;

            case R.id.botao_cinco:
                mathText.append(getString(R.string.cinco));
                break;

            case R.id.botao_seis:
                mathText.append(getString(R.string.seis));
                break;

            case R.id.botao_sete:
                mathText.append(getString(R.string.sete));
                break;

            case R.id.botao_oito:
                mathText.append(getString(R.string.oito));
                break;

            case R.id.botao_nove:
                mathText.append(getString(R.string.nove));
                break;

            case R.id.botao_abre_parentese:
                mathText.append(getString(R.string.abre_parentese));
                break;

            case R.id.botao_fecha_parentese:
                mathText.append(getString(R.string.fecha_parentese));
                break;

            case R.id.botao_decimal:
                mathText.append(getString(R.string.decimal));
                break;

            case R.id.botao_seno:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    /* Corrige a expressão matemática */
                    text = new Matematica.Expressao(text,this).corrigir();

                    /* Calcula o resultado da expressão digitada */
                    try {
                        x = dv.evaluate(text);
                        if(!radianos) {
                            /* Converter o x para radianos, pois a função só trabalha com radianos */
                            x = Math.toRadians(x);
                        }

                        result = Matematica.seno(x);
                        resultText.setText(String.valueOf(result));
                    } catch(Throwable error)
                    {
                        resultText.setText("Erro");
                        Log.e("[ERRO]", error.getCause().toString() + " : " + error.getMessage());
                    }
                }
                break;

            case R.id.botao_cosseno:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    /* Corrige a expressão matemática */
                    text = new Matematica.Expressao(text,this).corrigir();

                    /* Calcula o resultado da expressão digitada */
                    try {
                        x = dv.evaluate(text);
                        if(!radianos) {
                            /* Converter o x para radianos, pois a função só trabalha com radianos */
                            x = Math.toRadians(x);
                        }

                        result = Matematica.cosseno(x);

                        resultText.setText(String.valueOf(result));
                    } catch(Throwable error)
                    {
                        resultText.setText("Erro");
                        Log.e("[ERRO]", error.getCause().toString() + " : " + error.getMessage());
                    }
                }
                break;

            case R.id.botao_tangente:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    /* Corrige a expressão matemática */
                    text = new Matematica.Expressao(text,this).corrigir();

                    /* Calcula o resultado da expressão digitada */
                    try {
                        x = dv.evaluate(text);
                        if(!radianos) {
                            /* Converter o x para radianos, pois a função só trabalha com radianos */
                            x = Math.toRadians(x);
                        }

                        result = Matematica.tangente(x);

                        resultText.setText(String.valueOf(result));
                    } catch(Throwable error)
                    {
                        resultText.setText("Erro");
                        Log.e("[ERRO]", error.getCause().toString() + " : " + error.getMessage());
                    }
                }
                break;

            case R.id.botao_exp_x:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    try {
                        text = new Matematica.Expressao(text,this).corrigir();
                        x = dv.evaluate(text);
                        result = Matematica.exponencial(x);
                        if(Double.isNaN(result)) {
                            resultText.setText("Erro");
                        } else if(Double.isInfinite(result)) {
                            resultText.setText(R.string.infinito);
                        } else {
                            resultText.setText(String.valueOf(result));
                        }
                    } catch(Throwable error)
                    {
                        resultText.setText("Erro");
                    }
                } else {
                    common.showMessage("Erro", "Insira um número");
                }

                break;

            case R.id.botaoApagar:
                String new_text;

                text = mathText.getText().toString();
                resultText.setText("");

                if(!text.isEmpty())
                {
                    new_text = text.substring(0, text.length() - 1);
                    mathText.setText(new_text);
                    mathText.setSelection(new_text.length());
                }

                break;

            case R.id.botaoClear:
                mathText.setText("");
                resultText.setText("");
                break;

            case R.id.botaoSwitchRadDeg:
                if(radianos)
                {
                    botaoSwitch.setText("Gra");
                    botaoSwitch.setTextColor(Color.RED);
                    radianos = false;
                }
                else
                {
                    botaoSwitch.setText("Rad");
                    botaoSwitch.setTextColor(Color.GREEN);
                    radianos = true;
                }
                Log.i("[switch]", "Radianos: " + radianos);
                break;

            case R.id.botao_pi:
                mathText.append(getString(R.string.pi));
                break;

            case R.id.botao_num_euler:
                mathText.append(getString(R.string.numero_de_euler));
                break;

            case R.id.botao_raiz:
                // Calcular a raiz
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(text.isEmpty())
                {
                    common.showMessage("Erro", "Nenhum número foi inserido");
                } else {
                    try {
                        text = new Matematica.Expressao(text, this).corrigir();
                        if(text.contains("i")) {
                            common.showMessage("Erro", "Sei que sou uma calculadora, mas ainda não trabalho tão bem com números complexos");
                        } else {
                            x = dv.evaluate(text);
                            if (x < 0) {
                                x *= -1;
                                result = Matematica.raiz_quadrada(x);
                                resultText.setText(result + "i");
                            } else {
                                result = Matematica.raiz_quadrada(x);
                                resultText.setText("" + result);
                            }
                        }
                    } catch(Throwable error)
                    {
                        Log.e("[ERRO]", error.getCause().toString() + " : " + error.getMessage());
                        resultText.setText("Erro");
                    }
                }
                break;

            case R.id.botao_dez_elevx:
                dv = new DoubleEvaluator();

                try
                {
                    text = mathText.getText().toString();
                    if (text.isEmpty())
                    {
                        common.showMessage("Erro", "Preciso saber qual é o valor do expoente");
                    } else {
                        text = new Matematica.Expressao(text,this).corrigir();
                        x = dv.evaluate(text);

                        result = Math.pow(10.0, x);
                        resultText.setText(String.valueOf(result));
                    }
                } catch(Throwable error)
                {
                    common.showMessage("Erro crítico", "Não foi possível realizar esta operação. Tente novamente");
                    Log.e("[ERROR]", error.getMessage());
                }

                break;

            case R.id.botao_arcoseno:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    /* Corrige a expressão matemática */
                    text = new Matematica.Expressao(text,this).corrigir();

                    /* Calcula o resultado da expressão digitada */
                    try {
                        x = dv.evaluate(text);
                        if(!radianos) {
                            /* Converter o x para radianos, pois a função só trabalha com radianos */
                            x = x * (Matematica.PI / 180.0);
                        }

                        result = Matematica.arcoseno(x);
                        resultText.setText(String.valueOf(result));
                    } catch(Throwable error)
                    {
                        resultText.setText("Erro");
                        Log.e("[ERRO]", error.getCause().toString() + " : " + error.getMessage());
                    }
                }
                break;

            case R.id.botao_arcocoseno:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();
                text = new Matematica.Expressao(text,this).corrigir();

                if(!text.isEmpty())
                {
                    /* Calcula o resultado da expressão digitada */
                    x = dv.evaluate(text);
                    if(!radianos) {
                        /* Converter o x para radianos, pois a função só trabalha com radianos */
                        x = x * (Matematica.PI / 180.0);
                    }

                    result = Matematica.arcotangente(x);
                    resultText.setText(String.valueOf(result));
                }
                break;

            case R.id.botao_ln:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    // Calcula o resultado da expressão digitada
                    text = new Matematica.Expressao(text,this).corrigir();
                    x = dv.evaluate(text);
                    try {
                        result = Matematica.ln(x);
                        resultText.setText(String.valueOf(result));
                    } catch(Throwable e) {
                        common.showMessage(e.getMessage(),Toast.LENGTH_SHORT);
                    }
                }
                break;

            case R.id.botao_log:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    /* Avalia a expressão matemática */
                    text = new Matematica.Expressao(text,this).corrigir();
                    /* Calcula o resultado da expressão digitada */
                    x = dv.evaluate(text);
                    try {
                        result = Matematica.logaritmo10(x);
                        resultText.setText(String.valueOf(result));
                    } catch(Throwable e) {
                        common.showMessage("Erro", e.getMessage());
                    }
                }
                break;

            case R.id.botao_fatorial:
                long long_x;

                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    text = new Matematica.Expressao(text,this).corrigir();

                    try
                    {
                        x = dv.evaluate(text);
                        if(x == Math.floor(x) && !Double.isInfinite(x))
                        {
                            long_x = (long)x;
                            result = Matematica.fatorial(long_x);
                        } else {
                            result = Matematica.fatorial(x);
                        }

                        if(Double.isNaN(result))
                        {
                            resultText.setText("Erro");
                        } else if(Double.isInfinite(result))
                        {
                            resultText.setText(R.string.infinito);
                        } else {
                            if (x < 0)
                                resultText.setText("-" + result);
                            else
                                resultText.setText("" + result);
                        }
                    } catch(Throwable error)
                    {
                        resultText.setText("Erro");
                    }
                }
                break;

            case R.id.botao_x_elevy:
                mathText.append("^");
                break;

            case R.id.botaoModulo:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    text = new Matematica.Expressao(text,this).corrigir();

                    try {
                        x = dv.evaluate(text);
                        result = Math.abs(x);
                            resultText.setText("" + result);
                    } catch(Throwable error)
                    {
                        resultText.setText("Erro");
                        Log.e("[ERRO]", error.getCause().toString() + " : " + error.getMessage());
                    }
                }
                break;

            case R.id.botao_xquadrado:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    text = new Matematica.Expressao(text,this).corrigir();

                    try {
                        x = dv.evaluate(text);
                        result = Math.pow(x,2.0);
                        if(!Double.isInfinite(result) && !Double.isNaN(result)) {
                            resultText.setText("" + result);
                        } else if(Double.isInfinite(result)) {
                            resultText.setText(R.string.infinito);
                        } else {
                            resultText.setText("Erro: o resultado não é um número (NaN)");
                        }
                    } catch(Exception error)
                    {
                        resultText.setText("Erro");
                        Log.e("[ERRO]", error.getCause().toString() + " : " + error.getMessage());
                    }
                }
                break;

            case R.id.botao_raiz_cubica:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    text = new Matematica.Expressao(text,this).corrigir();

                    try {
                        x = dv.evaluate(text);
                        result = Math.cbrt(x);
                        if(!Double.isInfinite(result) && !Double.isNaN(result)) {
                            resultText.setText("" + result);
                        } else if(Double.isInfinite(result)) {
                            resultText.setText(R.string.infinito);
                        } else {
                            resultText.setText("Erro: o resultado não é um número (NaN)");
                        }
                    } catch(Exception error)
                    {
                        resultText.setText("Erro");
                        Log.e("[ERRO]", error.getCause().toString() + " : " + error.getMessage());
                    }
                }
                break;

            case R.id.botao_arcotangente:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    /* Corrige a expressão matemática */
                    text = new Matematica.Expressao(text,this).corrigir();

                    /* Calcula o resultado da expressão digitada */
                    try {
                        x = dv.evaluate(text);
                        if(!radianos) {
                            /* Converter o x para radianos, pois a função só trabalha com radianos */
                            x = Math.toRadians(x);
                        }

                        result = Matematica.arcotangente(x);

                        resultText.setText(String.valueOf(result));
                    } catch(Throwable error)
                    {
                        resultText.setText("Erro");
                        Log.e("[ERRO]", error.getCause().toString() + " : " + error.getMessage());
                    }
                }
                break;

            case R.id.botaoSair:
                int vezes = 5;
                if(vezes == 0)
                {
                    if(Build.VERSION.SDK_INT > 21)
                        finishAndRemoveTask();
                    else
                        finish();

                    Rede.stopThread = true;
                }

                String msg = "Clique mais" + vezes + "vezes para sair do programa";
                common.showMessage(msg,Toast.LENGTH_SHORT);
                vezes--;
                break;

            default:
                common.showMessage("Ação ainda não implementada",Toast.LENGTH_SHORT);
                //common.showMessage("Desculpe", "Esta ação ainda não foi implementada");
        }
    }
}
