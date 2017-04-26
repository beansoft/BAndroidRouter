package com.github.beansoftapp.android.router.util;

import android.util.Log;

/**
 * Log util class.
 */
public class LogUtil {
    public static boolean sIsLogEnable = false;// default no log

    private LogUtil() {
    }

    public static void setLogEnabled(boolean enableLog) {
        sIsLogEnable = enableLog;
    }

    public static void enableLog() {
        sIsLogEnable = true;
    }

    public static void disableLog() {
        sIsLogEnable = false;
    }

    public static void d(String tag, String msg) {
        if(sIsLogEnable) {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") " + stackTrace.getMethodName();
            Log.d(tag, fileInfo + ": " + msg);
        }

    }

    public static void i(String tag, String msg) {
        if(sIsLogEnable) {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") " + stackTrace.getMethodName();
            Log.i(tag, fileInfo + ": " + msg);
        }

    }

    public static void e(String tag, String msg) {
        if(sIsLogEnable) {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") " + stackTrace.getMethodName();
            Log.e(tag, fileInfo + ": " + msg);
        }

    }

    public static void e(String tag, String msg, Throwable e) {
        if(sIsLogEnable) {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") " + stackTrace.getMethodName();
            Log.e(tag, fileInfo + ": " + msg, e);
        }

    }

    public static void w(String tag, String msg) {
        if(sIsLogEnable) {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") " + stackTrace.getMethodName();
            Log.w(tag, fileInfo + ": " + msg);
        }

    }

    public static void v(String tag, String msg) {
        if(sIsLogEnable) {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") " + stackTrace.getMethodName();
            Log.v(tag, fileInfo + ": " + msg);
        }

    }

    public static String getStackTraceMsg() {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
        String fileInfo = stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") " + stackTrace.getMethodName();
        return fileInfo;
    }
}
