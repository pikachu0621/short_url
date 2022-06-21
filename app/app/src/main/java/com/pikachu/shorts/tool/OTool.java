package com.pikachu.shorts.tool;

import com.pikachu.shorts.cls.DataCls;
import com.pikachu.shorts.utils.AESCBCUtils;
import com.pikachu.shorts.utils.LogsUtils;

/**
 * @author Pikachu
 * @Project PK短链
 * @Package com.pikachu.shorts.tool
 * @Date 2021/8/15 ( 下午 5:29 )
 * @description
 */
public class OTool {

    private static String startStr(int type, String userMd5){
      return   "{\"type\":"+type+", \"user_md5\":\"" + userMd5 +"\", \"time\":"+(System.currentTimeMillis());
    }


    // 0 登录
    public static String getLoginStr(String userMd5){
        return  AESCBCUtils.encrypt(startStr(0, userMd5) + "}");
    }


    // 1 生成短链
    public static String getShortStr(String userMd5, String longUrl){
        return  AESCBCUtils.encrypt(startStr(1, userMd5) + ",\"long_url\":\""+longUrl+"\"}");
    }

    // 2  启用/禁用
    public static String getOffAndOnStr(String userMd5, String shortUrl){
        return  AESCBCUtils.encrypt(startStr(2, userMd5) + ",\"short_url\":\""+shortUrl+"\"}");
    }


    // 3  查看访问量
    public static String getLookNumStr(String userMd5, String shortUrl){
        return  AESCBCUtils.encrypt(startStr(3, userMd5) + ",\"short_url\":\""+shortUrl+"\"}");
    }

    //4   编辑      id = 0    $long_url - $short_url - $is_open - $is_open_win - $amount - $user_md5  - $time
    public static String getEditShortStr(String userMd5, long id, String longUrl, String shortUrl, boolean isOpen, boolean isOpenWin, long amount){
        return  AESCBCUtils.encrypt(startStr(4, userMd5) + ", \"id\":"+id+", \"long_url\":\""+longUrl+"\" , \"short_url\":\""+shortUrl+
                "\", \"is_open\":"+(isOpen?1:0)+", \"is_open_win\":"+(isOpenWin?1:0)+", \"amount\":"+amount+"}");
    }

    ///5 读取本短链全部数据
    public static String getLookOneShortStr(String userMd5, String shortUrl){
        return  AESCBCUtils.encrypt(startStr(5, userMd5) + ",\"short_url\":\""+shortUrl+"\"}");
    }


    ///6 读取全部数据
    public static String getHistoryShortStr(String userMd5, int page, int num){
        return  AESCBCUtils.encrypt(startStr(6, userMd5) + ",\"page\":"+page+", \"num\":"+num+"}");
    }


    ///7 读取全部配置数据
    public static String getLookInfoStr(String userMd5){
        return  AESCBCUtils.encrypt(startStr(7, userMd5) +"}");
    }


    ///8 修改配置数据
    public static String getEditInfoStr(String userMd5, DataCls.InfoBean infoCls){


        String json = startStr(8, userMd5);
        json += ", \"pk_servername\": \"%s\"," +
                "  \"pk_username\": \"%s\"," +
                "  \"pk_password\": \"%s\"," +
                "  \"pk_data\": \"%s\"," +
                "  \"pk_data_table\": \"%s\"," +
                "  \"pk_key_len\": %s," +
                "  \"pk_err_url\": \"%s\"," +
                "  \"pk_open_url\": \"%s\"," +
                "  \"pk_debug\": %s," +
                "  \"pk_time\": %s," +
                "  \"pk_key\": \"%s\"," +
                "  \"pk_is_htaccess\": %s," +
                "  \"pk_is_open\": %s," +
                "  \"pk_root_user\": \"%s\"," +
                "  \"pk_root_pass\": \"%s\"" + "}";

        String format = String.format(json, infoCls.getPk_servername(), infoCls.getPk_username(), infoCls.getPk_password(),
                infoCls.getPk_data(), infoCls.getPk_data_table(), infoCls.getPk_key_len(), infoCls.getPk_err_url(),
                infoCls.getPk_open_url(), infoCls.getPk_debug(), infoCls.getPk_time(), infoCls.getPk_key(), infoCls.getPk_is_htaccess(),
                infoCls.getPk_is_open(), infoCls.getPk_root_user(), infoCls.getPk_root_pass());

        //LogsUtils.showLog(format);
        return  AESCBCUtils.encrypt(format);
    }


    // 9  删除短链
    public static String getDeleteShortStr(String userMd5, String shortUrl){
        return  AESCBCUtils.encrypt(startStr(9, userMd5) + ",\"short_url\":\""+shortUrl+"\"}");
    }





}
