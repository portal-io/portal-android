package com.whaley.biz.livegift.support;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseModuleComponent;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Author: qxw
 * Date:2017/10/20
 * Introduction:
 */

public class LiveGiftRevealComponent extends BaseModuleComponent {
    @Override
    protected BaseController onCreateController() {
        return new LiveGiftRevealController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new LiveGiftRevealUIAdapter();
    }

}
