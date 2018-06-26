package com.whaleyvr.biz.danmu.support;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseModuleComponent;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by yangzhi on 2017/9/4.
 */

public class DanmuComponent extends BaseModuleComponent{

    @Override
    protected BaseController onCreateController() {
        return new DanmuController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new DanmuUIAdapter();
    }

}
