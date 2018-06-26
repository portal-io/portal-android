package com.whaley.biz.common.ui.viewmodel;

import java.io.Serializable;

/**
 * Created by dell on 2017/8/10.
 */

public class TabItemViewModel extends ViewModel implements Serializable {

    private String title;

    private Object data;

    public String getLinkType() {
        return LinkType;
    }

    public void setLinkType(String linkType) {
        LinkType = linkType;
    }

    private String LinkType;

    private String recommendPageType;

    private String bigCode;

    public TabItemViewModel(String title, Object data) {
        this.title = title;
        this.data = data;
    }

    public TabItemViewModel(String title, Object data, String linkType, String recommendPageType) {
        this.title = title;
        this.data = data;
        this.LinkType = linkType;
        this.recommendPageType = recommendPageType;
    }

    public String getBigCode() {
        return bigCode;
    }

    public void setBigCode(String bigCode) {
        this.bigCode = bigCode;
    }

    public String getTitle() {
        return title;
    }

    public String getRecommendPageType() {
        return recommendPageType;
    }

    public void setRecommendPageType(String recommendPageType) {
        this.recommendPageType = recommendPageType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
