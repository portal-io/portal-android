package com.whaley.biz.program.playersupport.component.liveplayer.lotteryresult;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by yangzhi on 2017/8/14.
 */

public class LotteryResultComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new LotteryResultUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new LotteryResultController();
    }
}
