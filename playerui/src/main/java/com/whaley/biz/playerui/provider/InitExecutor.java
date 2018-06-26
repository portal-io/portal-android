package com.whaley.biz.playerui.provider;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.playerui.PlayerUIInitor;
import com.whaley.core.router.Executor;

/**
 * Created by yangzhi on 2017/8/13.
 */

@Route(path = "/playerui/service/init")
public class InitExecutor implements Executor<Void>{
    Context context;

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void excute(Void sVoid, Callback callback) {
        PlayerUIInitor.init(context);
    }
}
