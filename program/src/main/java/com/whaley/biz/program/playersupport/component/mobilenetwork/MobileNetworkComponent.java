package com.whaley.biz.program.playersupport.component.mobilenetwork;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/9/30 17:06.
 */

public class MobileNetworkComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new MobileNetworkController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
