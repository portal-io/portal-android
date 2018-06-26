package com.whaley.biz.program.playersupport.component.localplayer.localbottomui;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by yangzhi on 2017/8/22.
 */

public class LocalBottomUIComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new LocalBottomUIController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new LocalBottomUIAdapter();
    }
}
