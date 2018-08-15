package com.whaleyvr.biz.unity.model;

/**
 * Created by dell on 2017/4/17.
 */

public class MatchInfo {

    private int matchId;
    private String behaviour;
    private String defaultSlot = "";
    private long currentProgress;

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(String behaviour) {
        this.behaviour = behaviour;
    }

    public void setCurrentProgress(long currentProgress) {
        this.currentProgress = currentProgress;
    }

    public void setDefaultSlot(String defaultSlot) {
        this.defaultSlot = defaultSlot;
    }

    public String getDefaultSlot() {
        return defaultSlot;
    }

    public long getCurrentProgress() {
        return currentProgress;
    }

}
