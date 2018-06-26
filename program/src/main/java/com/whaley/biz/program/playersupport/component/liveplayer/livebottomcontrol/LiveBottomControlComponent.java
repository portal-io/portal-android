package com.whaley.biz.program.playersupport.component.liveplayer.livebottomcontrol;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/8 20:15.
 */

public class LiveBottomControlComponent extends BaseComponent{

    boolean isbanner;

    public LiveBottomControlComponent(boolean isbanner){
        this.isbanner = isbanner;
    }

    @Override
    protected BaseController onCreateController() {
        return new LiveBottomControlController(isbanner);
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new LiveBottomControlUIAdapter();
    }
}
