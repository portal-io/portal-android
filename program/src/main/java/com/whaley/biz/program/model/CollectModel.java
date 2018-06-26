package com.whaley.biz.program.model;

import com.whaley.biz.playerui.model.DataBuilder;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.ProgramConstants;

/**
 * Author: qxw
 * Date:2017/9/1
 * Introduction:
 */

public class CollectModel implements ProgramConstants {
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

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
