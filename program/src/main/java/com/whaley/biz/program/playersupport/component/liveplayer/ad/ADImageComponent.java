package com.whaley.biz.program.playersupport.component.liveplayer.ad;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Author: qxw
 * Date:2018/3/26
 * Introduction:
 */

public class ADImageComponent extends BaseComponent {
    @Override
    protected BaseController onCreateController() {
        return new ADImageController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new ADImageUIAdapter();
    }
}
