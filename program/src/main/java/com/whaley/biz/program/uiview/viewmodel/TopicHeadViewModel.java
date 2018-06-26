package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.uiview.ViewTypeConstants;

/**
 * Author: qxw
 * Date:2017/8/21
 * Introduction:
 */

public class TopicHeadViewModel extends BaseUIViewModel {

    private boolean isPay;
    private String name;
    private String bigImageUrl;
    private String introduction;
    private String code;
    private String numVideo;
    private int isChargeable;
    private boolean isActivity;

    public boolean isActivity() {
        return isActivity;
    }

    public void setActivity(boolean activity) {
        isActivity = activity;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBigImageUrl() {
        return bigImageUrl;
    }

    public void setBigImageUrl(String bigImageUrl) {
        this.bigImageUrl = bigImageUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumVideo() {
        return numVideo;
    }

    public void setNumVideo(String numVideo) {
        this.numVideo = numVideo;
    }

    @Override
    public boolean isCanClick() {
        return isActivity;
    }

    @Override
    public int getType() {
        return isActivity ? ViewTypeConstants.TYPE_ACTIVITY_HEAD : ViewTypeConstants.TYPE_TOPIC_HEAD;
    }

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }
}
