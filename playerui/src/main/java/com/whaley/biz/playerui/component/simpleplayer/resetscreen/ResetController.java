package com.whaley.biz.playerui.component.simpleplayer.resetscreen;

import com.whaley.biz.playerui.component.common.control.ControlController;

/**
 * Created by YangZhi on 2017/8/2 16:02.
 */

public class ResetController<T extends ResetUIAdapter> extends ControlController<T> {

    public void onResetClick(){
        getPlayerController().orientationReset();
    }

    @Override
    protected void onSwitchToProtrait() {
        super.onSwitchToProtrait();
        hide();
    }

    @Override
    protected void onSwitchToLandscape() {
        super.onSwitchToLandscape();
        show();
        if(!isHide()){
            showUI(true);
        }
    }

    @Override
    protected boolean isRegistOrientation() {
        return true;
    }

    protected void show(){
        getUIAdapter().show();
    }

    protected void hide(){
        getUIAdapter().hide();
    }
}
