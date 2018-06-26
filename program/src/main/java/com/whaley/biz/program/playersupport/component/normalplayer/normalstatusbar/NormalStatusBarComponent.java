package com.whaley.biz.program.playersupport.component.normalplayer.normalstatusbar;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by yangzhi on 2017/8/25.
 */

public class NormalStatusBarComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new NormalStatusBarController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }

}
