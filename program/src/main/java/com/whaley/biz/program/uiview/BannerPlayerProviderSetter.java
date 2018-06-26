package com.whaley.biz.program.uiview;

import com.whaley.biz.program.playersupport.widget.BannerPlayerView;

/**
 * Created by YangZhi on 2017/8/31 18:47.
 */

public interface BannerPlayerProviderSetter {

    void setBannerProvider(BannerProvider bannerProvider);

    interface BannerProvider {
        BannerPlayerView providePlayerView(int width, int height);

        void scrollPlayer();
    }
}
