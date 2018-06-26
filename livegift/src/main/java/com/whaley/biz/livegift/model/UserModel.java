package com.whaley.biz.livegift.model;

/**
 * Author: qxw
 * Date:2017/10/25
 * Introduction:
 */

public class UserModel {
    private String account_id;
    private String nickname;
    private String avatarTime;//本地数据方便记录本地图片改变
    private String avatar;


    public String getAvatar() {
        return avatar + avatarTime;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
