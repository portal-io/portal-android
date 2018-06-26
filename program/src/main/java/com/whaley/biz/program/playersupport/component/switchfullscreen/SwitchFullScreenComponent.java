package com.whaley.biz.program.playersupport.component.switchfullscreen;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by yangzhi on 2017/8/20.
 */

public class SwitchFullScreenComponent extends BaseComponent{
    @Override
    protected BaseController onCreateController() {
        return new SwitchFullScreenController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
