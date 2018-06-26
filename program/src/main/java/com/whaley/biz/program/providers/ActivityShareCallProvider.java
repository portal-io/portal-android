package com.whaley.biz.program.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.interactor.ShareCall;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Executor;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2018/1/29
 * Introduction:
 */
@Route(path = "/program/service/activitysharecall")
public class ActivityShareCallProvider implements Executor<Integer> {

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(Integer integer, Callback callback) {
        ShareCall call = new ShareCall(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        call.execute(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        },new ShareCall.Param(integer));
    }
}
