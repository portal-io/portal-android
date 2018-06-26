package com.whaley.biz.program.playersupport.component.dramaplayer.lock;

import com.whaley.biz.playerui.component.simpleplayer.lock.LockUIAdapter;

/**
 * Created by dell on 2017/11/28.
 */

public class DramaLockUIAdapter extends LockUIAdapter {

    @Override
    public void hide(boolean anim) {
        if (inflatedView == null)
            return;
        startAnim(-getRootView().getMeasuredWidth(), 0, 0f);
    }

}
