package com.whaleyvr.biz.danmu;

import android.text.SpannableString;

/**
 * Created by dell on 2017/7/13.
 */

public class DanMu {

    public String uid = "";
    public String nickname = "";
    public long time;
    public String playTime;
    public String content;
    public long refreshTime;
    public boolean isLocal;
    public SpannableString contentSpannable;
    public String color;
    public int duration;
    public String type; //2 管理员

    public int getDuration() {
        return duration;
    }

    public DanMu setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public String getColor() {
        return color;
    }

    public DanMu setColor(String color) {
        this.color = color;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public DanMu setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public DanMu setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public long getTime() {
        return time;
    }

    public DanMu setTime(long time) {
        this.time = time;
        return this;
    }

    public SpannableString getContentSpannable() {
        return contentSpannable;
    }

    public void setContentSpannable(SpannableString contentSpannable) {
        this.contentSpannable = contentSpannable;
    }

    public String getPlayTime() {
        return playTime;
    }

    public DanMu setPlayTime(String playTime) {
        this.playTime = playTime;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DanMu setContent(String content) {
        this.content = content;
        return this;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public String getType() {
        return type;
    }

    public DanMu setType(String type) {
        this.type = type;
        return this;
    }

}
