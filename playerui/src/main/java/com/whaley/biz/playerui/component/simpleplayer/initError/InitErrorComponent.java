package com.whaley.biz.playerui.component.simpleplayer.initError;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/7 17:27.
 */

public class InitErrorComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new InitErrorController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new InitErrorUIAdapter();
    }
}
