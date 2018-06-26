package com.whaley.biz.program.playersupport.component.dramaplayer.uploadplaycount;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/9/19 21:35.
 */

public class DramaUpLoadPlayCountComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new DramaUpLoadPlayCountController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
