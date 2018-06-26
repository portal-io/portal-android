package com.whaley.biz.program.playersupport.component.bannerplayer.typeswitcher;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/28 16:18.
 */

public class TypeSwitcherComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new TypeSwitcherController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
