package com.whaley.biz.program.playersupport.component.liveplayer.liveinfoui;

/**
 * Created by yangzhi on 2017/9/8.
 */

public class LiveInfoViewModel {
    private String tvTitle;
    private String tvContent;
    private String playCount;
    private String tvPlayerTime;
    private String tvLocation;

    public String getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }

    public String getTvContent() {
        return tvContent;
    }

    public void setTvContent(String tvContent) {
        this.tvContent = tvContent;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

    public String getTvPlayerTime() {
        return tvPlayerTime;
    }

    public void setTvPlayerTime(String tvPlayerTime) {
        this.tvPlayerTime = tvPlayerTime;
    }

    public String getTvLocation() {
        return tvLocation;
    }

    public void setTvLocation(String tvLocation) {
        this.tvLocation = tvLocation;
    }
}
