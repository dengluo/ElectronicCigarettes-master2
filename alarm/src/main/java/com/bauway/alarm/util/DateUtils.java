package com.bauway.alarm.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by zhaotaotao on 2017/6/28.
 * 日期工具类
 */

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    private static SimpleDateFormat serviceDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat serviceDate2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static SimpleDateFormat serviceDate3 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private static SimpleDateFormat shortTimeDate = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat shortTimeDate2 = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dayOfMonth = new SimpleDateFormat("dd");
    private static SimpleDateFormat monthOfYear = new SimpleDateFormat("MM");
    private static SimpleDateFormat hourOfDay = new SimpleDateFormat("HH");
    private static SimpleDateFormat minOfHour = new SimpleDateFormat("mm");
    private static SimpleDateFormat hourAndMin = new SimpleDateFormat("HH:mm");


    private final static int MILLISECONDS = 1;
    private final static int SECONDS = 1000 * MILLISECONDS;
    private final static int MINUTES = 60 * SECONDS;
    private final static int HOURS = 60 * MINUTES;
    private final static int DAYS = 24 * HOURS;
    private final static long WEEKS = 7 * DAYS;
    private final static long MONTHS = 4 * WEEKS;
    private final static long YEARS = 12 * MONTHS;

    private static Calendar mCalendar;
    private static String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * Str-->long  yyyy-MM-dd HH:mm:ss
     *
     * @param date str
     * @return long
     */
    public static long getServiceDate(String date) {

        try {
            return serviceDate.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * str-->long   yyyy-MM-dd'T'HH:mm:ss
     *
     * @param date str
     * @return long
     */
    public static long getServiceDate2(String date) {

        try {
            return serviceDate2.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * str-->long   yyyy/MM/dd HH:mm
     *
     * @param date str
     * @return long
     */
    public static long getServiceDate3(String date) {

        try {
            return serviceDate3.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * long——>Str   yyyy-MM-dd
     *
     * @param time long
     * @return str
     */
    public static String conversionTime(Long time) {
        return shortTimeDate2.format(time);
    }

    /**
     * Str——> Str  yy-MM-dd HH:mm:ss ---> yyyy-MM-dd
     *
     * @param date str
     * @return str
     */
    public static String conversionTime2(String date) {
        try {
            if (date != null) {
                Date mDate = serviceDate.parse(date);
                return shortTimeDate2.format(mDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * str-->Date    数据解析 yyyy-MM-dd
     *
     * @param date str
     * @return date
     */
    public static Date conversionTime3(String date) {
        try {
            if (date != null) {
                return shortTimeDate2.parse(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Date-Str  时间转换，转化为 yyyy-MM-dd
     *
     * @param date date
     * @return str
     */
    public static String conversionTime4(Date date) {
        return shortTimeDate2.format(date);
    }

    /**
     * long-Str  时间转换，转化为 yyyy-MM-dd HH:mm:ss
     *
     * @param date date
     * @return str
     */
    public static String conversionTime5(Long date) {
        return serviceDate.format(date);
    }

    /**
     * Str-Str  时间转换，转化为 HH:mm --> HH(int)
     *
     * @param date date
     * @return str
     */
    public static Integer conversionTime6(String date) {
        try {
            return Integer.valueOf(hourOfDay.format(hourAndMin.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Str-Str  时间转换，转化为 HH:mm --> mm(int)
     *
     * @param date date
     * @return str
     */
    public static Integer conversionTime7(String date) {
        try {
            return Integer.valueOf(minOfHour.format(hourAndMin.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Str-long  时间转换，转化为 HH:mm --> long
     *
     * @param date date
     * @return str
     */
    public static long conversionTime8(String date) {
        try {
            return hourAndMin.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }


    /**
     * long-->Str  时间转换，转化为 HH:mm:ss
     *
     * @param dateLong long
     * @return str
     */
    public static String parseShortDateStr(Long dateLong) {
        return shortTimeDate.format(dateLong);
    }


    /**
     * 获取当前时区与0时区的时间差值-->毫秒值
     */
    public static int getTimeZone() {
        return TimeZone.getDefault().getRawOffset();
    }

    /**
     * 获取当前时区与0时区的时间差值-->小时
     */
    public static int getTimeZoneHour() {
        return getTimeZone() / (1000 * 60 * 60);
    }

    /**
     * 获取0时区与当前时区转换后的时间
     * yy-MM-dd HH:mm:ss
     *
     * @param playLocus playLocus
     * @return str
     */
    public static String getConvertTime(String playLocus) {
        return conversionTime5(getServiceDate2(playLocus) + getTimeZone());
    }

    /**
     * 获取0时区与当前时区转换后的时间2 HH:mm:ss
     *
     * @param playLocus playLocus
     * @return str
     */
    public static String getConvertTime2(String playLocus) {
        return parseShortDateStr(getServiceDate(playLocus) + getTimeZone());
    }


    /**
     * 获取0时区与当前时区转换后的时间2 HH:mm:ss
     *
     * @param playLocus playLocus
     * @return str
     */
    public static String getConvertTime3(String playLocus) {
        return conversionTime5(getServiceDate3(playLocus) + getTimeZone());
    }


    /**
     * dd
     *
     * @param date date
     * @return 天
     */
    public static String dayOfMonth(Date date) {
        return dayOfMonth.format(date);
    }

    /**
     * MM
     *
     * @param date date
     * @return 月
     */
    public static String monthOfYear(Date date) {
        return monthOfYear.format(date);
    }

    public static String getNowTime() {
        return shortTimeDate2.format(new Date());
    }

    /**
     * 获取当前时间转换为0时区的时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDataStr() {
        return conversionTime5(new Date().getTime() - getTimeZone());
    }

    /**
     * 获取当前时间-12小时后，转换为0时区的时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDataStrMinus12() {
        return conversionTime5(new Date().getTime() - (1000 * 60 * 60 * 12) - getTimeZone());
    }

    /**
     * 获取指定时间-12小时后，转换为0时区的时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDataStrMinus12(String str) {
        return conversionTime5(getServiceDate(str) - (1000 * 60 * 60 * 12) - getTimeZone());
    }



}
