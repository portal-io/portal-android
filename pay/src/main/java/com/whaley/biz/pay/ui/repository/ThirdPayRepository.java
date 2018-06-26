package com.whaley.biz.pay.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.pay.model.CouponModel;
import com.whaley.biz.pay.model.PayEventModel;

import java.util.List;

/**
 * Created by dell on 2017/8/21.
 */

public class ThirdPayRepository extends MemoryRepository {

    private String programcode;
    private String content;
    private List<CouponModel> couponModels;
    private CouponModel couponModel;
    private boolean isThreeKindsPay;
    private boolean isAlipay;
    private boolean isWechat;
    private boolean isWhaleyCurrency;
    private int displayMode;
    private int whaleyMoney;
    private boolean isUnity;
    private String fromType;

    public PayEventModel getPayEventModel() {
        return payEventModel;
    }

    public void setPayEventModel(PayEventModel payEventModel) {
        this.payEventModel = payEventModel;
    }

    public boolean isThreeKindsPay() {
        return isThreeKindsPay;
    }

    public void setThreeKindsPay(boolean threeKindsPay) {
        isThreeKindsPay = threeKindsPay;
    }

    public boolean isWhaleyCurrency() {
        return isWhaleyCurrency;
    }

    public void setWhaleyCurrency(boolean whaleyCurrency) {
        isWhaleyCurrency = whaleyCurrency;
    }

    private PayEventModel payEventModel;

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public boolean isUnity() {
        return isUnity;
    }

    public void setUnity(boolean unity) {
        isUnity = unity;
    }

    public int getWhaleyMoney() {
        return whaleyMoney;
    }

    public void setWhaleyMoney(int whaleyMoney) {
        this.whaleyMoney = whaleyMoney;
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    public boolean isAlipay() {
        return isAlipay;
    }

    public void setAlipay(boolean alipay) {
        isAlipay = alipay;
    }

    public boolean isWechat() {
        return isWechat;
    }

    public void setWechat(boolean wechat) {
        isWechat = wechat;
    }

    public CouponModel getCouponModel() {
        return couponModel;
    }

    public void setCouponModel(CouponModel couponModel) {
        this.couponModel = couponModel;
    }

    public List<CouponModel> getCouponModels() {
        return couponModels;
    }

    public void setCouponModels(List<CouponModel> couponModels) {
        this.couponModels = couponModels;
        boolean isFrist = true;
        for (CouponModel couponModel : couponModels) {
            if (isFrist) {
                this.couponModel = couponModel;
                couponModel.isSelect = true;
                isFrist = false;
            } else {
                couponModel.isSelect = false;
            }
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProgramcode() {
        return programcode;
    }

    public void setProgramcode(String programcode) {
        this.programcode = programcode;
    }
}
