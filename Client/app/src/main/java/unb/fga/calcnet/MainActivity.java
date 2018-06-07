/*
    MainActivity.java
    Autor: Lucas Vieira de Jesus

    Este arquivo contém os métodos relacionados com as operacoes na activity MainActivity
 */

package unb.fga.calcnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import java.net.Socket;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{
    public static Socket mainSocket = null;
    private EditText mathText;
    private EditText resultText;
    private GridLayout mainGrid;
    private boolean radianos;
    private Button botaoSwitch;
    private DoubleEvaluator dv;
    private Common common;
    public static volatile boolean MainActivityStopped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dv = new DoubleEvaluator();
        common = new Common(this);

        mathText = findViewById(R.id.editText_Math);
        resultText = findViewById(R.id.editTextResult);

        /* Apagar os textos que já aparecem */
        mathText.setText("");
        resultText.setText("");

        botaoSwitch = findViewById(R.id.botaoSwitchRadDeg);
        botaoSwitch.setTextColor(Color.GREEN);

        if(botaoSwitch.getText().toString().equals("Rad"))
            radianos = true;
        else
            radianos = false;

        // Adjust items
        mainGrid = findViewById(R.id.mainGrid);
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
                    finishAndRemoveTask();
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
        String text = "";

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

                if(radianos)
                {
                    // Converter o argumento de todos os angulos para radianos
                } else {
                    // Converter para graus
                }

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
                mathText.append(getString(R.string.seno) + "(");
                break;

            case R.id.botao_cosseno:
                mathText.append(getString(R.string.cosseno) + "(");
                break;

            case R.id.botao_tangente:
                mathText.append(getString(R.string.tangente) + "(");
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
                    } catch(Exception error)
                    {
                        resultText.setText("Erro");
                        Log.e("[ERRO]", error.getCause().toString() + " : " + error.getMessage());
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
                    } catch(Exception error)
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
                } catch(Exception error)
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
                        result = Matematica.arcoseno(x);
                        resultText.setText(String.valueOf(result));
                    } catch(Exception error)
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
                    result = Matematica.arcocoseno(x);
                    resultText.setText(String.valueOf(result));
                }
                break;

            case R.id.botao_ln:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();
                text = new Matematica.Expressao(text,this).corrigir();

                if(!text.isEmpty())
                {
                    /* Calcula o resultado da expressão digitada */
                    x = dv.evaluate(text);
                    try {
                        result = Matematica.ln(x);
                        resultText.setText(String.valueOf(result));
                    } catch(Exception e) {
                        common.showMessage("Erro", e.getMessage());
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
                    } catch(Exception e) {
                        common.showMessage("Erro", e.getMessage());
                    }
                }
                break;

            case R.id.botao_fatorial:
                dv = new DoubleEvaluator();
                text = mathText.getText().toString();

                if(!text.isEmpty())
                {
                    text = new Matematica.Expressao(text,this).corrigir();

                    try {
                        x = dv.evaluate(text);
                        result = Matematica.fatorial(Math.round(x));
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
                    } catch(Exception error)
                    {
                        resultText.setText("Erro");
                        Log.e("[ERRO]", error.getCause().toString() + " : " + error.getMessage());
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
                    } catch(Exception error)
                    {
                        resultText.setText("Erro");
                        Log.e("[ERRO]", error.getCause().toString() + " : " + error.getMessage());
                    }
                }
                break;

            default:
                common.showMessage("Desculpe", "Esta ação ainda não foi implementada");
        }
    }
}
