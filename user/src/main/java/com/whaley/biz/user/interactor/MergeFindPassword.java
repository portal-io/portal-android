package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.model.UserModel;
import com.whaley.core.inject.annotation.Presenter;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: qxw
 * Date:2017/8/8
 * Introduction:
 */

public class MergeFindPassword extends BaseUseCase<String, String> {

    @UseCase
    CaptchaUserPhone captchaUserPhone;
    @UseCase
    CaptchaSms captchaSms;

    public MergeFindPassword(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(final UseCaseParam<String> param) {
        return captchaUserPhone.buildUseCaseObservable(param)
                .flatMap(new Function<UserModel, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull UserModel userModel) throws Exception {
                        return captchaSms.buildUseCaseObservable(param);
                    }
                });
    }


    public void executeMerge(DisposableObserver<String> observer, String phone) {
        execute(observer, new UseCaseParam<>(phone));
    }
}
