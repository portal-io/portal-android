package com.whaley.biz.program.playersupport.component.normalplayer.normalinitloading;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.playerui.component.simpleplayer.initloading.InitLoadingController;
import com.whaley.biz.playerui.component.simpleplayer.initloading.InitLoadingUIAdapter;
import com.whaley.biz.playerui.component.simpleplayer.title.TitleController;
import com.whaley.biz.playerui.component.simpleplayer.title.TitleUIAdapter;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.program.constants.PlayerConstants;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/9/20 16:28.
 */

public class NormalInitLoadingComponent extends BaseComponent {

    @Override
    protected BaseController onCreateController() {
        return new InitLoadingController() {
            @Subscribe(priority = PlayerConstants.PREPARE_START_PLAY_PRIORITY_PROGRAM)
            public void onPrepareStart(PrepareStartPlayEvent prepareStartPlayEvent) {
                if(prepareStartPlayEvent.getMaxPriority()<PlayerConstants.PREPARE_START_PLAY_PRIORITY_PROGRAM)
                    return;
                updateLoadingText(prepareStartPlayEvent.getPlayData());
            }
        };
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new InitLoadingUIAdapter();
    }
}
