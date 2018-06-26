package com.whaley.biz.user.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.interactor.RefreshToken;
import com.whaley.biz.user.model.AccessTokenModel;
import com.whaley.biz.user.model.UserModel;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Executor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2018/1/31
 * Introduction:
 */
@Route(path = "/user/service/refreshtokenservice")
public class RefreshTokenService implements Executor {
    @Override
    public void excute(Object o, final Callback callback) {
        RefreshToken refreshToken = new RefreshToken(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        refreshToken.execute(new UpdateUIObserver<AccessTokenModel>(null) {
            @Override
            public void onNext(@NonNull AccessTokenModel accessTokenModel) {
                UserManager.getInstance().setAccessTokenBean(accessTokenModel);
                callback.onCall(accessTokenModel);
            }

            @Override
            public void onStatusError(int status, String message) {
                callback.onCall(UserManager.getInstance().getAccessTokenBean());
            }

            @Override
            public void onError(Throwable e) {
                callback.onCall(UserManager.getInstance().getAccessTokenBean());
            }
        });
    }

    @Override
    public void init(Context context) {

    }
}
