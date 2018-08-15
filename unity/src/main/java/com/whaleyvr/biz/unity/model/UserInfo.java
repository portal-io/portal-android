package com.whaleyvr.biz.unity.model;

/**
 * Created by dell on 2016/11/28.
 */

public class UserInfo {

    private long mwid;
    private String nickname;
    private String access_token;
    private String device_id;
    private String avatar;

    public UserInfo(){

    }

    public UserInfo(long mwid, String nickname, String access_token, String device_id, String avater){
        this.mwid = mwid;
        this.nickname = nickname;
        this.access_token = access_token;
        this.device_id = device_id;
        this.avatar = avater;
    }

    public long getMwid() {
        return mwid;
    }

    public void setMwid(long mwid) {
        this.mwid = mwid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
