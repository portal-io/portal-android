package com.whaley.biz.user.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.core.utils.StrUtil;


/**
 * Created by YangZhi on 2016/8/30 14:31.
 */

public class UserModel implements Parcelable {


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
    private String email_pwd;
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
    private AccessTokenModel accessTokenBean;
    private String deviceId;
    private int is_upload_avatar;

    private String portalAccessToken;
    private String portalAddress;

    private String avatarTime;//本地数据方便记录本地图片改变

    public int getIs_upload_avatar() {
        return is_upload_avatar;
    }

    public void setIs_upload_avatar(int is_upload_avatar) {
        this.is_upload_avatar = is_upload_avatar;
    }

    public String getAvatarTime() {
        return avatarTime;
    }

    public void setAvatarTime(long avatarTime) {
        this.avatarTime = "&time=" + avatarTime;
    }

    public void setAvatarTime(String avatarTime) {
        this.avatarTime = avatarTime;
    }

    public String getNickename() {
        if (StrUtil.isEmpty(nickename)) {
            nickename = nickname;
        }
        return nickename;
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

    public String getEmail_pwd() {
        return email_pwd;
    }

    public void setEmail_pwd(String email_pwd) {
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

    public void changeToAddInformation() {
        if (StrUtil.isEmpty(getNickname()) || getNickname().startsWith("vr_")) {
            setAddInformation(true);
        } else {
            setAddInformation(false);
        }
    }

    public UserModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.account_id);
        dest.writeString(this.accountid);
        dest.writeString(this.nickname);
        dest.writeString(this.nickename);
        dest.writeString(this.email_security);
        dest.writeString(this.email_status);
        dest.writeString(this.email_pwd);
        dest.writeString(this.gender);
        dest.writeString(this.gender_detail);
        dest.writeString(this.birthday);
        dest.writeString(this.area);
        dest.writeString(this.bandwidth);
        dest.writeString(this.mobile);
        dest.writeString(this.avatar);
        dest.writeString(this.is_moretv);
        dest.writeString(this.wb);
        dest.writeString(this.qq);
        dest.writeString(this.db);
        dest.writeString(this.wx);
        dest.writeString(this.tb);
        dest.writeString(this.ncode);
        dest.writeString(this.openid);
        dest.writeString(this.unionid);
        dest.writeString(this.orgin);
        dest.writeString(this.third_id);
        dest.writeByte(this.isAddInformation ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.accessTokenBean);
        dest.writeString(this.deviceId);
        dest.writeString(this.portalAccessToken);
        dest.writeString(this.portalAddress);
    }

    protected UserModel(Parcel in) {
        this.account_id = in.readString();
        this.accountid = in.readString();
        this.nickname = in.readString();
        this.nickename = in.readString();
        this.email_security = in.readString();
        this.email_status = in.readString();
        this.email_pwd = in.readString();
        this.gender = in.readString();
        this.gender_detail = in.readString();
        this.birthday = in.readString();
        this.area = in.readString();
        this.bandwidth = in.readString();
        this.mobile = in.readString();
        this.avatar = in.readString();
        this.is_moretv = in.readString();
        this.wb = in.readString();
        this.qq = in.readString();
        this.db = in.readString();
        this.wx = in.readString();
        this.tb = in.readString();
        this.ncode = in.readString();
        this.openid = in.readString();
        this.unionid = in.readString();
        this.orgin = in.readString();
        this.third_id = in.readString();
        this.isAddInformation = in.readByte() != 0;
        this.accessTokenBean = (AccessTokenModel) in.readSerializable();
        this.deviceId = in.readString();
        this.portalAccessToken = in.readString();
        this.portalAddress = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

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
