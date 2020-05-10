package com.solar.pinterest.solarmobile.network.tools;

import android.util.Log;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampConverter {
    private static final String sDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'";
    public static String fromDate(Date timestamp) {
        DateFormat df = new SimpleDateFormat(sDateFormat);
        return df.format(timestamp);
    }

    public static Date toDate(String timestamp) {
        DateFormat df = new SimpleDateFormat(sDateFormat);
        try {
            return df.parse(timestamp);
        } catch (ParseException e) {
            Log.e("Solar", "Cannot parse date");
            return new Date();
        }
    }
}