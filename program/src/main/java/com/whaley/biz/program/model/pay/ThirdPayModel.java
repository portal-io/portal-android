package com.whaley.biz.program.model.pay;

import com.whaley.biz.program.model.CouponModel;

import java.util.List;

/**
 * Created by dell on 2017/8/22.
 */

public class ThirdPayModel {

    private List<CouponModel> packsCoupons;
    private String content;
    private int displayMode; //0 竖屏 1 横屏
    private boolean isUnity;
    private String type;
    private String code;

    public final static int DISPLAY_MODE_PORTRAIT = 0;
    public final static int DISPLAY_MODE_LANDSCAPE = 1;

    public List<CouponModel> getPacksCoupons() {
        return packsCoupons;
    }

    public void setPacksCoupons(List<CouponModel> packsCoupons) {
        this.packsCoupons = packsCoupons;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    public boolean isUnity() {
        return isUnity;
    }

    public void setUnity(boolean unity) {
        isUnity = unity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
