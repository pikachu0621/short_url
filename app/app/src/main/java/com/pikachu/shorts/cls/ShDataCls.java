package com.pikachu.shorts.cls;

import java.io.Serializable;

/**
 * @author Pikachu
 * @Project PK短链
 * @Package com.pikachu.shorts.cls
 * @Date 2021/8/15 ( 下午 12:08 )
 * @description 保存数据到本地
 */
public class ShDataCls implements Serializable {
    private String appUrl;
    private String userMd5;
    private String key;


    public ShDataCls(String appUrl, String userMd5, String key) {
        this.appUrl = appUrl;
        this.userMd5 = userMd5;
        this.key = key;
    }

    public ShDataCls() {
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getUserMd5() {
        return userMd5;
    }

    public void setUserMd5(String userMd5) {
        this.userMd5 = userMd5;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public boolean isAppUrl(){
        return appUrl == null || appUrl.equals("") ;
    }

    public boolean isUserMd5(){
        return userMd5 == null || userMd5.equals("") ;
    }

    public boolean isKey(){
        return key == null || key.equals("") ;
    }
}
