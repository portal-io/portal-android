package com.whaley.biz.program.playersupport.component.splitplayer.close;

import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.BackPressEvent;

/**
 * Created by dell on 2017/11/1.
 */

public class SplitCloseController extends ControlController<SplitCloseUIAdapter> {

    public void onBackClick(){
        BackPressEvent backPressEvent = new BackPressEvent();
        emitEvent(backPressEvent);
    }


}
