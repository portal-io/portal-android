package com.whaleyvr.biz.danmu.model;

import com.whaley.core.utils.StrUtil;

import java.io.Serializable;

/**
 * Created by YangZhi on 2016/8/30 14:31.
 */

public class UserModel implements Serializable {


    /**
     * nickname : 113
     * email_security :
     * email_status : 1
     * email_pwd : null
     * gender : 1
     * gender_detail :
     * birthday :
     * area :
     * bandwidth :
     * mobile : 001****0680
     * avatar : http://phenix.moretv104.com.cn/moretv?acid=22101757&width=0&height=0&retate =0
     * is_moretv : 1
     * wb :
     * qq :
     * db :
     * wx :
     * tb :
     * ncode : 86
     */

    private String account_id;
    private String accountid;
    private String nickname;
    private String nickename;
    private String email_security;
    private String email_status;
    private Object email_pwd;
    private String gender;
    private String gender_detail;
    private String birthday;
    private String area;
    private String bandwidth;
    private String mobile;
    private String avatar;
    private String is_moretv;
    private String wb;
    private String qq;
    private String db;
    private String wx;
    private String tb;
    private String ncode;
    private String openid;
    private String unionid;
    private String orgin;
    private String third_id;
    private boolean isAddInformation = false;
    private AccessTokenBean accessTokenBean;
    private String deviceId;
    private boolean isLoginUser;


    public String getNickname() {
        if (StrUtil.isEmpty(nickname)) {
            nickname = nickename;
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        this.nickename = nickname;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWb() {
        return wb;
    }

    public void setWb(String wb) {
        this.wb = wb;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getTb() {
        return tb;
    }

    public void setTb(String tb) {
        this.tb = tb;
    }

}
