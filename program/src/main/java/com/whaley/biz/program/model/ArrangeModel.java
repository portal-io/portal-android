package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.playerui.model.DataBuilder;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.ProgramConstants;

import java.io.Serializable;

/**
 * Created by dell on 2017/8/10.
 */

public class ArrangeModel extends BaseModel implements Parcelable, Serializable, ProgramConstants {

    private int position;
    private String itemName;
    private String picUrl;
    private String flagUrl;
    private String videoUrl;
    private String downloadUrl;
    private String videoSource;
    private String downloadSource;
    private String subtitle;
    private String linkId;
    private String linkType;
    private String programType;
    private String videoType;
    private int duration;
    private int status;
    private String treeNodeCode;
    private int isChargeable;
    private String renderType;
    private StatQueryModel statQueryDto;


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

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
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
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getVideoType() {
        if (!"recorded".equals(programType)) {
            if ("tv".equals(linkType)) {
                return VIDEO_TYPE_MORETV_TV;
            } else {
                return VIDEO_TYPE_MORETV_2D;
            }
        }
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

    public StatQueryModel getStatQueryDto() {
        return statQueryDto;
    }

    public void setStatQueryDto(StatQueryModel statQueryDto) {
        this.statQueryDto = statQueryDto;
    }

    public ArrangeModel() {
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
        dest.writeString(this.flagUrl);
        dest.writeString(this.videoUrl);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.videoSource);
        dest.writeString(this.downloadSource);
        dest.writeString(this.subtitle);
        dest.writeString(this.linkId);
        dest.writeString(this.linkType);
        dest.writeString(this.programType);
        dest.writeString(this.videoType);
        dest.writeInt(this.duration);
        dest.writeInt(this.status);
        dest.writeString(this.treeNodeCode);
        dest.writeInt(this.isChargeable);
        dest.writeString(this.renderType);
        dest.writeParcelable(this.statQueryDto, flags);
    }

    protected ArrangeModel(Parcel in) {
        super(in);
        this.position = in.readInt();
        this.itemName = in.readString();
        this.picUrl = in.readString();
        this.flagUrl = in.readString();
        this.videoUrl = in.readString();
        this.downloadUrl = in.readString();
        this.videoSource = in.readString();
        this.downloadSource = in.readString();
        this.subtitle = in.readString();
        this.linkId = in.readString();
        this.linkType = in.readString();
        this.programType = in.readString();
        this.videoType = in.readString();
        this.duration = in.readInt();
        this.status = in.readInt();
        this.treeNodeCode = in.readString();
        this.isChargeable = in.readInt();
        this.renderType = in.readString();
        this.statQueryDto = in.readParcelable(StatQueryModel.class.getClassLoader());
    }

    public static final Creator<ArrangeModel> CREATOR = new Creator<ArrangeModel>() {
        @Override
        public ArrangeModel createFromParcel(Parcel source) {
            return new ArrangeModel(source);
        }

        @Override
        public ArrangeModel[] newArray(int size) {
            return new ArrangeModel[size];
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
        return DataBuilder.createBuilder(videoUrl, type)
                .setId(linkId)
                .setTitle(itemName)
                .setMonocular(true)
                .build();
    }
}
