package com.gulshansutey.newsapplication.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String DATE_FORMAT_SIMPLIFIED = "MMM dd - HH:mm a";

    /**
     * Converts string time in simplified string time
     *
     * @param time given time in <code>DATE_FORMAT</code> format to be simplified
     * @return date in <code>DATE_FORMAT_SIMPLIFIED</code> format
     * @throws Exception if the beginning of the specified string cannot be parsed.
     * @see #dateToMilli(String)
     */
    public static String simplifyDate(String time) throws Exception {
        if (time == null) return "";
        long timeCurrent = dateToMilli(time);
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_SIMPLIFIED);
        Date date = new Date();
        date.setTime(timeCurrent);
        return dateFormat.format(date);
    }


    /**
     * This method converts String date in given format into milliseconds
     *
     * @param time given time in format <code>DATE_FORMAT</code>
     * @return long milliseconds
     * @throws Exception if the beginning of the specified string cannot be parsed.
     */
    public static long dateToMilli(String time) throws Exception {
        if (time == null) return 0;
        DateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String dateString = time.replace("Z", "GMT+00:00");
        sdf.parse(dateString);
        return sdf.getCalendar().getTimeInMillis();
    }

}
