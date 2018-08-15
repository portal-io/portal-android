package com.whaley.biz.program.model.portal;

/**
 * Created by dell on 2018/8/9.
 */

public class PortalVideoModel {

    private String videoName;
    private String videoCode;
    private String accountId;
    private int duration;

    public PortalVideoModel(String videoName, String videoCode, String accountId, int duration) {
        this.videoName = videoName;
        this.videoCode = videoCode;
        this.accountId = accountId;
        this.duration = duration;
    }

    public PortalVideoModel(String videoName, String videoCode, int duration) {
        this.videoName = videoName;
        this.videoCode = videoCode;
        this.duration = duration;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoCode() {
        return videoCode;
    }

    public void setVideoCode(String videoCode) {
        this.videoCode = videoCode;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
