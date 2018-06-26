package com.whaleyvr.biz.danmu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/5/8.
 */

public class AuthModel implements Parcelable {


    /**
     * uid : 600
     * mobile :
     * email :
     * nickname : 游客600
     * text :
     * gender : 0
     * avatar :
     * level : 1
     * exp : 0
     * regtime : 1494317499
     * regip : 106.75.84.232
     * lasttime : 1494317499
     * lastip : 106.75.84.232
     * credits1 : 0
     * credits2 : 0
     * whaleyuid :
     * userupdatetime : 1494317499
     * device_id : 1
     * model : 1
     */

    private String uid;
    private String mobile;
    private String email;
    private String nickname;
    private String text;
    private String gender;
    private String avatar;
    private String level;
    private String exp;
    private String regtime;
    private String regip;
    private String lasttime;
    private String lastip;
    private String credits1;
    private String credits2;
    private String whaleyuid;
    private int userupdatetime;
    private String device_id;
    private String model;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public void setRegtime(String regtime) {
        this.regtime = regtime;
    }

    public void setRegip(String regip) {
        this.regip = regip;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    public void setLastip(String lastip) {
        this.lastip = lastip;
    }

    public void setCredits1(String credits1) {
        this.credits1 = credits1;
    }

    public void setCredits2(String credits2) {
        this.credits2 = credits2;
    }

    public void setWhaleyuid(String whaleyuid) {
        this.whaleyuid = whaleyuid;
    }

    public void setUserupdatetime(int userupdatetime) {
        this.userupdatetime = userupdatetime;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUid() {
        return uid;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getText() {
        return text;
    }

    public String getGender() {
        return gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLevel() {
        return level;
    }

    public String getExp() {
        return exp;
    }

    public String getRegtime() {
        return regtime;
    }

    public String getRegip() {
        return regip;
    }

    public String getLasttime() {
        return lasttime;
    }

    public String getLastip() {
        return lastip;
    }

    public String getCredits1() {
        return credits1;
    }

    public String getCredits2() {
        return credits2;
    }

    public String getWhaleyuid() {
        return whaleyuid;
    }

    public int getUserupdatetime() {
        return userupdatetime;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getModel() {
        return model;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.mobile);
        dest.writeString(this.email);
        dest.writeString(this.nickname);
        dest.writeString(this.text);
        dest.writeString(this.gender);
        dest.writeString(this.avatar);
        dest.writeString(this.level);
        dest.writeString(this.exp);
        dest.writeString(this.regtime);
        dest.writeString(this.regip);
        dest.writeString(this.lasttime);
        dest.writeString(this.lastip);
        dest.writeString(this.credits1);
        dest.writeString(this.credits2);
        dest.writeString(this.whaleyuid);
        dest.writeInt(this.userupdatetime);
        dest.writeString(this.device_id);
        dest.writeString(this.model);
    }

    public AuthModel() {
    }

    protected AuthModel(Parcel in) {
        this.uid = in.readString();
        this.mobile = in.readString();
        this.email = in.readString();
        this.nickname = in.readString();
        this.text = in.readString();
        this.gender = in.readString();
        this.avatar = in.readString();
        this.level = in.readString();
        this.exp = in.readString();
        this.regtime = in.readString();
        this.regip = in.readString();
        this.lasttime = in.readString();
        this.lastip = in.readString();
        this.credits1 = in.readString();
        this.credits2 = in.readString();
        this.whaleyuid = in.readString();
        this.userupdatetime = in.readInt();
        this.device_id = in.readString();
        this.model = in.readString();
    }

    public static final Creator<AuthModel> CREATOR = new Creator<AuthModel>() {
        public AuthModel createFromParcel(Parcel source) {
            return new AuthModel(source);
        }

        public AuthModel[] newArray(int size) {
            return new AuthModel[size];
        }
    };
}
