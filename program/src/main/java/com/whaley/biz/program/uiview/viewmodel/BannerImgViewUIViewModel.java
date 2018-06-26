package com.whaley.biz.program.uiview.viewmodel;


import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;

/**
 * Author: qxw
 * Date: 2017/3/16
 * banner界面模型
 */

public class BannerImgViewUIViewModel extends BaseUIViewModel {

    private String imageUrl;

    private String name;

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_BANNER_IMG;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void convert(RecommendModel recommendModel) {
        super.convert(recommendModel);
        if (recommendModel != null) {
            setName(recommendModel.getName());
            setImageUrl(recommendModel.getNewPicUrl());
            setPageModel(FormatPageModel.getPageModel(recommendModel));
        }
    }

    @Override
    public boolean isCanClick() {
        return true;
    }
}
