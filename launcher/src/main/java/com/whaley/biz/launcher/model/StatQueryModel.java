package com.whaley.biz.launcher.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author qxw
 * @time 2016/11/9 17:56
 * 瀏覽，播放数据量
 */
public class StatQueryModel implements Parcelable, Serializable {

    private String srcCode;

    private String srcDisplayName;

    private String srcType;

    private String videoType;

    private int viewCount;

    private int playCount;

    private int playSeconds;

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getSrcCode() {
        return srcCode;
    }

    public void setSrcCode(String srcCode) {
        this.srcCode = srcCode;
    }

    public String getSrcDisplayName() {
        return srcDisplayName;
    }

    public void setSrcDisplayName(String srcDisplayName) {
        this.srcDisplayName = srcDisplayName;
    }

    public String getSrcType() {
        return srcType;
    }

    public void setSrcType(String srcType) {
        this.srcType = srcType;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getPlaySeconds() {
        return playSeconds;
    }

    public void setPlaySeconds(int playSeconds) {
        this.playSeconds = playSeconds;
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

    public StatQueryModel() {
    }

    protected StatQueryModel(Parcel in) {
        this.srcCode = in.readString();
        this.srcDisplayName = in.readString();
        this.srcType = in.readString();
        this.viewCount = in.readInt();
        this.playCount = in.readInt();
        this.playSeconds = in.readInt();
    }

    public static final Creator<StatQueryModel> CREATOR = new Creator<StatQueryModel>() {
        @Override
        public StatQueryModel createFromParcel(Parcel source) {
            return new StatQueryModel(source);
        }

        @Override
        public StatQueryModel[] newArray(int size) {
            return new StatQueryModel[size];
        }
    };
}
