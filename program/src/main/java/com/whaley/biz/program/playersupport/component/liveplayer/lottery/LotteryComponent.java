package com.whaley.biz.program.playersupport.component.liveplayer.lottery;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.playerui.component.common.loading.LoadingController;

/**
 * Created by YangZhi on 2017/8/10 17:55.
 */

public class LotteryComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new LotteryUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new LotteryController();
    }

}
