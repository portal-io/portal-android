package com.whaley.biz.playerui.component.simpleplayer.initloading;

import com.whaley.biz.playerui.component.common.init.InitController;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/3 17:04.
 */

public class InitLoadingController extends InitController<InitLoadingUIAdapter> {

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        updateLoadingText(preparingEvent.getPlayData());
    }

    protected void updateLoadingText(PlayData playData) {
        String title = playData.getTitle();
        String loadingText = StrUtil.isEmpty(getPlayerController().getRepository().getCurrentPlayData().getTitle()) ? "" : "即将播放:" + title;
        getUIAdapter().updateLoadingText(loadingText);
    }

    @Override
    public void registEvents() {
        super.registEvents();

    }

    @Override
    public void unRegistEvents() {
        super.unRegistEvents();
        if (getUIAdapter() != null) {
            getUIAdapter().hideInit(true);
        }
    }
}
