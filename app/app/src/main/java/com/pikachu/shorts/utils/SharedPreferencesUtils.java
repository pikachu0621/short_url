package com.pikachu.shorts.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.Set;

/**
 * author : pikachu
 * date   : 2021/7/2813:47
 * version: 1.0
 * 本地 xml储存
 *
 *
 */

public class SharedPreferencesUtils {

    private static SharedPreferences sharedPreferences;
    private static String xmlName = "APP_INFO";

    public static SharedPreferences info(Context context) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences( xmlName, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static SharedPreferences info(Context context, String xmlName) {
        SharedPreferencesUtils.xmlName = xmlName;
        return info(context);
    }



    public static void write(String key, String var) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, var);
        edit.apply();
    }

    public static void write(String key, int var) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, var);
        edit.apply();
    }

    public static void write(String key, float var) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putFloat(key, var);
        edit.apply();
    }

    public static void write(String key, long var) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong(key, var);
        edit.apply();
    }

    public static void write(String key, boolean var) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, var);
        edit.apply();
    }

    public static void write(String key, Set<String> values) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putStringSet(key, values);
        edit.apply();
    }

    public static void write(String key, Object values) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Gson gson = new Gson();
        String s = gson.toJson(values);
        edit.putString(key, s);
        edit.apply();
    }


    public static String readString(String key) {
        return readString(key, null);
    }
    public static int readInt(String key) {
        return readInt(key, -1);
    }
    public static float readFloat(String key) {
        return readFloat(key, -1);
    }
    public static long readLong(String key) {
        return readLong(key, -1);
    }
    public static boolean readBoolean(String key) {
        return readBoolean(key, false);
    }
    public static Set<String> readStrings(String key) {
        return readStrings(key, null);
    }
    public static <T> T readObject(String key, Class<T> cls) {
        String string = readString(key);
        if (string == null || string.equals(""))
            return null;
        Gson gson = new Gson();
        return gson.fromJson(string, cls);
    }


    public static String readString(String key, String v) {
        return sharedPreferences.getString(key, v);
    }
    public static int readInt(String key, int v) {
        return sharedPreferences.getInt(key, v);
    }
    public static float readFloat(String key, float v) {
        return sharedPreferences.getFloat(key, v);
    }
    public static long readLong(String key, long v) {
        return sharedPreferences.getLong(key, v);
    }
    public static boolean readBoolean(String key, boolean v) {
        return sharedPreferences.getBoolean(key, v);
    }
    public static Set<String> readStrings(String key, Set<String> v) {
        return sharedPreferences.getStringSet(key, v);
    }





}
