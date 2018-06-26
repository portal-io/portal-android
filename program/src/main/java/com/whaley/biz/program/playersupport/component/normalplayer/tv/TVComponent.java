package com.whaley.biz.program.playersupport.component.normalplayer.tv;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/23 16:58.
 */

public class TVComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new TVController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
