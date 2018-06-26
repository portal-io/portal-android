package com.whaleyvr.biz.danmu.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.VersionUtil;

/**
 * Created by dell on 2017/5/8.
 */

public class DmModel implements Parcelable {

    private int roomid;
    private int timestamp;
    private String message;

    private String systemname = "android";
    private String systemver = AppUtil.getVersionRelease();
    private String appname = "WhaleyVR";
    private String appver = VersionUtil.getVersionName();
    private String appvercode = "" + VersionUtil.getVersionCode();
    private String sign;

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSystemname() {
        return systemname;
    }

    public void setSystemname(String systemname) {
        this.systemname = systemname;
    }

    public String getSystemver() {
        return systemver;
    }

    public void setSystemver(String systemver) {
        this.systemver = systemver;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAppver() {
        return appver;
    }

    public void setAppver(String appver) {
        this.appver = appver;
    }

    public String getAppvercode() {
        return appvercode;
    }

    public void setAppvercode(String appvercode) {
        this.appvercode = appvercode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.roomid);
        dest.writeInt(this.timestamp);
        dest.writeString(this.message);
        dest.writeString(this.systemname);
        dest.writeString(this.systemver);
        dest.writeString(this.appname);
        dest.writeString(this.appver);
        dest.writeString(this.appvercode);
        dest.writeString(this.sign);
    }

    public DmModel() {
    }

    protected DmModel(Parcel in) {
        this.roomid = in.readInt();
        this.timestamp = in.readInt();
        this.message = in.readString();
        this.systemname = in.readString();
        this.systemver = in.readString();
        this.appname = in.readString();
        this.appver = in.readString();
        this.appvercode = in.readString();
        this.sign = in.readString();
    }

    public static final Creator<DmModel> CREATOR = new Creator<DmModel>() {
        public DmModel createFromParcel(Parcel source) {
            return new DmModel(source);
        }

        public DmModel[] newArray(int size) {
            return new DmModel[size];
        }
    };
}
