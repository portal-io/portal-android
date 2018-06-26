package com.whaley.biz.playerui.component.common.delaytorestart;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/7 20:51.
 */

public class DelayToRestartComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new DelayToRestartController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
