package com.whaley.biz.playerui.model;

import com.whaley.biz.playerui.exception.PlayerException;

import java.util.List;

/**
 * Created by YangZhi on 2017/4/18 19:40.
 */

public class Repository implements IRepository{

    private PlayData currentPlayData;

    private List<PlayData> playDataList;

    private int currentPlayIndex;

    private boolean isOnBuffering;

    private long duration;

    private long secondProgress;

    private long currentProgress;

    private boolean isStarted;

    private boolean isVideoPrepared;

    private PlayerException playerException;

    private boolean isMonocular;

    public void reset(){
        currentPlayData = null;
        isOnBuffering = false;
        duration = 0;
        secondProgress = 0;
        currentProgress = 0;
        isStarted = false;
        isVideoPrepared = false;
        playerException = null;
        isMonocular = true;
    }

    public void convertCurrentPlayData(PlayData currentPlayData){
        setCurrentPlayData(currentPlayData);
        setVideoPrepared(false);
        setDuration(currentPlayData.getDuration());
    }

    public void convertCurrentPlayData(List<PlayData> playDataList,int playIndex){
        if(playDataList == null)
            return;
        this.playDataList = playDataList;
        if(playDataList.size()-1<playIndex){
            playIndex = 0;
        }
        currentPlayIndex = playIndex;
        PlayData playData = playDataList.get(playIndex);
        convertCurrentPlayData(playData);
    }

    public void changeToNextPlayData(){
        if(hasNextPlayData()) {
            int nextIndex;
            if(currentPlayIndex == playDataList.size()-1){
                nextIndex = 0;
            }else {
                nextIndex = currentPlayIndex+1;
            }
            PlayData playData = playDataList.get(nextIndex);
            convertCurrentPlayData(playData);
            currentPlayIndex = nextIndex;
        }
    }

    private void setCurrentPlayData(PlayData currentPlayData) {
        this.currentPlayData = currentPlayData;
    }

    public PlayData getCurrentPlayData() {
        return currentPlayData;
    }

    @Override
    public boolean hasNextPlayData() {
        if(playDataList!=null) {
            return playDataList.size() > 1;
        }
        return false;
    }

    @Override
    public PlayData getNextPlayData() {
        if(hasNextPlayData()) {
            int nextIndex;
            if(currentPlayIndex == playDataList.size()-1){
                nextIndex = 0;
            }else {
                nextIndex = currentPlayIndex+1;
            }
            PlayData playData = playDataList.get(nextIndex);
            return playData;
        }
        return null;
    }

    @Override
    public void setOnBuffering(boolean onBuffering) {
        isOnBuffering = onBuffering;
    }

    @Override
    public void setMonocular(boolean isMonocular) {
        this.isMonocular = isMonocular;
    }

    @Override
    public boolean getMonocular() {
        return isMonocular;
    }

    public boolean isOnBuffering() {
        return isOnBuffering;
    }


    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setSecondProgress(long secondProgress) {
        this.secondProgress = secondProgress;
    }

    public long getSecondProgress() {
        return secondProgress;
    }

    public void setCurrentProgress(long currentProgress) {
        this.currentProgress = currentProgress;
    }

    @Deprecated
    public long getCurrentProgress() {
        return currentProgress;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isVideoPrepared() {
        return isVideoPrepared;
    }

    public void setVideoPrepared(boolean videoPrepared) {
        isVideoPrepared = videoPrepared;
    }

    public void setPlayerException(PlayerException playerException) {
        this.playerException = playerException;
    }

    public PlayerException getPlayerException() {
        return playerException;
    }

}
