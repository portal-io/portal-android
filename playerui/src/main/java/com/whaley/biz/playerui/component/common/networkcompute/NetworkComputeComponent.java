package com.whaley.biz.playerui.component.common.networkcompute;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/3 21:23.
 */

public class NetworkComputeComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new NetworkController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
