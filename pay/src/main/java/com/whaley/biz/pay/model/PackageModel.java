package com.whaley.biz.pay.model;

/**
 * Author: qxw
 * Date:2017/9/1
 * Introduction:
 */

public class PackageModel {

    private String code;
    private String displayName;
    private int type;
    private int payType;
    private String pic;
    private String description;
    private int seatus;
    private int totalCount;
    private String price;
    private int isChargeable;
    private String currency;
    private CouponModel couponDto;
    private String discountPrice;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeatus() {
        return seatus;
    }

    public void setSeatus(int seatus) {
        this.seatus = seatus;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public CouponModel getCouponDto() {
        return couponDto;
    }

    public void setCouponDto(CouponModel couponDto) {
        this.couponDto = couponDto;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }
}
