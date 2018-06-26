package com.whaley.biz.program.playersupport.component.liveplayer.playcount;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by yangzhi on 2017/9/8.
 */

public class PlayCountComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }

    @Override
    protected BaseController onCreateController() {
        return new PlayCountController();
    }
}
