package com.whaley.biz.playerui.component.common.backpress;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/4 17:08.
 */

public class BackPressComponent extends BaseComponent{

    private BackPressController.SwitchScreenHandle switchScreenHandle;

    public BackPressComponent(BackPressController.SwitchScreenHandle switchScreenHandle) {
        this.switchScreenHandle = switchScreenHandle;
    }

    @Override
    protected BaseController onCreateController() {
        return new BackPressController(switchScreenHandle);
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
