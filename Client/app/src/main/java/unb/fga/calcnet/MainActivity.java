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

            case R.id.botao_um:
                mathText.append(getString(R.string.um));
                break;

            case R.id.botao_zero:
                mathText.append(getString(R.string.zero));
                break;
        }
    }
}
