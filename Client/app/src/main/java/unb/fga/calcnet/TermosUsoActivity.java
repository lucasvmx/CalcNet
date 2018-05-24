/*
    TermosUsoActivity.java
    Autor: Lucas Vieira de Jesus

    Este arquivo contém os métodos relacionados com as operacoes na activity TermosUsoActivity
 */

package unb.fga.calcnet;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class TermosUsoActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos_uso);
        setTitle(autorevision.VCS_BASENAME + " " + autorevision.VCS_TAG);
    }

    public void OnClick(View v)
    {
        switch(v.getId())
        {
            case R.id.botao_nao:
                this.finishAndRemoveTask();
                break;

            case R.id.botao_sim:
                if(Rede.modoAviaoLigado(getApplicationContext().getContentResolver()) == Rede.MODO_AVIAO_ON)
                {
                    Intent i = new Intent(this, ActivityDadosUsuario.class);
                    this.startActivity(i);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Erro crítico");
                    if(Rede.bluetoothLigado())
                        alert.setMessage("Desligue o bluetooth e ative o modo avião");
                    else
                        alert.setMessage("Ative o modo avião");

                    alert.show();
                }
                break;
        }
    }
}