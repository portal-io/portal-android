package com.whaley.biz.program.playersupport.component.dramaplayer.initbackgroud;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2017/11/14.
 */

public class DramaInitBackgroundComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new DramaInitBackgroundController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new DramaInitBackgroundUIAdapter();
    }

}
