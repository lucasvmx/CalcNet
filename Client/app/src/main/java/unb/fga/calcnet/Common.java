package unb.fga.calcnet;

import android.app.AlertDialog;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class Common
{
    Context c;

    public Common(Context ctx)
    {
        this.c = ctx;
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

    public AlertDialog showMessage(String title, String text)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setTitle(title);
        dialog.setMessage(text);
        dialog.setPositiveButton("OK",null);

        return dialog.show();
    }
}
