package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.interactor.function.RefreshTokenFunction;
import com.whaley.biz.user.interactor.function.RetryWhenRefreshTokenFunction;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.api.UserVrApi;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/7/18.
 */

public class GetUserInfo extends BaseUseCase<UserModel, String> {


    public GetUserInfo(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<UserModel> buildUseCaseObservable(final UseCaseParam<String> stringUseCaseParam) {
        return getRepositoryManager()
                .obtainRemoteService(UserVrApi.class)
                .info(UserManager.getInstance().getDeviceId(), UserManager.getInstance().getAccesstoken())
                .map(new CommonFunction<WhaleyResponse<UserModel>, UserModel>())
                .retryWhen(new RetryWhenRefreshTokenFunction<Observable<Throwable>, UserModel>() {
                    @Override
                    public ObservableSource<UserModel> retryWhenThrowable() {
                        return buildUseCaseObservable(stringUseCaseParam);
                    }
                })
                .doOnNext(new Consumer<UserModel>() {
                    @Override
                    public void accept(@NonNull UserModel userModel) throws Exception {
                        UserManager.getInstance().saveUserInfo(userModel);
                    }
                });
//                .onErrorResumeNext(new RefreshTokenFunction<Throwable, UserModel>() {
//                    @Override
//                    public ObservableSource<UserModel> buidModelObservable() {
//                        return buildUseCaseObservable(stringUseCaseParam);
//                    }
//                });
//                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends UserModel>>() {
//                    @Override
//                    public ObservableSource<? extends UserModel> apply(@NonNull Throwable throwable) throws Exception {
//                        RefreshToken refreshToken = null;
//                        if (throwable instanceof StatusErrorThrowable) {
//                            StatusErrorThrowable statusErrorThrowable = (StatusErrorThrowable) throwable;
//                            if (statusErrorThrowable.getStatus() == 152) {
//                                return refreshToken.buildUseCaseObservable(null)
//                                        .flatMap(new Function<AccessTokenModel, ObservableSource<? extends UserModel>>() {
//                                            @Override
//                                            public ObservableSource<? extends UserModel> apply(@NonNull AccessTokenModel accessTokenBean) throws Exception {
//                                                return buildUseCaseObservable(stringUseCaseParam);
//                                            }
//                                        });
//                            } else {
//                                throw statusErrorThrowable;
//                            }
//                        } else {
//                            throw (Exception) throwable;
//                        }
//                    }
//                });
    }
}
