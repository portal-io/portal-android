package com.whaley.biz.program.playersupport.component.dramaplayer.bottomui;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2017/11/14.
 */

public class DramaBottomUIComponent extends BaseComponent {

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new DramaBottomUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new DramaBottomController();
    }
}
