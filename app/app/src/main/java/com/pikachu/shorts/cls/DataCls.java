package com.pikachu.shorts.cls;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Pikachu
 * @Project PK短链
 * @Package com.pikachu.shorts.cls
 * @Date 2021/8/14 ( 上午 1:50 )
 * @description 返回数据解析
 */
public class DataCls {


    private Integer code;
    private String msg;
    @SerializedName("short")
    private String shortX;
    private Integer amount;
    private List<DataBean> data;
    private InfoBean info;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getShortX() {
        return shortX;
    }

    public void setShortX(String shortX) {
        this.shortX = shortX;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        private String pk_servername;
        private String pk_username;
        private String pk_password;
        private String pk_data;
        private String pk_data_table;
        private Integer pk_key_len;
        private String pk_err_url;
        private String pk_open_url;
        private Boolean pk_debug;
        private Integer pk_time;
        private String pk_key;
        private Boolean pk_is_htaccess;
        private Boolean pk_is_open;
        private String pk_root_user;
        private String pk_root_pass;


        public boolean isContrast(InfoBean infoBean) {

            return pk_servername.equals(infoBean.getPk_servername()) &&
                    pk_username.equals(infoBean.getPk_username()) &&
                    pk_password.equals(infoBean.getPk_password()) &&
                    pk_data.equals(infoBean.getPk_data()) &&
                    pk_data_table.equals(infoBean.getPk_data_table()) &&
                    pk_key_len.equals(infoBean.getPk_key_len()) &&
                    pk_err_url.equals(infoBean.getPk_err_url()) &&
                    pk_open_url.equals(infoBean.getPk_open_url()) &&
                    pk_debug == infoBean.getPk_debug() &&
                    pk_time.equals(infoBean.getPk_time()) &&
                    pk_key.equals(infoBean.getPk_key()) &&
                    pk_is_htaccess == infoBean.getPk_is_htaccess() &&
                    pk_is_open == infoBean.getPk_is_open() &&
                    pk_root_user.equals(infoBean.getPk_root_user()) &&
                    pk_root_pass.equals(infoBean.getPk_root_pass());

        }


        public String getPk_servername() {
            return pk_servername;
        }

        public void setPk_servername(String pk_servername) {
            this.pk_servername = pk_servername;
        }

        public String getPk_username() {
            return pk_username;
        }

        public void setPk_username(String pk_username) {
            this.pk_username = pk_username;
        }

        public String getPk_password() {
            return pk_password;
        }

        public void setPk_password(String pk_password) {
            this.pk_password = pk_password;
        }

        public String getPk_data() {
            return pk_data;
        }

        public void setPk_data(String pk_data) {
            this.pk_data = pk_data;
        }

        public String getPk_data_table() {
            return pk_data_table;
        }

        public void setPk_data_table(String pk_data_table) {
            this.pk_data_table = pk_data_table;
        }

        public Integer getPk_key_len() {
            return pk_key_len;
        }

        public void setPk_key_len(Integer pk_key_len) {
            this.pk_key_len = pk_key_len;
        }

        public String getPk_err_url() {
            return pk_err_url;
        }

        public void setPk_err_url(String pk_err_url) {
            this.pk_err_url = pk_err_url;
        }

        public String getPk_open_url() {
            return pk_open_url;
        }

        public void setPk_open_url(String pk_open_url) {
            this.pk_open_url = pk_open_url;
        }

        public Boolean getPk_debug() {
            return pk_debug;
        }

        public void setPk_debug(Boolean pk_debug) {
            this.pk_debug = pk_debug;
        }

        public Integer getPk_time() {
            return pk_time;
        }

        public void setPk_time(Integer pk_time) {
            this.pk_time = pk_time;
        }

        public String getPk_key() {
            return pk_key;
        }

        public void setPk_key(String pk_key) {
            this.pk_key = pk_key;
        }

        public boolean getPk_is_htaccess() {
            return pk_is_htaccess;
        }

        public void setPk_is_htaccess(Boolean pk_is_htaccess) {
            this.pk_is_htaccess = pk_is_htaccess;
        }

        public Boolean getPk_is_open() {
            return pk_is_open;
        }

        public void setPk_is_open(Boolean pk_is_open) {
            this.pk_is_open = pk_is_open;
        }

        public String getPk_root_user() {
            return pk_root_user;
        }

        public void setPk_root_user(String pk_root_user) {
            this.pk_root_user = pk_root_user;
        }

        public String getPk_root_pass() {
            return pk_root_pass;
        }

        public void setPk_root_pass(String pk_root_pass) {
            this.pk_root_pass = pk_root_pass;
        }
    }

    public static class DataBean {
        private Integer id;
        private String long_url;
        private String short_url;
        private Integer is_win;
        private Integer is_open;
        private Integer url_visits;
        private String create_time;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLong_url() {
            return long_url;
        }

        public void setLong_url(String long_url) {
            this.long_url = long_url;
        }

        public String getShort_url() {
            return short_url;
        }

        public void setShort_url(String short_url) {
            this.short_url = short_url;
        }

        public Integer getIs_win() {
            return is_win;
        }

        public void setIs_win(Integer is_win) {
            this.is_win = is_win;
        }

        public Integer getIs_open() {
            return is_open;
        }

        public void setIs_open(Integer is_open) {
            this.is_open = is_open;
        }

        public Integer getUrl_visits() {
            return url_visits;
        }

        public void setUrl_visits(Integer url_visits) {
            this.url_visits = url_visits;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
