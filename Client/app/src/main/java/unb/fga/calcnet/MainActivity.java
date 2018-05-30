/*
    MainActivity.java
    Autor: Lucas Vieira de Jesus

    Este arquivo contém os métodos relacionados com as operacoes na activity MainActivity
 */

package unb.fga.calcnet;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class MainActivity extends AppCompatActivity
{
    private Bundle dados;
    private Socket sock = null;

    private int n1;
    private int n2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dados = getIntent().getExtras();

        sock = (Socket)dados.get("socket");
    }

    public void OnClick(View b)
    {
        EditText mathText = findViewById(R.id.editText_Math);

        /* FIXME: falta o botão CLEAR */
        switch(b.getId())
        {
            case R.id.botao_divisao:
                mathText.append(getString(R.string.divisao));
                break;

            case R.id.botao_igual:
                /* Avaliar expressão matemática e realizar o devido cálculo */
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
                n1 = 0;
                break;

            case R.id.botao_um:
                mathText.append(getString(R.string.um));
                n1 = 1;
                break;

            case R.id.botao_dois:
                mathText.append(getString(R.string.dois));
                n1 = 2;
                break;

            case R.id.botao_tres:
                mathText.append(getString(R.string.tres));
                n1 = 3;
                break;

            case R.id.botao_quatro:
                mathText.append(getString(R.string.quatro));
                n1 = 4;
                break;

            case R.id.botao_cinco:
                mathText.append(getString(R.string.cinco));
                n1 = 5;
                break;

            case R.id.botao_seis:
                mathText.append(getString(R.string.seis));
                n1 = 6;
                break;

            case R.id.botao_sete:
                mathText.append(getString(R.string.sete));
                n1 = 7;
                break;

            case R.id.botao_oito:
                mathText.append(getString(R.string.oito));
                n1 = 8;
                break;

            case R.id.botao_nove:
                mathText.append(getString(R.string.nove));
                n1 = 9;
                break;

            case R.id.botao_abre_parentese:
                //mathText.append(getString(R.string.abre_parentese));
                break;

            case R.id.botao_fecha_parentese:
                //.append(getString(R.string.fecha_parentese));
                break;
        }
    }
}
