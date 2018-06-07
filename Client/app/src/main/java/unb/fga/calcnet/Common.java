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

    public int ConvertToPoint(double px)
    {
        Double pt;

        pt = px * 0.75;

        return pt.intValue();
    }

    public Point getWindowSize()
    {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return size;
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
