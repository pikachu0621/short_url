package com.pikachu.shorts.utils;

import android.util.Log;


public class LogsUtils {

    public static String APP_LOG = "TEST_TT";


    public static void showLog(String tag, String msg) {
        if (tag == null)
            tag = APP_LOG;
        if (msg == null)
            msg = "无消息";
        Log.d(tag, msg);
    }

    public static void showLog(int msg) {
        showLog(null, msg + "");
    }

    public static void showLog(long msg) {
        showLog(null, msg + "");
    }

    public static void showLog(float msg) {
        showLog(null, msg + "");
    }

    public static void showLog(int[] msg) {
        showLog(null, msg.toString());
    }

    public static void showLog(Object msg) {
        showLog(null, msg + "");
    }


}
