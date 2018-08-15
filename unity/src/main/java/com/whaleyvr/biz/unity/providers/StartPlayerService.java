package com.whaleyvr.biz.unity.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.unity.MessageControl;
import com.whaleyvr.biz.unity.model.MediaInfo;

/**
 * Created by dell on 2017/8/14.
 */

@Route(path = "/unity/service/startPlayer")
public class StartPlayerService implements Executor<MediaInfo> {

    @Override
    public void excute(MediaInfo mediaInfo, final Callback callback) {
        EventController.postEvent(new BaseEvent("/unity/service/player",null));
        MessageControl.getInstance().startUnityPlayer(mediaInfo);

    }

    @Override
    public void init(Context context) {

    }

}
