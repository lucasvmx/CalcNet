/*
    TermosUsoActivity.java
    Autor: Lucas Vieira de Jesus

    Este arquivo contém os métodos relacionados com as operacoes na activity TermosUsoActivity
 */

package unb.fga.calcnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.graphics.Point;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.AbsListView.LayoutParams;

public class TermosUsoActivity extends Activity
{
    private Button botaoSim, botaoNao;
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos_uso);
        setTitle(autorevision.VCS_BASENAME + " - Termos de uso");

        botaoSim = findViewById(R.id.botao_sim);
        botaoNao = findViewById(R.id.botao_nao);
        mainLayout = findViewById(R.id.termosUsoConstraintLayout);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        Common common = new Common(this);

        Log.i("[SIZE]", "Dimensoes: " + ConvertToPoint(mainLayout.getWidth()) + " x " + ConvertToPoint(mainLayout.getHeight()));
    }

    @Override
    public void onStop()
    {
        Log.e("[WARNING]", "Usuario cometendo fraude");
        super.onStop();
    }

    private int ConvertToPoint(double px)
    {
        Double pt;

        pt = px * 0.75;

        return pt.intValue();
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
                    finish();
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