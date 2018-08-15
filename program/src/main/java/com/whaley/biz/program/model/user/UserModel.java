package com.whaley.biz.program.model.user;

import com.whaley.core.utils.StrUtil;

import java.io.Serializable;

/**
 * Created by YangZhi on 2016/8/30 14:31.
 */

public class UserModel implements Serializable {

    private String account_id;
    private String nickname;
    private String nickename;
    private String mobile;

    private AccessTokenModel accessTokenBean;
    private String deviceId;
    private boolean isLoginUser;

    private String portalAccessToken;
    private String portalAddress;

    public String getNickename() {
        if (StrUtil.isEmpty(nickename)) {
            nickename = nickname;
        }
        return nickename;
    }


    public void setNickename(String nickename) {
        this.nickename = nickename;
        this.nickname = nickename;
    }

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

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }


    public void setLoginUser(boolean loginUser) {
        isLoginUser = loginUser;
    }

    public boolean isLoginUser() {
        return isLoginUser;
    }

    public AccessTokenModel getAccessTokenModel() {
        return accessTokenBean;
    }

    public void setAccessTokenModel(AccessTokenModel accessTokenModel) {
        this.accessTokenBean = accessTokenModel;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPortalAccessToken() {
        return portalAccessToken;
    }

    public void setPortalAccessToken(String portalAccessToken) {
        this.portalAccessToken = portalAccessToken;
    }

    public String getPortalAddress() {
        return portalAddress;
    }

    public void setPortalAddress(String portalAddress) {
        this.portalAddress = portalAddress;
    }
}
