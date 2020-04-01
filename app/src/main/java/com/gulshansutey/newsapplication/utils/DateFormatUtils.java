package com.gulshansutey.newsapplication.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {


    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";



    public static String formatDate(String time) throws Exception{
        if (time == null) return "";
            long timeCurrent = dateToMilli(time);
            DateFormat dateFormat = new SimpleDateFormat("MMM dd - HH:mm a");
            Date date = new Date();
            date.setTime(timeCurrent);
        return dateFormat.format(date);
    }

    public static long dateToMilli(String time) throws Exception {
        if (time==null)return 0;

        DateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String dateString = time.replace("Z", "GMT+00:00");
        sdf.parse(dateString);
        return sdf.getCalendar().getTimeInMillis();
    }






}
