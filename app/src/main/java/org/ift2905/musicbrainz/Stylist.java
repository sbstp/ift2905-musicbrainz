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

}
