package com.whaley.biz.program.playersupport.component.bannerplayer.bottomui;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/28 16:55.
 */

public class BannerBottomUIComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new BannerBottomUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new BannerBottomUIController();
    }
}
