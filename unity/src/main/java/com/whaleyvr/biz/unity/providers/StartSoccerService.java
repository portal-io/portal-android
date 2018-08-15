package com.whaleyvr.biz.unity.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.unity.MessageControl;
import com.whaleyvr.biz.unity.model.MatchInfo;

/**
 * Created by dell on 2017/8/28.
 */

@Route(path = "/unity/service/startSoccer")
public class StartSoccerService implements Executor<MatchInfo> {

    @Override
    public void excute(MatchInfo matchInfo, final Callback callback) {
        EventController.postEvent(new BaseEvent("/unity/service/player",null));
        MessageControl.getInstance().startUnitySoccer(matchInfo);
    }

    @Override
    public void init(Context context) {

    }

}
