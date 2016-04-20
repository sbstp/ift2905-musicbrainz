package org.ift2905.musicbrainz;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;

public class Stylist {

    public static ProgressDialog makeProgressDialog(Context ctx) {
        ProgressDialog p = new ProgressDialog(ctx);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p.setIndeterminate(true);
        return p;
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
