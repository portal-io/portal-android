package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;

/**
 * Author: qxw
 * Date: 2017/3/20
 */

public class BannerPlayerSingerUIViewModel extends BaseUIViewModel {

    private String imgUrl;

    private String bgUrl;


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_BANNER_PLAYER_SINGER;
    }

    @Override
    public void convert(Object severModel) {
        super.convert(severModel);
        RecommendModel recommendModel = getSeverModel();
        if (recommendModel != null) {
            setBgUrl(recommendModel.getLogoImageUrl());
            setImgUrl(recommendModel.getNewPicUrl());
            setPageModel(FormatPageModel.getPageModel(recommendModel));
        }
    }
}
