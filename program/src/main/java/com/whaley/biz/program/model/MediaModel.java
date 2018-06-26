package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Author: qxw
 * Date: 2016/11/8
 */

public class MediaModel extends BaseModel implements Serializable,Parcelable{

    private String parentCode;
    private String code;
    private String source;
    private String playUrl;
    private String downloadUrl;
    private String videoType;
    private String threedType;
    private String renderType;
    private String resolution;
    private int prefer;
    private int status;

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getThreedType() {
        return threedType;
    }

    public void setThreedType(String threedType) {
        this.threedType = threedType;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public int getPrefer() {
        return prefer;
    }

    public void setPrefer(int prefer) {
        this.prefer = prefer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MediaModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.parentCode);
        dest.writeString(this.code);
        dest.writeString(this.source);
        dest.writeString(this.playUrl);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.videoType);
        dest.writeString(this.threedType);
        dest.writeString(this.renderType);
        dest.writeString(this.resolution);
        dest.writeInt(this.prefer);
        dest.writeInt(this.status);
    }

    protected MediaModel(Parcel in) {
        super(in);
        this.parentCode = in.readString();
        this.code = in.readString();
        this.source = in.readString();
        this.playUrl = in.readString();
        this.downloadUrl = in.readString();
        this.videoType = in.readString();
        this.threedType = in.readString();
        this.renderType = in.readString();
        this.resolution = in.readString();
        this.prefer = in.readInt();
        this.status = in.readInt();
    }

    public static final Creator<MediaModel> CREATOR = new Creator<MediaModel>() {
        @Override
        public MediaModel createFromParcel(Parcel source) {
            return new MediaModel(source);
        }

        @Override
        public MediaModel[] newArray(int size) {
            return new MediaModel[size];
        }
    };


    @Override
    public boolean equals(Object obj) {
        if(obj!=null) {
            if (obj instanceof String) {
                String url = (String) obj;
                if(getPlayUrl()!=null)
                    return url.equals(getPlayUrl());
                else
                    return false;
            }
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "MediaModel{" +
                "parentCode='" + parentCode + '\'' +
                ", code='" + code + '\'' +
                ", source='" + source + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", videoType='" + videoType + '\'' +
                ", threedType='" + threedType + '\'' +
                ", renderType='" + renderType + '\'' +
                ", resolution='" + resolution + '\'' +
                ", prefer=" + prefer +
                ", status=" + status +
                '}';
    }
}
