package com.whaley.biz.program.model;

/**
 * Created by YangZhi on 2017/10/12 15:55.
 */

public class GiftTotalCountModel {

    /**
     * liveCode : 519f52b1c87a40b19096e7ecc95bec9f
     * totalUserCount : 1
     * totalGiftCount : 7
     * totalWhaleyCurrentCount : 70
     */

    private String liveCode;
    private int totalUserCount;
    private int totalGiftCount;
    private int totalWhaleyCurrentCount;

    public String getLiveCode() {
        return liveCode;
    }

    public void setLiveCode(String liveCode) {
        this.liveCode = liveCode;
    }

    public int getTotalUserCount() {
        return totalUserCount;
    }

    public void setTotalUserCount(int totalUserCount) {
        this.totalUserCount = totalUserCount;
    }

    public int getTotalGiftCount() {
        return totalGiftCount;
    }

    public void setTotalGiftCount(int totalGiftCount) {
        this.totalGiftCount = totalGiftCount;
    }

    public int getTotalWhaleyCurrentCount() {
        return totalWhaleyCurrentCount;
    }

    public void setTotalWhaleyCurrentCount(int totalWhaleyCurrentCount) {
        this.totalWhaleyCurrentCount = totalWhaleyCurrentCount;
    }
}
