package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.RecommendAreasBean;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: qxw
 * Date: 2017/3/16
 */

public class BannerImgLoopViewUIViewModel extends BaseUIViewModel {

    private List<BannerImgViewUIViewModel> bannerImgViewModelList;

    private int height;
    private int indicator_unfocused, indicator_focused;
    private int align = ALIGN_PARENT_RIGHT;
    private boolean isActivity;

    public final static int ALIGN_PARENT_RIGHT = 0;
    public final static int ALIGN_PARENT_LEFT = 1;
    public final static int CENTER_HORIZONTAL = 2;

    public boolean isActivity() {
        return isActivity;
    }

    public void setActivity(boolean activity) {
        isActivity = activity;
    }

    @Override
    public int getType() {
        return isActivity ? ViewTypeConstants.TYPE_ACTIVITY_BANNER_IMG_LOOP : ViewTypeConstants.TYPE_BANNER_IMG_LOOP;
    }

    public List<BannerImgViewUIViewModel> getBannerImgViewModelList() {
        return bannerImgViewModelList;
    }

    public void setBannerImgViewModelList(List<BannerImgViewUIViewModel> bannerImgViewModelList) {
        this.bannerImgViewModelList = bannerImgViewModelList;
    }

    public void convert(RecommendAreasBean areasBean) {
        List<BannerImgViewUIViewModel> bannerImgViewModelList = null;
        if (areasBean.getRecommendElements() != null && areasBean.getRecommendElements().size() > 0) {
            bannerImgViewModelList = new ArrayList<>();
            for (RecommendModel recommendModel : areasBean.getRecommendElements()) {
                BannerImgViewUIViewModel bannerImg = new BannerImgViewUIViewModel();
                bannerImg.convert(recommendModel);
                bannerImgViewModelList.add(bannerImg);
            }
        }
        setBannerImgViewModelList(bannerImgViewModelList);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getIndicator_unfocused() {
        return indicator_unfocused;
    }

    public void setIndicator_unfocused(int indicator_unfocused) {
        this.indicator_unfocused = indicator_unfocused;
    }

    public int getIndicator_focused() {
        return indicator_focused;
    }

    public void setIndicator_focused(int indicator_focused) {
        this.indicator_focused = indicator_focused;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }
}
