package com.whaley.biz.program.playersupport.component.dramaplayer.lock;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.playerui.component.simpleplayer.lock.LockUIAdapter;

/**
 * Created by dell on 2017/11/15.
 */

public class DramaLockComponent extends BaseComponent{
    @Override
    protected BaseController onCreateController() {
        return new DramaLockController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new DramaLockUIAdapter();
    }
}
