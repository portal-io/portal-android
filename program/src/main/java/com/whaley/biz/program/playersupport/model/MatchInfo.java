package com.whaley.biz.program.playersupport.model;

/**
 * Created by dell on 2017/4/17.
 */

public class MatchInfo {

    public int matchId;
    public String behaviour = "";
    public String defaultSlot = "";
    public long currentProgress;

    public MatchInfo(int matchId, String behaviour){
        this(matchId, behaviour, "", 0);
    }

    public MatchInfo(int matchId, String behaviour, String defaultSlot){
        this(matchId, behaviour, defaultSlot, 0);
    }

    public MatchInfo(int matchId, String behaviour, String defaultSlot, long currentProgress){
        this.matchId = matchId;
        this.behaviour = behaviour;
        this.defaultSlot = defaultSlot;
        this.currentProgress = currentProgress;
    }

}
