package com.whaley.biz.framerate.frametest;

/**
 * Created by caojianglong on 16-8-20.
 */
public class TestResult {

    private boolean isPlayed;//是否成功播放
    private float frameRate;//播放时的帧率

    public TestResult(boolean isPlayed, float frameRate) {
        this.isPlayed = isPlayed;
        this.frameRate = frameRate;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    public float getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(float frameRate) {
        this.frameRate = frameRate;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "isPlayed=" + isPlayed +
                ", frameRate=" + frameRate +
                '}';
    }
}
