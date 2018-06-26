package com.whaley.biz.playerui.component.simpleplayer.initCompleted;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/7 22:07.
 */

public class InitCompletedComponent extends BaseComponent{
    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new InitCompletedUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new InitCompletedController();
    }
}
