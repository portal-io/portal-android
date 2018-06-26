package com.whaley.biz.program.playersupport.component.splitplayer.bottom;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitBottomUIComponent extends BaseComponent {

    boolean isLive;

    public SplitBottomUIComponent(boolean isLive){
        this.isLive = isLive;
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new SplitBottomUIAdapter(isLive);
    }

    @Override
    protected BaseController onCreateController() {
        return new SplitBottomController();
    }

}
