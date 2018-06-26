package com.whaley.biz.program.playersupport.component.normalplayer.normalbottomui;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by yangzhi on 2017/8/5.
 */

public class NormalBottomUIComponent extends BaseComponent{

    boolean isbanner;

    public NormalBottomUIComponent(boolean isbanner){
        this.isbanner = isbanner;
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new NormalBottomUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new NormalBottomController(isbanner);
    }
}
