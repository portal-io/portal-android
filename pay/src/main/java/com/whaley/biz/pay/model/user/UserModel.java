package com.whaley.biz.pay.model.user;

import com.whaley.core.utils.StrUtil;

import java.io.Serializable;

/**
 * Created by YangZhi on 2016/8/30 14:31.
 */

public class UserModel implements Serializable {

    private String account_id;
    private String nickname;
    private String nickename;

    private AccessTokenModel accessTokenBean;
    private String deviceId;
    private boolean isLoginUser;
    private String avatar;
    private String avatarTime;//本地数据方便记录本地图片改变

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

    public String getAvatar() {
        return avatar + avatarTime;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
