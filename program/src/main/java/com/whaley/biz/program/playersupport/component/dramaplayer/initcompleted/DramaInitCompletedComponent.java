package com.whaley.biz.program.playersupport.component.dramaplayer.initcompleted;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2017/11/13.
 */

public class DramaInitCompletedComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new DramaInitCompletedController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new DramaInitCompletedUIAdapter();
    }
}
