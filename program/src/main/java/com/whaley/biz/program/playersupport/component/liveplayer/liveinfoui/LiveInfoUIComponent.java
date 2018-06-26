package com.whaley.biz.program.playersupport.component.liveplayer.liveinfoui;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/9/8 19:35.
 */

public class LiveInfoUIComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new LiveInfoUIController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new LiveInfoUIAdapter();
    }
}
