package com.whaley.biz.program.playersupport.component.dramaplayer.reset;

import android.util.Log;

import com.whaley.biz.program.playersupport.component.normalplayer.normalreset.NormalResetController;
import com.whaley.biz.program.playersupport.event.ShowDramaSelectEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dell on 2017/11/15.
 */

public class DramaResetController extends NormalResetController<DramaResetUIAdapter> {

    boolean isDramaShow;
    boolean isHideUI;

    @Subscribe
    public void onShowDramaSelectEvent(ShowDramaSelectEvent showDramaSelectEvent) {
        if(isDramaShow == showDramaSelectEvent.isVisible())
            return;
        isDramaShow = showDramaSelectEvent.isVisible();
            if (isDramaShow) {
                getUIAdapter().hide();
            } else {
                if (isLandScape()) {
//                    getUIAdapter().hide(false);
                    getUIAdapter().show();
                    if(!isHide()){
                        getUIAdapter().show(false);
                    }
                }
            }
    }

    @Override
    protected void show() {
        if(isDramaShow)
            return;
        getUIAdapter().show();
    }

}
