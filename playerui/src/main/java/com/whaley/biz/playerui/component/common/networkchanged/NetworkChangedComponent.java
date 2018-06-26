package com.whaley.biz.playerui.component.common.networkchanged;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/7 20:36.
 */

public class NetworkChangedComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new NetworkChangedController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
