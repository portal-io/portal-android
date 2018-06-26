package com.whaley.biz.pay.support;

import com.whaley.biz.pay.model.CouponModel;
import com.whaley.biz.pay.model.PackageModel;

import java.util.List;

/**
 * Created by YangZhi on 2017/8/22 12:53.
 */

public class PayDetailModel {

    private int isChargeable;

    private String code;

    private int freeTime;

    private long endTime;

    private long disableTimeDate;

    private List<PackageModel> contentPackageQueryDtos;


    private CouponModel couponDto;


    public long getDisableTimeDate() {
        return disableTimeDate;
    }

    public void setDisableTimeDate(long disableTimeDate) {
        this.disableTimeDate = disableTimeDate;
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

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    public int getFreeTime() {
        return freeTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
