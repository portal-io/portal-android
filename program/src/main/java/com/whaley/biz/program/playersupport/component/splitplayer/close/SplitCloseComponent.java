package com.whaley.biz.program.playersupport.component.splitplayer.close;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2017/11/1.
 */

public class SplitCloseComponent extends BaseComponent{

    boolean isLive;

    public SplitCloseComponent(boolean isLive){
        this.isLive = isLive;
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new SplitCloseUIAdapter(isLive);
    }

    @Override
    protected BaseController onCreateController() {
        return new SplitCloseController();
    }

}
