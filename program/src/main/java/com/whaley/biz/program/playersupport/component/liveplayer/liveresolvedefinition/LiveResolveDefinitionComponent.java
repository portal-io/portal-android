package com.whaley.biz.program.playersupport.component.liveplayer.liveresolvedefinition;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/22 13:19.
 */

public class LiveResolveDefinitionComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new LiveResolveDefinitionController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
