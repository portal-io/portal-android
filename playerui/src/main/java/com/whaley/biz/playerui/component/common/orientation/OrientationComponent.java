package com.whaley.biz.playerui.component.common.orientation;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/2 17:07.
 */

public class OrientationComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }

    @Override
    protected BaseController onCreateController() {
        return new OrientationController();
    }
}
