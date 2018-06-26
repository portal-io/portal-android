package com.whaley.biz.jpush.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.jpush.PushStartController;
import com.whaley.biz.jpush.model.NoticeModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.router.Executor;

/**
 * Author: qxw
 * Date:2017/9/18
 * Introduction:
 */
@Route(path = "/jpush/service/noctice")
public class NocticeService implements Executor<String> {

    @Override
    public void excute(String notice, Callback callback) {
        PushStartController.processNotice((Starter) AppContextProvider.getInstance().getContext(), notice);
    }

    @Override
    public void init(Context context) {

    }
}
