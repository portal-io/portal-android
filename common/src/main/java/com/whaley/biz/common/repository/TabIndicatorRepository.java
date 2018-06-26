package com.whaley.biz.common.repository;

import com.whaley.biz.common.ui.viewmodel.TabItemViewModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2017/8/10.
 */

public class TabIndicatorRepository extends MemoryRepository implements Serializable{

    private List<TabItemViewModel> titles;

    private String titleText;

    private int count;

    private String code;

    public boolean isNoData() {
        return titles == null || titles.size() <= 0;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
