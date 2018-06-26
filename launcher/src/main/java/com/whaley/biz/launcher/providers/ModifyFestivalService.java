package com.whaley.biz.launcher.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.launcher.event.ModifyFestivalEvent;
import com.whaley.biz.launcher.festival.FestivalManager;
import com.whaley.biz.launcher.model.FestivalModel;
import com.whaley.core.router.Executor;

/**
 * Created by dell on 2018/1/30.
 */

@Route(path = "/launch/service/modifyfestival")
public class ModifyFestivalService implements Executor<FestivalModel> {

    @Override
    public void excute(FestivalModel festivalModel, Callback callback) {
        FestivalManager.getInstance().setFestivalModel(festivalModel);
        EventController.postEvent(new ModifyFestivalEvent());
        callback.onCall(FestivalManager.getInstance().isFestivalAvailable());
    }

    @Override
    public void init(Context context) {

    }
}
