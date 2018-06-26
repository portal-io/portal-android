package com.whaley.biz.setting.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.setting.constant.ProgramConstants;
import com.whaley.biz.setting.model.player.DataBuilder;
import com.whaley.biz.setting.model.player.PlayData;

public class CollectModel implements Parcelable, ProgramConstants {
    private int id;
    private String createTime;
    private String updateTime;
    private String userLoginId;
    private String userName;
    private String programName;
    private String programCode;
    private String videoType;
    private String programType;
    private int status;
    private String picUrl;
    private int duration;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
        dest.writeString(this.userLoginId);
        dest.writeString(this.userName);
        dest.writeString(this.programName);
        dest.writeString(this.programCode);
        dest.writeString(this.videoType);
        dest.writeString(this.programType);
        dest.writeInt(this.status);
        dest.writeString(this.picUrl);
        dest.writeInt(this.duration);
    }

    public CollectModel() {
    }

    protected CollectModel(Parcel in) {
        this.id = in.readInt();
        this.createTime = in.readString();
        this.updateTime = in.readString();
        this.userLoginId = in.readString();
        this.userName = in.readString();
        this.programName = in.readString();
        this.programCode = in.readString();
        this.videoType = in.readString();
        this.programType = in.readString();
        this.status = in.readInt();
        this.picUrl = in.readString();
        this.duration = in.readInt();
    }

    public static final Creator<CollectModel> CREATOR = new Creator<CollectModel>() {
        public CollectModel createFromParcel(Parcel source) {
            return new CollectModel(source);
        }

        public CollectModel[] newArray(int size) {
            return new CollectModel[size];
        }
    };

    public PlayData getPlayData() {
        int type;
        if (VIDEO_TYPE_VR.equals(videoType)) {
            type = TYPE_PLAY_PANO;
        } else if (VIDEO_TYPE_3D.equals(videoType)) {
            type = TYPE_PLAY_3D;
        } else if (VIDEO_TYPE_MORETV_TV.equals(videoType)) {
            type = TYPE_PLAY_MORETV_TV;
        } else if (VIDEO_TYPE_MORETV_2D.equals(videoType)) {
            type = TYPE_PLAY_MORETV_2D;
        } else {
            type = TYPE_PLAY_PANO;
        }
        return DataBuilder.createBuilder("", type)
                .setId(getProgramCode())
                .setTitle(getProgramName())
                .setMonocular(true)
                .build();
    }

}
