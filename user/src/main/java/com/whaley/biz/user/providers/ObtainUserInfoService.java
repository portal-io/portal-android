package com.whaley.biz.user.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.interactor.GetUserInfo;
import com.whaley.biz.user.model.UserModel;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Executor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/5
 * Introduction:
 */
@Route(path = "/user/service/obtainuserinfo")
public class ObtainUserInfoService implements Executor {
    @Override
    public void excute(Object o, final Callback callback) {
        if (UserManager.getInstance().isLogin()) {
            GetUserInfo getUserInfo = new GetUserInfo(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
            getUserInfo.execute(new UpdateUIObserver<UserModel>(null) {
                @Override
                public void onNext(@NonNull UserModel userModel) {
                    super.onNext(userModel);
                    callback.onCall(UserManager.getInstance().getUser());
                }

                @Override
                public void onStatusError(int status, String message) {
                    if(status==8080){
                        callback.onFail(null);
                    }else {
                        callback.onCall(UserManager.getInstance().getUser());
                    }
                }
            });
        } else {
            callback.onFail(new ExecutionError("", null) {

            });
        }
    }

    @Override
    public void init(Context context) {


    }
}
