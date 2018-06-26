package com.whaley.biz.playerui.component.simpleplayer.initloading;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/3 17:03.
 */

public class InitLoadingComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new InitLoadingUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new InitLoadingController();
    }
}
