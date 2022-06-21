package com.pikachu.shorts.utils;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Pikachu
 * @Project pk短链
 * @Package com.pikachu.shorts
 * @Date 2021/8/10 ( 下午 5:17 )
 * @description
 */
public class OtherUtils {


    public static int random(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static int random(int max) {
        return random(1, max);
    }

    public static int random() {
        return random(1, 100);
    }


    // 判断手机号码是否规则
    public static boolean isPhoneNumber(String input) {
        String regex = "(1[0-9][0-9]|15[0-9]|18[0-9])\\d{8}";
        //Pattern p = Pattern.compile(regex);
        return Pattern.matches(regex, input);

    }


    /**
     * 是否有此权限
     *
     * @param context
     * @param authority
     * @return
     */
    public static boolean isAuthority(Context context, String authority) {
        return context.checkCallingOrSelfPermission(authority) == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * 是否有 查看使用量权限
     *
     * @param context
     * @return
     */
    @SuppressLint("ObsoleteSdkInt")
    public static boolean hasPermission(Context context) {
        AppOpsManager appOpsM = (AppOpsManager)
                context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mode = appOpsM.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), context.getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }


    /**
     * 是否为网址 只检测开头
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        String regex = "^http(s)?://.*?$";
        return Pattern.matches(regex, url);
    }


    //复制
    public static void copy(Context context, String data) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）,其他的还有
        // newHtmlText、
        // newIntent、
        // newUri、
        // newRawUri
        ClipData clipData = ClipData.newPlainText(null, data);
        clipboard.setPrimaryClip(clipData);
    }


    public static boolean isSpecialChar(String str) {
        String regex = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }

    /**
     * 跳转QQ聊天界面
     */
    public static void jumpQQ(Context context, String qqNum) throws Exception {
        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum;
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @SuppressLint("QueryPermissionsNeeded")
    public static  void jumpBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            intent.resolveActivity(context.getPackageManager());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        }
    }




}
