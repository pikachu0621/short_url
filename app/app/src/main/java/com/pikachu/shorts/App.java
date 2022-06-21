package com.pikachu.shorts;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.pikachu.shorts.base.BaseAppManager;
import com.pikachu.shorts.cls.ShDataCls;
import com.pikachu.shorts.utils.AESCBCUtils;
import com.pikachu.shorts.utils.SharedPreferencesUtils;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class App extends Application {

    public final static String initialKey = "123456789";
    public static boolean isLogin = false;

    /////////////////////////////// 连接配置 /////////////////////////////////////////////
    public final static int  APP_CONNECTION_TIME = 100000;      // 连接超时 --- 单位s
    public final static int APP_READ_TIME = 100000;             // 读取超时 --- 单位s

    public final static String APP_KEY = "APP_KEY";
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static BaseAppManager appManager;
    public static ShDataCls shDataCls = null;

    public static Context getContext() {
        return context;
    }

    public static BaseAppManager getAppManager() {
        return appManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ////// activity
        appManager = BaseAppManager.getAppManager();
        ///// 屏幕适配
        //configUnits();
        //配置SharedPreferences
        SharedPreferencesUtils.info(context);
        //证书
        handleSSLHandshake();
    }


    public static ShDataCls readInfo(){
        ShDataCls shDataCls = SharedPreferencesUtils.readObject(APP_KEY, ShDataCls.class);
        if (shDataCls == null || shDataCls.getKey() == null || shDataCls.getKey().equals(""))
            AESCBCUtils.init(initialKey);
        else if (shDataCls.getKey() != null && !shDataCls.getKey().equals(""))
            AESCBCUtils.init(shDataCls.getKey());
        return shDataCls;
    }

    public static ShDataCls writeInfo(ShDataCls shDataCls){
        SharedPreferencesUtils.write(APP_KEY, shDataCls);
        return readInfo();
    }



    /**
     * trustAllCerts信任所有的证书
     */
    public void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception ignored) {
        }
    }





    //////////////// 屏幕适配 //////////////////////////
/*    private void configUnits() {
        AutoSizeConfig.getInstance().getUnitsManager()
                .setSupportDP(true)
                .setSupportSubunits(Subunits.MM);
    }*/













     /*


    /＼7　　　 ∠ /
    /　│　　 ／　／
    │　Z ＿,＜　／　　 /`ヽ
    │　　　　　ヽ　　 /　　〉
    Y　　　　　`　 /　　/
    ｲ●　､　●　　⊂⊃〈　　/
    （）　 へ　　　　|　＼〈
    >ｰ ､_　 ィ　 │ ／／
    / へ　　 /　ﾉ＜| ＼＼
    ヽ_ﾉ　　（_／　 │／／
    7　　　　　　　|／
    ＞―r￣￣`ｰ―＿丿😘
    吉祥物



    */


}
