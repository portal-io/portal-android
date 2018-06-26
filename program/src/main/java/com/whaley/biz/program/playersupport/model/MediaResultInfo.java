package com.whaley.biz.program.playersupport.model;

import java.util.List;

/**
 * Created by dell on 2017/12/8.
 */

public class MediaResultInfo {

    private String title;
    private String code;
    private String currentPosition;
    private String movieSource;
    private String currentQuality;
    private boolean isDrama;
    private String currentNode;
    private List<String> nodeTrack;
    private boolean returnAppType;
    private int playTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getMovieSource() {
        return movieSource;
    }

    public void setMovieSource(String movieSource) {
        this.movieSource = movieSource;
    }

    public String getCurrentQuality() {
        return currentQuality;
    }

    public void setCurrentQuality(String currentQuality) {
        this.currentQuality = currentQuality;
    }

    public List<String> getNodeTrack() {
        return nodeTrack;
    }

    public void setNodeTrack(List<String> nodeTrack) {
        this.nodeTrack = nodeTrack;
    }

    public String getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(String currentNode) {
        this.currentNode = currentNode;
    }

    public boolean isDrama() {
        return isDrama;
    }

    public void setDrama(boolean drama) {
        isDrama = drama;
    }

    public boolean isReturnAppType() {
        return returnAppType;
    }

    public void setReturnAppType(boolean returnAppType) {
        this.returnAppType = returnAppType;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }
}
