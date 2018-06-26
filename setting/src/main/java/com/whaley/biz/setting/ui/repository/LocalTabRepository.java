package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.setting.ui.viewmodel.LocalTabViewModel;

import java.util.List;

/**
 * Created by dell on 2017/8/4.
 */

public class LocalTabRepository extends MemoryRepository{

    private List<LocalTabViewModel> titles;

    private String titleText = "本地";

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public List<LocalTabViewModel> getTitles() {
        return titles;
    }

    public void setTitles(List<LocalTabViewModel> titles) {
        this.titles = titles;
    }

}
