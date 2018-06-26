package com.whaley.biz.program.playersupport.component.liveplayer.initblurbackground;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/8 19:16.
 */

public class InitBlurBackgroundComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new InitBlurBackgroundUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new InitBlurBackgroundController();
    }
}
