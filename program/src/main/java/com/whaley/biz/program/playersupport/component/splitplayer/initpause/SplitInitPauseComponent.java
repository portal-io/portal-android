package com.whaley.biz.program.playersupport.component.splitplayer.initpause;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2017/11/9.
 */

public class SplitInitPauseComponent extends BaseComponent {

    private boolean isLock;

    public SplitInitPauseComponent() {
        this(false);
    }

    public SplitInitPauseComponent(boolean isLock) {
        this.isLock = isLock;
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new SplitInitPauseUIAdapter(isLock);
    }

    @Override
    protected BaseController onCreateController() {
        return new SplitInitPauseController();
    }
}
