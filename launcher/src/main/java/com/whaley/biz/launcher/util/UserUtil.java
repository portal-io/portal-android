package com.whaley.biz.launcher.util;

import com.mw.persistent.connect.info.UserInfo;
import com.whaley.biz.launcher.model.UserModel;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaleyvr.core.network.longconnection.Connection;

/**
 * Author: qxw
 * Date:2017/9/29
 * Introduction:
 */

public class UserUtil {
    public static String accountId;

    public static void loginUser() {
        Router.getInstance().buildExecutor("/datastatistics/service/updatametadata").excute();
        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel userModel) {
                accountId = userModel.getAccount_id();
                UserInfo userInfo = new UserInfo();
                userInfo.setAccountId(userModel.getAccount_id());
                Log.d("PersistentConnect", "loginUser");
                Connection.getInstance().loginUser(userInfo);
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
            }
        }).excute();
    }

    public static void signOut() {
        Router.getInstance().buildExecutor("/datastatistics/service/updatametadata").excute();
        accountId = null;
    }
}
