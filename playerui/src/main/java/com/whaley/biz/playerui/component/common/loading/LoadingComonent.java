package com.whaley.biz.playerui.component.common.loading;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/3 17:26.
 */

public class LoadingComonent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new LoadingUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new LoadingController();
    }
}
