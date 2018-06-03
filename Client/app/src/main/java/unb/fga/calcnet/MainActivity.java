/*
    MainActivity.java
    Autor: Lucas Vieira de Jesus

    Este arquivo contém os métodos relacionados com as operacoes na activity MainActivity
 */

package unb.fga.calcnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        radianos = true;
        // Adjust items
        mainGrid = findViewById(R.id.mainGrid);
    }

    @Override
    public void onBackPressed()
    {
        common.showMessage("Cuidado","Se você sair, entenderei que você está tentando fraudar a prova!");
        Log.d("[DEBUG]", "Back pressed");
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

                /* Corrigir a expressão matemática */
                text = mathText.getText().toString();
                mathText.setText(text.replace(getString(R.string.multiplicacao),"*"));
                text = mathText.getText().toString();
                mathText.setText(text.replace(getString(R.string.divisao),"/"));

                try
                {
                    result = dv.evaluate(mathText.getText().toString());
                    if(!Double.isNaN(result) && !Double.isInfinite(result))
                    {
                        resultText.setText(String.valueOf(result));
                    }
                    else {
                        resultText.setText("ERROR");
                    }
                } catch(IllegalArgumentException iae)
                {
                    resultText.setText("ERROR");
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
                text = mathText.getText().toString();
                if(!text.isEmpty())
                {
                    x = Double.parseDouble(text);
                    result = Matematica.exponencial(x);
                    resultText.setText(String.valueOf(result));
                } else {
                    common.showMessage("Erro", "Insira um número");
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
                text = mathText.getText().toString();
                if(text.isEmpty())
                {
                    common.showMessage("Erro", "Nenhum número foi inserido");
                } else {
                    x = Double.parseDouble(text);
                    result = Matematica.raiz_quadrada(x);

                    resultText.setText("" + result);
                }
                break;

            default:
                common.showMessage("Info", "Não implementado ainda");
        }
    }
}
