package com.whaleyvr.biz.parser.support;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseModuleComponent;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/9/7 17:08.
 */

public class DecryptUrlComponent extends BaseModuleComponent{

    @Override
    protected BaseController onCreateController() {
        return new DecryptUrlController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
