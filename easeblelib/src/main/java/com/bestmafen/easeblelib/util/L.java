package com.bestmafen.easeblelib.util;

import android.util.Log;

/**
 * This class is used to be a Logcat helper.
 *
 * @author xiaokai
 */
public final class L {
    private static final int LEVEL_VERBOSE = 1;
    private static final int LEVEL_DEBUG = 2;
    private static final int LEVEL_INFO = 3;
    private static final int LEVEL_WARN = 4;
    private static final int LEVEL_ERROR = 5;

    /**
     * The lowest level will be logged.
     */
    private static final int LEVEL = LEVEL_VERBOSE;

    /**
     * The default tag.
     */
    private static final String TAG = "Alarm";

    /**
     * Log.v().
     *
     * @param msg The message you would like logged.
     */
    public static void v(String msg) {
        if (LEVEL <= LEVEL_VERBOSE) {
            Log.v(TAG, msg);
        }
    }

    /**
     * Log.d().
     *
     * @param msg The message you would like logged.
     */
    public static void d(String msg) {
        if (LEVEL <= LEVEL_DEBUG) {
            Log.d(TAG, msg);
        }
    }

    /**
     * Log.i().
     *
     * @param msg The message you would like logged.
     */
    public static void i(String msg) {
        if (LEVEL <= LEVEL_INFO) {
            Log.i(TAG, msg);
        }
    }

    /**
     * Log.w().
     *
     * @param msg The message you would like logged.
     */
    public static void w(String msg) {
        if (LEVEL <= LEVEL_WARN) {
            Log.w(TAG, msg);
        }
    }

    /**
     * Log.e().
     *
     * @param msg The message you would like logged.
     */
    public static void e(String msg) {
        if (LEVEL <= LEVEL_ERROR) {
            Log.e(TAG, msg);
        }
    }

    /**
     * Log.v().
     *
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        if (LEVEL <= LEVEL_VERBOSE) {
            Log.v(tag, msg);
        }
    }

    /**
     * Log.d().
     *
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if (LEVEL <= LEVEL_DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     * Log.i().
     *
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        if (LEVEL <= LEVEL_INFO) {
            Log.i(tag, msg);
        }
    }

    /**
     * Log.w().
     *
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        if (LEVEL <= LEVEL_WARN) {
            Log.w(tag, msg);
        }
    }

    /**
     * Log.e().
     *
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        if (LEVEL <= LEVEL_ERROR) {
            Log.e(tag, msg);
        }
    }
}
