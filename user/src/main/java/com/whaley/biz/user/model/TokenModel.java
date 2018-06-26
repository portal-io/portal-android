package com.whaley.biz.user.model;

/**
 * Created by yangzhi on 16/9/1.
 */
public class TokenModel {

    private String sms_token;

    private String expiration_time;

    private String now_time;


    public String getSms_token() {
        return sms_token;
    }

    public void setSms_token(String sms_token) {
        this.sms_token = sms_token;
    }

    public String getExpiration_time() {
        return expiration_time;
    }

    public void setExpiration_time(String expiration_time) {
        this.expiration_time = expiration_time;
    }

    public String getNow_time() {
        return now_time;
    }

    public void setNow_time(String now_time) {
        this.now_time = now_time;
    }
}
