package com.whaley.biz.user.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.user.UserManager;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

/**
 * Created by YangZhi on 2017/7/31 10:19.
 */

@Route(path = "/user/service/checklogin")
public class CheckLoginService implements Executor {

    @Override
    public void init(Context context) {
    }

    @Override
    public void excute(Object o, Callback callback) {
        if (UserManager.getInstance().isLogin()) {
            callback.onCall(UserManager.getInstance().getUser());
            return;
        }
        callback.onFail(new ExecutionError("未登录", null) {

        });
    }
}
