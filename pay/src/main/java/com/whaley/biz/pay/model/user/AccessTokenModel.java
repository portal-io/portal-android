package com.whaley.biz.pay.model.user;

import java.io.Serializable;

/**
 * Created by dell on 2017/8/4.
 */

public class AccessTokenModel implements Serializable {

    private String accesstoken;
    private String refreshtoken;
    private String expiretime;

    public AccessTokenModel() {

    }

    public AccessTokenModel(String accesstoken, String refreshtoken, String expiretime) {
        this.accesstoken = accesstoken;
        this.refreshtoken = refreshtoken;
        this.expiretime = expiretime;
    }

    public String getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(String expiretime) {
        this.expiretime = expiretime;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }
}