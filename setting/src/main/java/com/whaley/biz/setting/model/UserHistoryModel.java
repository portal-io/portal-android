package com.whaley.biz.setting.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;
import com.whaley.biz.setting.constant.ProgramConstants;
import com.whaley.biz.setting.model.player.DataBuilder;
import com.whaley.biz.setting.model.player.PlayData;

/**
 * Created by dell on 2017/8/24.
 */

public class UserHistoryModel extends BaseModel implements Parcelable, ProgramConstants {

    private int playStatus;
    private long playTime;
    private long totalPlayTime;
    private String programCode;
    private String programType;
    private String programName;
    private String programImgUrl;
    private String parentCode;
    private long reportTime;
    private String parentDisplayName;
    private int programStatus;
    private int curEpisode;
    private int totalEpisode;
    private String type;
    private String videoType;

    public long getReportTime() {
        return reportTime;
    }

    public void setReportTime(long reportTime) {
        this.reportTime = reportTime;
    }

    public void setTotalPlayTime(long totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideoType() {
        if (VIDEO_TYPE_MORETV_2D.equals(videoType) && "tv".equals(getType())) {
            videoType = VIDEO_TYPE_MORETV_TV;
            return videoType;
        }
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public int getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(int playStatus) {
        this.playStatus = playStatus;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramImgUrl() {
        return programImgUrl;
    }

    public void setProgramImgUrl(String programImgUrl) {
        this.programImgUrl = programImgUrl;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentDisplayName() {
        return parentDisplayName;
    }

    public void setParentDisplayName(String parentDisplayName) {
        this.parentDisplayName = parentDisplayName;
    }

    public int getProgramStatus() {
        return programStatus;
    }

    public void setProgramStatus(int programStatus) {
        this.programStatus = programStatus;
    }

    public int getCurEpisode() {
        return curEpisode;
    }

    public void setCurEpisode(int curEpisode) {
        this.curEpisode = curEpisode;
    }

    public int getTotalEpisode() {
        return totalEpisode;
    }

    public void setTotalEpisode(int totalEpisode) {
        this.totalEpisode = totalEpisode;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    public void setTotalPlayTime(int totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.playStatus);
        dest.writeString(this.programCode);
        dest.writeString(this.programType);
        dest.writeString(this.programName);
        dest.writeString(this.programImgUrl);
        dest.writeString(this.parentCode);
        dest.writeString(this.parentDisplayName);
        dest.writeInt(this.programStatus);
        dest.writeInt(this.curEpisode);
        dest.writeInt(this.totalEpisode);
    }

    public UserHistoryModel() {
    }

    protected UserHistoryModel(Parcel in) {
        super(in);
        this.playStatus = in.readInt();
        this.programCode = in.readString();
        this.programType = in.readString();
        this.programName = in.readString();
        this.programImgUrl = in.readString();
        this.parentCode = in.readString();
        this.parentDisplayName = in.readString();
        this.programStatus = in.readInt();
        this.curEpisode = in.readInt();
        this.totalEpisode = in.readInt();
    }

    public static final Creator<UserHistoryModel> CREATOR = new Creator<UserHistoryModel>() {
        @Override
        public UserHistoryModel createFromParcel(Parcel source) {
            return new UserHistoryModel(source);
        }

        @Override
        public UserHistoryModel[] newArray(int size) {
            return new UserHistoryModel[size];
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
                .setProgress(playTime)
                .setMonocular(true)
                .build();
    }

}
