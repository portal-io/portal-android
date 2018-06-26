package com.whaley.biz.user.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.event.LoginSuccessEvent;
import com.whaley.biz.user.event.SignOutEvent;
import com.whaley.core.router.Executor;

/**
 * Author: qxw
 * Date:2017/8/5
 * Introduction:
 */
@Route(path = "/user/service/signout")
public class SignOutServie implements Executor {


    @Override
    public void excute(Object o, Callback callback) {
        UserManager.signOut();
        callback.onCall(true);
    }

    @Override
    public void init(Context context) {

    }
}
