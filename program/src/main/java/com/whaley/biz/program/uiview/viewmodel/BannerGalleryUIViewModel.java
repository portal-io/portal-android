package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.uiview.ViewTypeConstants;

import java.util.List;

/**
 * Created by dell on 2017/8/23.
 */

public class BannerGalleryUIViewModel extends BaseUIViewModel {

    private List<BannerItemUIViewModel> bannerItemUIViewModels;

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_BANNER_GALLERY;
    }

    public List<BannerItemUIViewModel> getBannerItemUIViewModels() {
        return bannerItemUIViewModels;
    }

    public void setBannerItemUIViewModels(List<BannerItemUIViewModel> bannerItemUIViewModels) {
        this.bannerItemUIViewModels = bannerItemUIViewModels;
    }

}
