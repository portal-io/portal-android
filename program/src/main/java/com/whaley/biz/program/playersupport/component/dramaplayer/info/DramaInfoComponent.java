package com.whaley.biz.program.playersupport.component.dramaplayer.info;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2017/11/13.
 */

public class DramaInfoComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new DramaInfoController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }

}
