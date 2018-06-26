package com.whaley.biz.program.playersupport.component.liveplayer.switchlotterydanmu;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by yangzhi on 2017/8/14.
 */

public class SwitchLotteryDanMuComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new SwitchLotteryDanMuController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
