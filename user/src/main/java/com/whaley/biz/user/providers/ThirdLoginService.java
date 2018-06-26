package com.whaley.biz.user.providers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.user.R;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.event.LoginSuccessEvent;
import com.whaley.biz.user.interactor.MergeThirdLogin;
import com.whaley.biz.user.interactor.ThirdLogin;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.ui.view.CompleteUserInfoFragment;
import com.whaley.biz.user.ui.view.UserBindFragment;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.utils.GsonUtil;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/9/5
 * Introduction:
 */


@Route(path = "/user/service/thirdlogin")
public class ThirdLoginService implements Executor<Map<String, Object>> {

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(Map<String, Object> stringObjectMap, Callback callback) {
        Activity activity = (Activity) stringObjectMap.get("activity");
        String type = (String) stringObjectMap.get("type");
        UpdateUIObserver updateUIObserver = (UpdateUIObserver) stringObjectMap.get("disposableObserver");
        MergeThirdLogin.MergeThirdLoginParam param = new MergeThirdLogin.MergeThirdLoginParam(activity, type);
        final MergeThirdLogin mergeThirdLogin = new MergeThirdLogin(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        mergeThirdLogin.buildUseCaseObservable(param)
                .map(new Function<UserModel, String>() {
                    @Override
                    public String apply(@NonNull UserModel userModel) throws Exception {
                        return GsonUtil.getGson().toJson(userModel);
                    }
                })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> apply(@NonNull Throwable throwable) throws Exception {
                        if (throwable instanceof StatusErrorThrowable) {
                            StatusErrorThrowable statusError = (StatusErrorThrowable) throwable;
                            String message = statusError.getMessage();
                            if (statusError.getStatus() == UserConstants.THIRD_CANCEL) {
                                message = "取消登录";
                            }
                            if (statusError.getStatus() == UserConstants.THIRD_ERROR) {
                                message = "登录失败";
                            }
                            StatusErrorThrowable statusErrorThrowable = new StatusErrorThrowable(statusError.getStatus(), message, GsonUtil.getGson().toJson(statusError.getData()));
                            return Observable.error(statusErrorThrowable);
                        }
                        return Observable.error(throwable);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(updateUIObserver);


    }
}
