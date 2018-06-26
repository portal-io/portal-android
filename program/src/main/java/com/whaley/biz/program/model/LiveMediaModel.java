package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;

import java.io.Serializable;

/**
 * Created by dell on 2017/8/14.
 */

public class LiveMediaModel extends BaseModel implements Parcelable, Serializable {

    private String parentCode;

    private String code;

    private String source;

    private String playUrl;

    private Integer analysis;

    private String videoType;

    private String threedType;

    private String renderType;

    private String resolution;

    private String srcName;

    /**
     * 1:valid 0:invalid
     */
    private Integer status;

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Integer analysis) {
        this.analysis = analysis;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LiveMediaModel() {
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
        dest.writeValue(this.analysis);
        dest.writeString(this.videoType);
        dest.writeString(this.threedType);
        dest.writeString(this.renderType);
        dest.writeString(this.resolution);
        dest.writeValue(this.status);
    }

    protected LiveMediaModel(Parcel in) {
        super(in);
        this.parentCode = in.readString();
        this.code = in.readString();
        this.source = in.readString();
        this.playUrl = in.readString();
        this.analysis = (Integer) in.readValue(Integer.class.getClassLoader());
        this.videoType = in.readString();
        this.threedType = in.readString();
        this.renderType = in.readString();
        this.resolution = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<LiveMediaModel> CREATOR = new Creator<LiveMediaModel>() {
        @Override
        public LiveMediaModel createFromParcel(Parcel source) {
            return new LiveMediaModel(source);
        }

        @Override
        public LiveMediaModel[] newArray(int size) {
            return new LiveMediaModel[size];
        }
    };
}

