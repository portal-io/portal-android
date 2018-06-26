package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;
import com.whaley.biz.playerui.model.DataBuilder;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.ProgramConstants;

import java.io.Serializable;

/**
 * Author: qxw
 * Date: 2016/11/9
 */

public class TopicModel extends BaseModel implements Parcelable, Serializable, ProgramConstants {
    private int position;
    private String itemName;
    private String picUrl;
    private String videoUrl;
    private String downloadUrl;
    private String videoSource;
    private String downloadSource;
    private String subtitle;
    private String linkId;
    private String linkType;
    private String programType;
    private String videoType;
    private int status;
    private int duration;
    private String treeNodeCode;
    private StatQueryModel statQueryDto;
    private int isChargeable;
    private String price;
    private String renderType;
    private long disableTimeDate;
    private int freeTime;
    private int detailCount;
    private String introduction;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getDetailCount() {
        return detailCount;
    }

    public void setDetailCount(int detailCount) {
        this.detailCount = detailCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }

    public String getDownloadSource() {
        return downloadSource;
    }

    public void setDownloadSource(String downloadSource) {
        this.downloadSource = downloadSource;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getProgramType() {
        if (VIDEO_TYPE_MORETV_MOVIE.equals(programType)) {
            return VIDEO_TYPE_MORETV_2D;
        }
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getVideoType() {

        if (VIDEO_TYPE_MORETV_2D.equals(getProgramType()) || VIDEO_TYPE_MORETV_TV.equals(getProgramType())) {
            return getProgramType();
        }
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTreeNodeCode() {
        return treeNodeCode;
    }

    public void setTreeNodeCode(String treeNodeCode) {
        this.treeNodeCode = treeNodeCode;
    }

    public StatQueryModel getStatQueryDto() {
        return statQueryDto;
    }

    public void setStatQueryDto(StatQueryModel statQueryDto) {
        this.statQueryDto = statQueryDto;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.position);
        dest.writeString(this.itemName);
        dest.writeString(this.picUrl);
        dest.writeString(this.videoUrl);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.videoSource);
        dest.writeString(this.downloadSource);
        dest.writeString(this.subtitle);
        dest.writeString(this.linkId);
        dest.writeString(this.linkType);
        dest.writeString(this.programType);
        dest.writeString(this.videoType);
        dest.writeInt(this.status);
        dest.writeString(this.treeNodeCode);
        dest.writeParcelable(this.statQueryDto, flags);
        dest.writeInt(this.isChargeable);
        dest.writeString(this.price);
        dest.writeString(this.renderType);
        dest.writeLong(this.disableTimeDate);
        dest.writeInt(this.freeTime);
    }

    public TopicModel() {
    }

    protected TopicModel(Parcel in) {
        super(in);
        this.position = in.readInt();
        this.itemName = in.readString();
        this.picUrl = in.readString();
        this.videoUrl = in.readString();
        this.downloadUrl = in.readString();
        this.videoSource = in.readString();
        this.downloadSource = in.readString();
        this.subtitle = in.readString();
        this.linkId = in.readString();
        this.linkType = in.readString();
        this.programType = in.readString();
        this.videoType = in.readString();
        this.status = in.readInt();
        this.treeNodeCode = in.readString();
        this.statQueryDto = in.readParcelable(StatQueryModel.class.getClassLoader());
        this.isChargeable = in.readInt();
        this.price = in.readString();
        this.renderType = in.readString();
        this.disableTimeDate = in.readLong();
        this.freeTime = in.readInt();
    }

    public static final Creator<TopicModel> CREATOR = new Creator<TopicModel>() {
        @Override
        public TopicModel createFromParcel(Parcel source) {
            return new TopicModel(source);
        }

        @Override
        public TopicModel[] newArray(int size) {
            return new TopicModel[size];
        }
    };

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

    public long getDisableTimeDate() {
        return disableTimeDate;
    }

    public void setDisableTimeDate(long disableTimeDate) {
        this.disableTimeDate = disableTimeDate;
    }

    public int getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    public PlayData getPlayData() {
        int type;
        if (VIDEO_TYPE_VR.equals(getVideoType())) {
            type = TYPE_PLAY_PANO;
        } else if (VIDEO_TYPE_3D.equals(getVideoType())) {
            type = TYPE_PLAY_3D;
        } else if (VIDEO_TYPE_MORETV_2D.equals(getVideoType())) {
            type = TYPE_PLAY_MORETV_2D;
        } else if (VIDEO_TYPE_MORETV_TV.equals(getVideoType())) {
            type = TYPE_PLAY_MORETV_TV;
        } else {
            type = TYPE_PLAY_PANO;
        }
        return DataBuilder.createBuilder("", type)
                .setId(linkId)
                .setTitle(itemName)
                .setMonocular(true)
                .build();
    }
}
