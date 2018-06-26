package com.whaley.biz.program.playersupport.component.liveplayer.livetitle;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/8 20:42.
 */

public class LiveTitleComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new LiveTitleController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new LiveTitleUIAdapter();
    }

}
