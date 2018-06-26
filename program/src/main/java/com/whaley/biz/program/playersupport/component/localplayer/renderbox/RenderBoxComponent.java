package com.whaley.biz.program.playersupport.component.localplayer.renderbox;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by yangzhi on 2017/8/22.
 */

public class RenderBoxComponent extends BaseComponent{
    @Override
    protected BaseController onCreateController() {
        return new RenderBoxController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new RenderBoxUIAdapter();
    }
}
