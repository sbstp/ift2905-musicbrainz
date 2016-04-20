package org.ift2905.musicbrainz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;

public class Util {

    public static ProgressDialog makeProgressDialog(Context ctx) {
        ProgressDialog p = new ProgressDialog(ctx);
        p.setCancelable(false);
        p.setCanceledOnTouchOutside(false);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p.setIndeterminate(true);
        return p;
    }

    public static AlertDialog ioErrorDialog(final Activity act, final boolean finishOnOk) {
        return new AlertDialog.Builder(act)
                .setCancelable(false)
                .setMessage(act.getResources().getString(R.string.util_io_error))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (finishOnOk) {
                            act.finish();
                        }
                    }
                }).create();
    }

    public static void interweaveListViewBgColor(int position, View v) {
        if (position % 2 == 0) {
            v.setBackgroundColor(Color.WHITE);
        } else {
            v.setBackgroundColor(Color.LTGRAY);
        }
    }

    public static String secondsToText(int secs) {
        secs /= 1000;
        int minutes = secs / 60;
        int seconds = secs % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}
