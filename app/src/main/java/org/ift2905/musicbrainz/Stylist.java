package org.ift2905.musicbrainz;

import android.graphics.Color;
import android.view.View;

public class Stylist {

    public static void interweaveListViewBgColor(int position, View v) {
        if (position % 2 == 0) {
            v.setBackgroundColor(Color.WHITE);
        } else {
            v.setBackgroundColor(Color.LTGRAY);
        }
    }

}
