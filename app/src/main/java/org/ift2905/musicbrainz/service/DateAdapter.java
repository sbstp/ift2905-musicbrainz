package org.ift2905.musicbrainz.service;

import android.util.Log;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateAdapter {

    @FromJson
    public GregorianCalendar parseDate(String date) {
        if (date.isEmpty()) {
            return null;
        }

        String[] parts = date.split("-");
        int year = 0, month = 0, day = 0;

        switch (parts.length) {
            case 3:
                day = Integer.parseInt(parts[2]);
            case 2:
                month = Integer.parseInt(parts[1]);
            case 1:
                year = Integer.parseInt(parts[0]);
        }
        return new GregorianCalendar(year, month, day);
    }

    @ToJson
    public String formatDate(GregorianCalendar date) {
        return String.format("%04d-%02d-%02d",
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));
    }

}
