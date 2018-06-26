package com.whaley.biz.livegift.model;

import java.util.ArrayDeque;

/**
 * Author: qxw
 * Date:2017/10/22
 * Introduction:
 */

public class GiftState {

    public static final int NO_STATE = 0;
    public static final int SHOW_STATE = 1;
    public static final int STAY_STATE = 2;
    public static final int HIDE_STATE = 3;

    private GiftNoticeModle giftNoticeModle;
    private GiftNoticeModle temporaryModle;
    private int temporaryNum;
    private int duplicate;
    private int total;
    //    private ArrayDeque duplicateQueue;
    private int state;

    public GiftNoticeModle getTemporaryModle() {
        return temporaryModle;
    }

    public void setTemporaryModle(GiftNoticeModle temporaryModle) {
        this.temporaryModle = temporaryModle;
    }

    public int getTemporaryNum() {
        return temporaryNum;
    }

    public void setTemporaryNum(int temporaryNum) {
        this.temporaryNum = temporaryNum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public GiftNoticeModle getGiftNoticeModle() {
        return giftNoticeModle;
    }

    public void setGiftNoticeModle(GiftNoticeModle giftNoticeModle) {
        this.giftNoticeModle = giftNoticeModle;
        duplicate = 0;
        total = 0;
    }

    public void jumpQueue(GiftNoticeModle giftNoticeModle) {
        if (poll()) {
            setTemporaryModle(this.giftNoticeModle);
            setTemporaryNum(total - duplicate);
        }
        setGiftNoticeModle(giftNoticeModle);
    }

    public int getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(int duplicate) {
        this.duplicate = duplicate;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void addGift(int num) {
        total = total + num;
    }

    public boolean poll() {
        return duplicate < total;
    }

    public boolean isTemporary() {
        return temporaryModle != null;
    }

    public void replace() {
        setGiftNoticeModle(getTemporaryModle());
        setTotal(getTemporaryNum());
        setTemporaryModle(null);
        setTemporaryNum(0);
    }
}
