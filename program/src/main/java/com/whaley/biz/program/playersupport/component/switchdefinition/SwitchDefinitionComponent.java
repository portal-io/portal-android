package com.whaley.biz.program.playersupport.component.switchdefinition;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/4 20:38.
 */

public class SwitchDefinitionComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new SwitchDefinitionController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
