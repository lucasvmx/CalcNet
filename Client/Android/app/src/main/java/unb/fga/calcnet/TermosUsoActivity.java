/*
    TermosUsoActivity.java
    Autor: Lucas Vieira de Jesus

    Este arquivo contém os métodos relacionados com as operacoes na activity TermosUsoActivity
 */

package unb.fga.calcnet;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.graphics.Point;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TermosUsoActivity extends Activity
{
    private Button botaoSim, botaoNao;
    private LinearLayout mainLayout;
    private Point size;
    private DisplayMetrics displayMetrics;
    private String dpi_type;
    private Common common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos_uso);
        setTitle(autorevision.VCS_BASENAME + " - Termos de uso");
        size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        common = new Common(this,this);

        botaoSim = findViewById(R.id.botao_sim);
        botaoNao = findViewById(R.id.botao_nao);
        mainLayout = findViewById(R.id.termosUsoConstraintLayout);

        Common common = new Common(this,this);

        try {
            displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            switch (displayMetrics.densityDpi)
            {
                case 120: dpi_type = "ldpi"; break;
                case 160: dpi_type = "mdpi"; break;
                case 213: dpi_type = "tvdpi"; break;
                case 240: dpi_type = "hdpi"; break;
                case 320: dpi_type = "xhdpi"; break;
                case 480: dpi_type = "xxhdpi"; break;
                case 640: dpi_type = "xxxhdpi"; break;
            }
        } catch(Exception e)
        {
            Log.e("[ERROR]", e.getMessage());
        }
    }

    public void OnClick(View v)
    {
        switch(v.getId())
        {
            case R.id.botao_nao:
                if(Build.VERSION.SDK_INT >= 21)
                    this.finishAndRemoveTask();
                else
                    finish();
                break;

            case R.id.botao_sim:
                if(Rede.modoAviaoLigado(getApplicationContext().getContentResolver()) == Rede.MODO_AVIAO_ON)
                {
                    Intent i = new Intent(this, ActivityDadosUsuario.class);
                    this.startActivity(i);
                    finish();
                } else {
                    if(Rede.bluetoothLigado())
                       common.showMessage(getString(R.string.msg_bluetooth_off), Toast.LENGTH_SHORT);
                    else
                        common.showMessage(getString(R.string.msg_modo_aviao_on), Toast.LENGTH_SHORT);
                }
                break;

            case R.id.botao_modo_offline:
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("offline_mode",true);
                if(Build.VERSION.SDK_INT > 21) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                }
                else {
                    startActivity(intent);
                }
                finish();
                break;
        }
    }
}