package com.whaley.biz.program.playersupport.component.normalplayer.normalreset;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.playerui.component.simpleplayer.resetscreen.ResetController;
import com.whaley.biz.playerui.component.simpleplayer.resetscreen.ResetUIAdapter;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerType;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/9/15 21:04.
 */

public class NormalResetComponent extends BaseComponent {

    @Override
    protected BaseController onCreateController() {
        return new NormalResetController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new ResetUIAdapter();
    }
}
