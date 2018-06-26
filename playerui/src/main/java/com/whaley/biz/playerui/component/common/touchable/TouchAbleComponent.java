package com.whaley.biz.playerui.component.common.touchable;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/2 15:20.
 */

public class TouchAbleComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new TouchAbleUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new TouchAbleController();
    }
}
