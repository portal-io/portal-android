package com.whaley.biz.setting.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PrizeDataModel implements Parcelable {

    private String actid;
    private String action;
    private String actiontxt;
    private String uid;
    private String whaleyuid;
    private String nickname;
    private String status;
    private String goodsid;
    private String name;
    private String picture;
    private String info;
    private String dateline;
    private String goodstype;

    public String getActid() {
        return actid;
    }

    public void setActid(String actid) {
        this.actid = actid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActiontxt() {
        return actiontxt;
    }

    public void setActiontxt(String actiontxt) {
        this.actiontxt = actiontxt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWhaleyuid() {
        return whaleyuid;
    }

    public void setWhaleyuid(String whaleyuid) {
        this.whaleyuid = whaleyuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getGoodstype() {
        return goodstype;
    }

    public void setGoodstype(String goodstype) {
        this.goodstype = goodstype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.actid);
        dest.writeString(this.action);
        dest.writeString(this.actiontxt);
        dest.writeString(this.uid);
        dest.writeString(this.whaleyuid);
        dest.writeString(this.nickname);
        dest.writeString(this.status);
        dest.writeString(this.goodsid);
        dest.writeString(this.name);
        dest.writeString(this.picture);
        dest.writeString(this.info);
        dest.writeString(this.dateline);
        dest.writeString(this.goodstype);
    }

    public PrizeDataModel() {
    }

    protected PrizeDataModel(Parcel in) {
        this.actid = in.readString();
        this.action = in.readString();
        this.actiontxt = in.readString();
        this.uid = in.readString();
        this.whaleyuid = in.readString();
        this.nickname = in.readString();
        this.status = in.readString();
        this.goodsid = in.readString();
        this.name = in.readString();
        this.picture = in.readString();
        this.info = in.readString();
        this.dateline = in.readString();
        this.goodstype = in.readString();
    }

    public static final Creator<PrizeDataModel> CREATOR = new Creator<PrizeDataModel>() {
        public PrizeDataModel createFromParcel(Parcel source) {
            return new PrizeDataModel(source);
        }

        public PrizeDataModel[] newArray(int size) {
            return new PrizeDataModel[size];
        }
    };
}
