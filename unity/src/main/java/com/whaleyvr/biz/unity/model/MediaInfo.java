package com.whaleyvr.biz.unity.model;

import com.whaleyvr.biz.unity.model.pay.PayModel;

import java.util.List;

/**
 * Created by dell on 2016/11/23.
 */

public class MediaInfo {

    private String title; //节目名称
    private String sid; //节目code
    private float progress ; //节目播放时间（单位:秒）
    private String movieMode; //视频类型（NORMAL->普通，SPHERE ->全景,SEMIPHERE->半球）
    private String movieFormat; //视频格式（"2D"->平面， "3DUD"=>3D 上下，"3DLR"=>3D 左右）
    private String movieSource; //视频来源（“LIVE”->正在直播, “ONLINE”->“在线视频”，“OFFLINE”->“本地视频”，“DRAMA”->“互动剧”）
    private List<PathInfo> path;
    private String movieQuality; //节目清晰度（0,1,2 对应url次序）
    private String videoTag =  "";
    private String videoType;
    private long duration;
    private DramaInfo dramaInfo;
    private int playCount;
    private PayModel payModel;
    private int isChargeable;
    private String videoFormat;
    private String contentType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getMovieMode() {
        return movieMode;
    }

    public void setMovieMode(String movieMode) {
        this.movieMode = movieMode;
    }

    public String getMovieFormat() {
        return movieFormat;
    }

    public void setMovieFormat(String movieFormat) {
        this.movieFormat = movieFormat;
    }

    public String getMovieSource() {
        return movieSource;
    }

    public void setMovieSource(String movieSource) {
        this.movieSource = movieSource;
    }

    public String getMovieQuality() {
        return movieQuality;
    }

    public void setMovieQuality(String movieQuality) {
        this.movieQuality = movieQuality;
    }

    public String getVideoTag() {
        return videoTag;
    }

    public void setVideoTag(String videoTag) {
        this.videoTag = videoTag;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public List<PathInfo> getPath() {
        return path;
    }

    public void setPath(List<PathInfo> path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public DramaInfo getDramaInfo() {
        return dramaInfo;
    }

    public void setDramaInfo(DramaInfo dramaInfo) {
        this.dramaInfo = dramaInfo;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public PayModel getPayModel() {
        return payModel;
    }

    public void setPayModel(PayModel payModel) {
        this.payModel = payModel;
    }

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }

    public String getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
