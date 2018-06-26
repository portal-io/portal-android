package com.whaley.biz.program.playersupport.component.liveplayer.livestatusbar;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/28 15:20.
 */

public class LiveStatusBarComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new LiveStatusBarController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
