package com.whaley.biz.playerui.component.simpleplayer.initbackground;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/2 20:02.
 */

public class InitBackgroundComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new InitBackgroundUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new InitBackgroundController();
    }
}
