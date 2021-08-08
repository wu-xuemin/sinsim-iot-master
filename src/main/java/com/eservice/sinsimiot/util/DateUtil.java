package com.eservice.sinsimiot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author ：silent
 * @description ：
 * @date ：2020/9/22 16:32
 **/
public class DateUtil {

    public static final String GMT = "GMT+8";

    public static long computationTime(long timestamp) {
        //GMT+8
        int difference = 28800;
        int day = 24 * 60 * 60;
        return (timestamp + difference) % day;
    }

    /**
     * 当天开始时间
     *
     * @return
     */
    public static Date getDateStartTime() {
        return getDayDateTime(0, 0, 0, 0);
    }

    /**
     * 当天结束时间
     *
     * @return
     */
    public static Date getDateEndTime() {
        return getDayDateTime(23, 59, 59, 999);
    }


    /**
     * 获取当天指定时间
     *
     * @param hour
     * @param minute
     * @param second
     * @param milliSecond
     * @return
     */
    public static Date getDayDateTime(Integer hour, Integer minute, Integer second, Integer milliSecond) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, hour);
        todayStart.set(Calendar.MINUTE, minute);
        todayStart.set(Calendar.SECOND, second);
        todayStart.set(Calendar.MILLISECOND, milliSecond);
        return todayStart.getTime();
    }

    public static String timeToStringDateTime(Long time) {
        return dateToString(new Date(time), "yyyy-MM-dd HH:mm:ss");
    }

    public static String timeToStringDate(Long time) {
        return dateToString(new Date(time), "yyyy-MM-dd");
    }

    public static String timeToStringTime(Long time) {
        return dateToString(new Date(time), "HH:mm:ss");
    }

    public static String timeToString(Long time, String formatString) {
        return dateToString(new Date(time), formatString);
    }

    public static String dateToStringTimeZone(String formatString) {
        return dateToStringSetTimeZone(new Date(), formatString, TimeZone.getTimeZone(GMT));
    }

    public static String dateToStringTimeZone(Date date, String formatString) {
        return dateToStringSetTimeZone(date, formatString, TimeZone.getTimeZone(GMT));
    }

    public static String dateToStringSetTimeZone(Date date, String formatString, TimeZone timeZone) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }

    public static String dateToString(Date date, String formatString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        return simpleDateFormat.format(date);
    }

    public static Date stringToDate(String date, String formatString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
