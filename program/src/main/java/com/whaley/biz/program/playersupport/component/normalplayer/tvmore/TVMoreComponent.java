package com.whaley.biz.program.playersupport.component.normalplayer.tvmore;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/24 16:23.
 */

public class TVMoreComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new TVMoreController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
