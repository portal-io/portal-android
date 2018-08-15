package com.whaleyvr.biz.unity.model.pay;

import java.util.List;

/**
 * Created by dell on 2017/12/20.
 */

public class PayModel {

    private int freeTime;
    private int playTime;
    private List<PackageModel> contentPackageQueryDtos;
    private CouponModel couponDto;

    public int getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    public List<PackageModel> getContentPackageQueryDtos() {
        return contentPackageQueryDtos;
    }

    public void setContentPackageQueryDtos(List<PackageModel> contentPackageQueryDtos) {
        this.contentPackageQueryDtos = contentPackageQueryDtos;
    }

    public CouponModel getCouponDto() {
        return couponDto;
    }

    public void setCouponDto(CouponModel couponDto) {
        this.couponDto = couponDto;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }
}
