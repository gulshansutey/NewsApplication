package com.gulshansutey.newsapplication.utils;

import android.net.ParseException;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateFormatUtils {


    public static String formatDate(String date) {
        // 2020-02-10T17:06:42Z
        CharSequence timeAgo = "";
        if (date == null) return timeAgo.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long time = sdf.parse("2016-01-24T16:00:00.000Z").getTime();
            long now = System.currentTimeMillis();
            timeAgo =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return "\u2022 "+timeAgo.toString();
    }

}
