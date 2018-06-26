package com.whaley.biz.program.playersupport.component.dramaplayer.reset;

import com.whaley.biz.playerui.component.simpleplayer.resetscreen.ResetUIAdapter;

/**
 * Created by dell on 2017/11/28.
 */

public class DramaResetUIAdapter extends ResetUIAdapter {

    @Override
    public void hide(boolean anim) {
        startAnim(getRootView().getMeasuredWidth(),0,0f);
    }

}
