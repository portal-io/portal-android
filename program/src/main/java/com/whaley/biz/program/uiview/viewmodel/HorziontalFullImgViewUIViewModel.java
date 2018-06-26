package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.core.utils.DisplayUtil;


/**
 * Author: qxw
 * Date: 2017/3/16
 */

public class HorziontalFullImgViewUIViewModel extends BaseUIViewModel {
    public final static int TYPE_AD = 1;
    public final static int AD_HEIGHT = DisplayUtil.convertDIP2PX(101);


    private String imageUrl;
    private int imgHeight;
    private int imgHeightType;


    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_IMG_HORZIONTAL_FULL;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImgHeight() {
        switch (imgHeightType) {
            case TYPE_AD:
                return AD_HEIGHT;
            default:
                return imgHeight;
        }

    }

    @Override
    public boolean isCanClick() {
        return true;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public int getImgHeightType() {
        return imgHeightType;
    }

    public void setImgHeightType(int imgHeightType) {
        this.imgHeightType = imgHeightType;
    }

    public void convert(RecommendModel recommendModel) {
        super.convert(recommendModel);
        if (recommendModel != null) {
            setImageUrl(recommendModel.getNewPicUrl());
            setPageModel(FormatPageModel.getPageModel(recommendModel));
        }
    }
}
