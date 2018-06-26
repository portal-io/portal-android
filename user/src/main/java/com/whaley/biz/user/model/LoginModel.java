package com.whaley.biz.user.model;

import com.whaley.core.utils.StrUtil;

/**
 * Created by YangZhi on 2016/9/8 11:45.
 */
public class LoginModel {

    /**
     * reg_type : new
     * username :
     * heliosid :
     * mobile : 18917295891
     * avatar : http://image.aginomoto.com/whaley?acid=&width=0&height=0&retate=0&t=1473306167
     */

    private String reg_type;
    private String username;
    private String heliosid;
    private String mobile;
    private String avatar;
    private String nick_is_repetitive;
    private String accesstoken;
    private String refreshtoken;
    private String expiretime;
    private String third_id;
    private String unionid;

    public boolean isNewUser() {
        return (!StrUtil.isEmpty(reg_type) && reg_type.equals("new")) || StrUtil.isEmpty(username) || (!StrUtil.isEmpty(username) && username.startsWith("vr_"));
    }

    public String getNick_is_repetitive() {
        return nick_is_repetitive;
    }

    public void setNick_is_repetitive(String nick_is_repetitive) {
        this.nick_is_repetitive = nick_is_repetitive;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    public String getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(String expiretime) {
        this.expiretime = expiretime;
    }

    public String getReg_type() {
        return reg_type;
    }

    public void setReg_type(String reg_type) {
        this.reg_type = reg_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeliosid() {
        return heliosid;
    }

    public void setHeliosid(String heliosid) {
        this.heliosid = heliosid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getThird_id() {
        return third_id;
    }

    public void setThird_id(String third_id) {
        this.third_id = third_id;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public UserModel convertToUserBean() {
        UserModel userModel = new UserModel();
        userModel.setAccount_id(getHeliosid());
        userModel.setAvatar(getAvatar());
        userModel.setMobile(getMobile());
        userModel.setNickname(getUsername());
        setAccessTokenBean(userModel);
        return userModel;
    }

    public UserModel convertToUserBean(UserModel userModel) {
        if (userModel != null) {
            userModel.setAccount_id(getHeliosid());
            userModel.setAvatar(getAvatar());
            userModel.setMobile(getMobile());
            userModel.setNickname(getUsername());
            setAccessTokenBean(userModel);
            return userModel;
        } else {
            return convertToUserBean();
        }

    }

    public void setAccessTokenBean(UserModel userModel) {
        if (!StrUtil.isEmpty(getAccesstoken()))
            userModel.setAccessTokenModel(new AccessTokenModel(getAccesstoken(), getRefreshtoken(), getExpiretime()));
    }
}
