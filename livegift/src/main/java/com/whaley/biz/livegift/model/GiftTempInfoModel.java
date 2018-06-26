package com.whaley.biz.livegift.model;

import java.util.List;

/**
 * Author: qxw
 * Date:2017/10/13
 * Introduction:
 */

public class GiftTempInfoModel {

    private String giftTemplateCode;
    private String platform;
    private String project;
    private String title;
    private String intro;
    private String pic;
    private int status;
    private List<GiftModel> giftList;


    public String getGiftTemplateCode() {
        return giftTemplateCode;
    }

    public void setGiftTemplateCode(String giftTemplateCode) {
        this.giftTemplateCode = giftTemplateCode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<GiftModel> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<GiftModel> giftList) {
        this.giftList = giftList;
    }
}
