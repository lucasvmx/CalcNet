package unb.fga.calcnet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClickMenos(View view)
    {
        EditText text = findViewById(R.id.editText_Math);
        text.append(getString(R.string.subtracao));
    }

    public void OnClickMais(View view)
    {
        EditText text = findViewById(R.id.editText_Math);
        text.append(getString(R.string.soma));
    }

    public void OnClickVezes(View view)
    {
        EditText text = findViewById(R.id.editText_Math);
        text.append(getString(R.string.multiplicacao));
    }

    public void OnClickDivisao(View view)
    {
        EditText text = findViewById(R.id.editText_Math);
        text.append(getString(R.string.divisao));
    }

    public void OnClickIgual(View view)
    {

    }

    public void OnClick0(View view)
    {
        EditText text = findViewById(R.id.editText_Math);
        text.append(getString(R.string.zero));
    }

    public void OnClick1(View view)
    {
        EditText text = findViewById(R.id.editText_Math);
        text.append(getString(R.string.um));
    }
}
