package com.whaley.biz.playerui.component.simpleplayer.bottomcontrol;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/2 15:05.
 */

public class BottomControlComponent extends BaseComponent{
    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new BottomControlUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new BottomControlController();
    }
}
