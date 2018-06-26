package com.whaley.biz.program.playersupport.component.splitplayer.bottom;

import com.whaley.biz.playerui.model.State;
import com.whaley.biz.program.playersupport.component.normalplayer.normalbottomui.NormalBottomController;
import com.whaley.biz.playerui.event.SplitChangeEvent;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitBottomController extends NormalBottomController<SplitBottomUIAdapter> {

    public SplitBottomController() {
        super(false);
    }

    @Override
    public boolean onSplitClick() {
        if ( isForbidClick()) {
            return false;
        }
        emitEvent(new SplitChangeEvent(false));
        return true;
    }

    protected boolean isForbidClick(){
        return getPlayerController().getRepository().isOnBuffering()||getPlayerController().getState().getCurrentState()== State.STATE_INIT||getPlayerController().getState().getCurrentState()==State.STATE_PREPARING;
    }

}
