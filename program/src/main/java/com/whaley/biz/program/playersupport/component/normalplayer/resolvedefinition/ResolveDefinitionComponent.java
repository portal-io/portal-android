package com.whaley.biz.program.playersupport.component.normalplayer.resolvedefinition;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/4 20:41.
 */

public class ResolveDefinitionComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new ResolveDefinitionController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
