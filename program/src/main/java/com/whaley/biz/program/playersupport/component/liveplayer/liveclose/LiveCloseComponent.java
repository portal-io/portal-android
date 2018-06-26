package com.whaley.biz.program.playersupport.component.liveplayer.liveclose;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by yangzhi on 2017/8/9.
 */

public class LiveCloseComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new LiveCloseController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new LiveCloseUIAdapter();
    }
}
