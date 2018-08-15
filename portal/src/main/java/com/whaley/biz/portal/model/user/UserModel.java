package com.whaley.biz.portal.model.user;

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
    private int is_upload_avatar;
    private String avatarTime;//本地数据方便记录本地图片改变

    private String portalAccessToken;
    private String portalAddress;

    public String getNickename() {
        if (StrUtil.isEmpty(nickename)) {
            nickename = nickname;
        }
        return nickename;
    }

    public int getIs_upload_avatar() {
        return is_upload_avatar;
    }

    public void setIs_upload_avatar(int is_upload_avatar) {
        this.is_upload_avatar = is_upload_avatar;
    }

    public String getThird_id() {
        return third_id;
    }

    public void setThird_id(String third_id) {
        this.third_id = third_id;
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

    public String getEmail_security() {
        return email_security;
    }

    public void setEmail_security(String email_security) {
        this.email_security = email_security;
    }

    public String getEmail_status() {
        return email_status;
    }

    public void setEmail_status(String email_status) {
        this.email_status = email_status;
    }

    public Object getEmail_pwd() {
        return email_pwd;
    }

    public void setEmail_pwd(Object email_pwd) {
        this.email_pwd = email_pwd;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender_detail() {
        return gender_detail;
    }

    public void setGender_detail(String gender_detail) {
        this.gender_detail = gender_detail;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar + avatarTime;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIs_moretv() {
        return is_moretv;
    }

    public void setIs_moretv(String is_moretv) {
        this.is_moretv = is_moretv;
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

    public String getNcode() {
        return ncode;
    }

    public void setNcode(String ncode) {
        this.ncode = ncode;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }


    public String getOrgin() {
        return orgin;
    }

    public void setOrgin(String orgin) {
        this.orgin = orgin;
    }

    public boolean isAddInformation() {
        return isAddInformation;
    }

    public void setAddInformation(boolean addInformation) {
        isAddInformation = addInformation;
    }

    public void setLoginUser(boolean loginUser) {
        isLoginUser = loginUser;
    }

    public boolean isLoginUser() {
        return isLoginUser;
    }

    public AccessTokenBean getAccessTokenBean() {
        return accessTokenBean;
    }

    public void setAccessTokenBean(AccessTokenBean accessTokenBean) {
        this.accessTokenBean = accessTokenBean;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
