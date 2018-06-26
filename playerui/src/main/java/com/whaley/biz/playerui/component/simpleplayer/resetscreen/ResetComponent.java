package com.whaley.biz.playerui.component.simpleplayer.resetscreen;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/2 16:09.
 */

public class ResetComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new ResetUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new ResetController();
    }
}
