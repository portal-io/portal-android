package com.whaley.biz.program.playersupport.component.splitplayer.playpause;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitPlayPauseComponent extends BaseComponent {

    private boolean isLocal;

    public SplitPlayPauseComponent() {
        this(false);
    }

    public SplitPlayPauseComponent(boolean isLocal) {
        this.isLocal = isLocal;
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new SplitPlayPauseUIAdapter(isLocal);
    }

    @Override
    protected BaseController onCreateController() {
        return new SplitPlayPauseController();
    }
}
