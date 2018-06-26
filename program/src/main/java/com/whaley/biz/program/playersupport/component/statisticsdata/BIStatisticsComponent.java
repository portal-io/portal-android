package com.whaley.biz.program.playersupport.component.statisticsdata;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Author: qxw
 * Date:2017/9/22
 * Introduction:
 */

public class BIStatisticsComponent extends BaseComponent {

    @Override
    protected BaseController onCreateController() {
        return new BIStatisticsController(false);
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }

}
