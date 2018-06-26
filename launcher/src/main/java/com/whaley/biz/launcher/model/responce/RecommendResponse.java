package com.whaley.biz.launcher.model.responce;

import com.whaley.biz.launcher.model.RecommendAreasBean;

/**
 * Created by dell on 2017/8/10.
 */

public class RecommendResponse extends ListRecommendResponse<RecommendAreasBean> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}