package com.whaley.biz.program.playersupport.component.dramaplayer.reset;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.playerui.component.simpleplayer.resetscreen.ResetUIAdapter;

/**
 * Created by dell on 2017/11/15.
 */

public class DramaResetComponent extends BaseComponent {

    @Override
    protected BaseController onCreateController() {
        return new DramaResetController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new DramaResetUIAdapter();
    }
}
