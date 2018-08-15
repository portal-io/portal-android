package com.whaleyvr.biz.unity.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.unity.MessageControl;

/**
 * Created by dell on 2017/8/14.
 */

@Route(path = "/unity/service/startShow")
public class StartShowService implements Executor<String> {

    @Override
    public void excute(String room_id, final Callback callback) {
        EventController.postEvent(new BaseEvent("/unity/service/player", null));
        MessageControl.getInstance().startUnityShow(room_id);
    }

    @Override
    public void init(Context context) {

    }

}
