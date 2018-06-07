package unb.fga.calcnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

public class Common
{
    Context c;
    Activity activity;

    public Common(Context ctx)
    {
        this.c = ctx;
    }

    public Common(Context ctx, Activity activity)
    {
        c = ctx;
        activity = activity;
    }

    public void mudarBrilhoTela(float brilho)
    {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.screenBrightness = brilho;

            activity.getWindow().setAttributes(lp);
        } catch(Exception e)
        {
            Log.e("[ERROR]", "Falha ao mudar brilho da tela: " + e.getMessage());
        }
    }

    public int getScreenOffTimeout(boolean seconds)
    {
        int result = -1;

        try {
            result = Settings.System.getInt(c.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch(Settings.SettingNotFoundException snfe)
        {
            Log.e("[ERROR]", "Failed to get screen off timeout: " + snfe.getMessage());
        }

        /* Retornaremos o resultado já convertido em segundos */
        if(seconds)
            result /= 1000;

        return result;
    }

    public static double DpToPx(double dp, String density_type)
    {
        Double px = -1.0;
        Double new_px;

        switch (density_type)
        {
            case "ldpi": px = 0.75; break;
            case "mdpi": px = 1.0; break;
            case "tvdpi": px = 1.33; break;
            case "hdpi": px = 1.50; break;
            case "xxhdpi": px = 2.0; break;
            case "xxxhdpi": px = 3.0; break;
        }
        new_px = dp * px;

        return new_px;
    }

    public AlertDialog showMessage(String title, String text)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setTitle(title);
        dialog.setMessage(text);
        dialog.setPositiveButton("OK",null);

        return dialog.show();
    }
}
