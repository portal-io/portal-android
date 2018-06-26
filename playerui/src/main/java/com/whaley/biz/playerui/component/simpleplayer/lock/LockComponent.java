package com.whaley.biz.playerui.component.simpleplayer.lock;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/2 16:09.
 */

public class LockComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new LockUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new LockController();
    }
}
