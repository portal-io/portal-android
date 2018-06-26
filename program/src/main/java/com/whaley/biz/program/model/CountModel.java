package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by dell on 2016/10/14.
 */

public class CountModel implements Parcelable, Serializable {
    /**
     * srcCode : 51c89ec6be874bdca07ef0f7ed885200
     * srcDisplayName : ttt
     * srcType : tv
     * viewCount : 2
     * playCount : 0
     * playSeconds : 0
     */

    private String srcCode;
    private String srcDisplayName;
    private String srcType;
    private int viewCount;
    private int playCount;
    private int playSeconds;

    public void setSrcCode(String srcCode) {
        this.srcCode = srcCode;
    }

    public void setSrcDisplayName(String srcDisplayName) {
        this.srcDisplayName = srcDisplayName;
    }

    public void setSrcType(String srcType) {
        this.srcType = srcType;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public void setPlaySeconds(int playSeconds) {
        this.playSeconds = playSeconds;
    }

    public String getSrcCode() {
        return srcCode;
    }

    public String getSrcDisplayName() {
        return srcDisplayName;
    }

    public String getSrcType() {
        return srcType;
    }

    public int getViewCount() {
        return viewCount;
    }

    public int getPlayCount() {
        return playCount;
    }

    public int getPlaySeconds() {
        return playSeconds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.srcCode);
        dest.writeString(this.srcDisplayName);
        dest.writeString(this.srcType);
        dest.writeInt(this.viewCount);
        dest.writeInt(this.playCount);
        dest.writeInt(this.playSeconds);
    }

    public CountModel() {
    }

    protected CountModel(Parcel in) {
        this.srcCode = in.readString();
        this.srcDisplayName = in.readString();
        this.srcType = in.readString();
        this.viewCount = in.readInt();
        this.playCount = in.readInt();
        this.playSeconds = in.readInt();
    }

    public static final Parcelable.Creator<CountModel> CREATOR = new Parcelable.Creator<CountModel>() {
        public CountModel createFromParcel(Parcel source) {
            return new CountModel(source);
        }

        public CountModel[] newArray(int size) {
            return new CountModel[size];
        }
    };
}
