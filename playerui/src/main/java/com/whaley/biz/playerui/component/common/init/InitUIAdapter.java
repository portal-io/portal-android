package com.whaley.biz.playerui.component.common.init;

import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;

/**
 * Created by YangZhi on 2017/8/3 16:39.
 */

public abstract class InitUIAdapter<CONTROLLER extends InitController> extends ControlUIAdapter<CONTROLLER>{

    @Override
    public void show(boolean anim) {

    }

    @Override
    public void hide(boolean anim) {

    }

    public abstract void showInit(boolean anim);

    public abstract void hideInit(boolean anim);

    public abstract void changeVisibleOnError();

    public abstract void changeVisibleOnComplete();

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected long getRootAnimDuration() {
        return 150;
    }
}
