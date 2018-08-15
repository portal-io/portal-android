package com.whaleyvr.biz.unity.util;

import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaleyvr.biz.unity.model.user.UserModel;

/**
 * Created by dell on 2017/8/30.
 */

public class LoginUtil {

    public static void checkLogin(final LoginCallback callback) {
        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel userModel) {
                if(callback!=null)
                    callback.onLogin(userModel);
            }
            @Override
            public void onFail(Executor.ExecutionError executionError) {
                if(callback!=null)
                    callback.onNotLogin();
            }
        }).excute();
    }

    public interface LoginCallback{

        void onLogin(UserModel userModel);

        void onNotLogin();

    }

}
