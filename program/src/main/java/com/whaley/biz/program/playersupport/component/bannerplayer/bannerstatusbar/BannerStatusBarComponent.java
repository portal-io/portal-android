package com.whaley.biz.program.playersupport.component.bannerplayer.bannerstatusbar;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2017/9/30.
 */

public class BannerStatusBarComponent extends BaseComponent {

    @Override
    protected BaseController onCreateController() {
        return new BannerStatusBarController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
