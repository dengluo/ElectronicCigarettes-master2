package com.bauway.alarm.util;

import android.support.annotation.IntRange;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zhaotaotao on 2017/12/14.
 */

public class Tools {

    public static String[] num         = null;
    public static String[] amHours     = null;
    public static String[] pmHours     = null;
    public static String[] dayHours    = null;
    public static String[] hourMinutes = null;

    /**
     * 获取0至指定数字的数据
     *
     * @return
     */
    public static String[] getNum(int start, int end) {
        if (num == null) {
            ArrayList<String> integers = new ArrayList<>();
            for (int i = start; i <= end; i++) {
                integers.add(String.valueOf(i));
            }
            num = integers.toArray(new String[integers.size()]);
        }
        return num;
    }

    /**
     * 获取一天小时集合 0-23
     *
     * @return String
     */
    public static String[] getHoursOfDay() {
        if (dayHours == null) {
            ArrayList<String> integers = new ArrayList<>();
            for (int i = 0; i <= 23; i++) {
                if (i < 10) {
                    integers.add("0" + String.valueOf(i));
                } else {
                    integers.add(String.valueOf(i));
                }
            }
            dayHours = integers.toArray(new String[integers.size()]);
        }
        return dayHours;
    }

    /**
     * 获取一小时中分钟数的集合 0-59
     *
     * @return String
     */
    public static String[] getHourMinutes() {
        if (hourMinutes == null) {
            ArrayList<String> integers = new ArrayList<>();
            for (int i = 0; i <= 59; i++) {
                if (i < 10) {
                    integers.add("0" + String.valueOf(i));
                } else {
                    integers.add(String.valueOf(i));
                }
            }
            hourMinutes = integers.toArray(new String[integers.size()]);
        }
        return hourMinutes;
    }

    /**
     * 获取一小时中分钟数的集合 0-59
     *
     * @return String
     */
    public static String[] getHourMinutes2() {
        if (hourMinutes == null) {
            ArrayList<String> integers = new ArrayList<>();
            for (int i = 1; i <= 59; i++) {
                if (i < 10) {
                    integers.add("0" + String.valueOf(i));
                } else {
                    integers.add(String.valueOf(i));
                }
            }
            hourMinutes = integers.toArray(new String[integers.size()]);
        }
        return hourMinutes;
    }

    public static String parseTime(@IntRange(from = 0, to = 23) int hour, @IntRange(from = 0, to = 59) int minute) {
        if (hour < 0 || hour > 23) {
            hour = 0;
        }
        if (minute < 0 || minute > 59) {
            minute = 0;
        }

        DecimalFormat format = new DecimalFormat("000");
        String time = format.format(hour) + format.format(minute);
        return time;
    }

    public static String parseTime2(@IntRange(from = 0, to = 23) int hour, @IntRange(from = 0, to = 59) int minute) {
        if (hour < 0 || hour > 23) {
            hour = 0;
        }
        if (minute < 0 || minute > 59) {
            minute = 0;
        }

        DecimalFormat format = new DecimalFormat("00");
        String time = format.format(hour) + format.format(minute);
        return time;
    }

    public static String parseTime(@IntRange(from = 0, to = 23) int hour,
                                   @IntRange(from = 0, to = 59) int minute,
                                   @IntRange(from = 0, to = 59) int second) {
        if (hour < 0 || hour > 23) {
            hour = 0;
        }
        if (minute < 0 || minute > 59) {
            minute = 0;
        }
        if (second < 0 || second > 59) {
            second = 0;
        }
        DecimalFormat format = new DecimalFormat("00");
        String time = format.format(hour) + format.format(minute) + format.format(second);
        return time;
    }

    public static String parseInt(int value) {
        DecimalFormat format = new DecimalFormat("000000");
        String str = format.format(value);
        return str;
    }
}
