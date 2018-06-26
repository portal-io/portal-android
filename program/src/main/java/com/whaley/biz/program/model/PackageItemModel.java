package com.whaley.biz.program.model;

import android.os.Parcel;

import com.whaley.biz.playerui.model.DataBuilder;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.ProgramConstants;


/**
 * Author: qxw
 * Date: 2017/4/13
 */

public class PackageItemModel extends BaseModel implements ProgramConstants {


    private String code;
    private String packCode;
    private String contentCode;
    private String displayName;
    private String pic;
    private int viewCount;
    private int playCount;
    private int sort;
    private int duration;
    private int status;
    private String contentType;
    private String videoType;
    private int liveStatus;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getContentCode() {
        return contentCode;
    }

    public void setContentCode(String contentCode) {
        this.contentCode = contentCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.code);
        dest.writeString(this.packCode);
        dest.writeString(this.contentCode);
        dest.writeString(this.displayName);
        dest.writeString(this.pic);
        dest.writeInt(this.viewCount);
        dest.writeInt(this.playCount);
        dest.writeInt(this.sort);
        dest.writeInt(this.duration);
        dest.writeInt(this.status);
        dest.writeString(this.contentType);
        dest.writeString(this.videoType);
        dest.writeInt(this.liveStatus);
    }

    public PackageItemModel() {
    }

    protected PackageItemModel(Parcel in) {
        super(in);
        this.code = in.readString();
        this.packCode = in.readString();
        this.contentCode = in.readString();
        this.displayName = in.readString();
        this.pic = in.readString();
        this.viewCount = in.readInt();
        this.playCount = in.readInt();
        this.sort = in.readInt();
        this.duration = in.readInt();
        this.status = in.readInt();
        this.contentType = in.readString();
        this.videoType = in.readString();
        this.liveStatus = in.readInt();
    }

    public static final Creator<PackageItemModel> CREATOR = new Creator<PackageItemModel>() {
        @Override
        public PackageItemModel createFromParcel(Parcel source) {
            return new PackageItemModel(source);
        }

        @Override
        public PackageItemModel[] newArray(int size) {
            return new PackageItemModel[size];
        }
    };


    public String getVideoType() {
        return videoType;
    }

    public boolean isLive() {
        return ProgramConstants.TYPE_LIVE.equals(getVideoType()) || ProgramConstants.TYPE_LIVE.equals(getContentType());
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public PlayData getPlayData() {
        int type;
        if (isLive()) {
            type = TYPE_PLAY_LIVE;
        } else if (VIDEO_TYPE_VR.equals(getVideoType())) {
            type = TYPE_PLAY_PANO;
        } else if (VIDEO_TYPE_3D.equals(getVideoType())) {
            type = TYPE_PLAY_3D;
        } else {
            type = TYPE_PLAY_PANO;
        }

        return DataBuilder.createBuilder("", type)
                .setId(contentCode)
                .setTitle(displayName)
                .setMonocular(true)
                .build();
    }
}
