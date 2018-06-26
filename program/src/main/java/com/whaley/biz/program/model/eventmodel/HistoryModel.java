package com.whaley.biz.program.model.eventmodel;

/**
 * Author: qxw
 * Date:2017/9/18
 * Introduction:
 */

public class HistoryModel {
    private String programCode;
    private String playTime;
    private String totalPlayTime;
    private String videoType;
    private int curEpisode;

    public HistoryModel(String programCode, String playTime, String totalPlayTime, String videoType, int curEpisode) {
        this.programCode = programCode;
        this.playTime = playTime;
        this.totalPlayTime = totalPlayTime;
        this.videoType = videoType;
        this.curEpisode = curEpisode;
    }
}
