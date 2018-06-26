package com.whaley.biz.playerui.component.common.halfswitchscreen;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by yangzhi on 2017/8/4.
 */

public class HalfSwitchScreenComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new HalfSwitchScreenController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
