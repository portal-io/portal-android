package com.whaley.biz.program.ui.arrange.repository;

import com.whaley.biz.program.model.CouponModel;

import java.util.List;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */

public class PackageRepository extends TopicRepository implements PackageService {
    private boolean isPay;

    private boolean isChargeable;
    List<CouponModel> couponModels;

    private boolean isPackage;
    private String payString;
    private String goodsNos;
    private String goodsTypes;
    private boolean isPayList;

    public String getPayString() {
        return payString;
    }

    @Override
    public void setPayString(String payString) {
        this.payString = payString;
    }

    @Override
    public boolean isHasBeenPaid() {
        return false;
    }

    @Override
    public void setHasBeenPaid(boolean hasBeenPaid) {

    }

    @Override
    public void setGoodsNos(String goodsNos) {
        this.goodsNos = goodsNos;
    }

    @Override
    public void setGoodsTypes(String goodsTypes) {
        this.goodsTypes = goodsTypes;
    }

    @Override
    public String getGoodsNos() {
        return goodsNos;
    }

    @Override
    public String getGoodsTypes() {
        return goodsTypes;
    }

    @Override
    public void setPayList(boolean isPayList) {
        this.isPayList = isPayList;
    }

    @Override
    public void setPackage(boolean isPackage) {
        this.isPackage = isPackage;
    }

    public boolean isPackage() {
        return isPackage;
    }

    @Override
    public boolean isPayList() {
        return isPayList;
    }

    public List<CouponModel> getCouponModels() {
        return couponModels;
    }

    @Override
    public void setCouponModels(List<CouponModel> couponModels) {
        this.couponModels = couponModels;
    }

    @Override
    public void setPay(boolean isPay) {
        this.isPay = isPay;
    }

    public boolean isPay() {
        return isPay;
    }

    public boolean isChargeable() {
        return isChargeable;
    }

    @Override
    public void setChargeable(boolean isChargeable) {
        this.isChargeable = isChargeable;
    }
}
