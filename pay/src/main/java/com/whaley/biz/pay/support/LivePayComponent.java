package com.whaley.biz.pay.support;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseModuleComponent;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/9/7 18:56.
 */

public class LivePayComponent extends BaseModuleComponent<Integer>{

    @Override
    protected BaseController onCreateController() {
        return new LivePayController(getData());
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new LivePayUIAdapter();
    }

}
