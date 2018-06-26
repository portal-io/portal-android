package com.whaley.biz.program.playersupport.component.normalplayer.info;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by yangzhi on 2017/8/15.
 */

public class InfoComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new InfoController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
