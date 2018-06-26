package com.whaley.biz.program.ui.uimodel;

import com.whaley.biz.common.ui.viewmodel.IViewModel;
import com.whaley.biz.common.ui.viewmodel.TabItemViewModel;

import java.util.List;

/**
 * Author: qxw
 * Date:2017/8/16
 * Introduction:
 */

public class PublisherDetailViewModel implements IViewModel {
    private String bgPic;
    private String headPic;
    private String name;
    private String info;
    private boolean isFollowed;
    private int fans;
    private List<TabItemViewModel> titles;

    private String titleText;

    private int count;

    public List<TabItemViewModel> getTitles() {
        return titles;
    }

    public void setTitles(List<TabItemViewModel> titles) {
        this.titles = titles;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getBgPic() {
        return bgPic;
    }

    public void setBgPic(String bgPic) {
        this.bgPic = bgPic;
    }

    @Override
    public int getType() {
        return 0;
    }
}
