package com.whaley.biz.program.playersupport.component.liveplayer.liveinfo;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.playersupport.component.liveplayer.liveinfoui.LiveInfoUIAdapter;

/**
 * Created by YangZhi on 2017/8/22 12:27.
 */

public class LiveInfoComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }

    @Override
    protected BaseController onCreateController() {
        return new LiveInfoController();
    }
}
