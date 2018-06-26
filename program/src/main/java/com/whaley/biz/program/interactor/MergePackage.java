package com.whaley.biz.program.interactor;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.response.PackageResponse;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.ui.arrange.repository.PackageService;
import com.whaley.biz.program.ui.arrange.repository.TopicRepository;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */

@Route(path = ProgramRouterPath.USECASE_PACKAGE)
public class MergePackage extends UseCase<PackageResponse, String> implements IProvider {
    @com.whaley.core.inject.annotation.UseCase
    IsPayed isPayed;
    @com.whaley.core.inject.annotation.UseCase
    GetPackage getPackage;

    @com.whaley.core.inject.annotation.UseCase
    CheckLogin checkLogin;

    public MergePackage() {

    }

    public MergePackage(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<PackageResponse> buildUseCaseObservable(final String code) {
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return isPayed.buildUseCaseObservable(new IsPayed.Param(code, ProgramConstants.TYPE_CONTENT_PACKGE))
                                    .map(new Function<Boolean, Boolean>() {
                                        @Override
                                        public Boolean apply(@NonNull Boolean aBoolean) throws Exception {
                                            return aBoolean;
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext(new Consumer<Boolean>() {
                                        @Override
                                        public void accept(@NonNull Boolean aBoolean) throws Exception {
                                            getRepositoryManager().obtainMemoryService(PackageService.class).setPay(aBoolean);
                                            getRepositoryManager().obtainMemoryService(PackageService.class).setHasBeenPaid(aBoolean);
                                        }
                                    });
                        } else {
                            return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                @Override
                                public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                                    if (e.isDisposed())
                                        return;
                                    e.onNext(false);
                                    e.onComplete();
                                }
                            });
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<PackageResponse>>() {
                    @Override
                    public ObservableSource<PackageResponse> apply(@NonNull Boolean aBoolean) throws Exception {
                        return getPackage.buildUseCaseObservable(code);
                    }
                });
    }


    @Override
    public void init(Context context) {

    }

}
