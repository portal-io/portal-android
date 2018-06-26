package com.whaley.biz.user.interactor.function;

import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.interactor.RefreshToken;
import com.whaley.biz.user.model.AccessTokenModel;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/3
 * Introduction:
 */

public class RefreshTokenFunction<T extends Throwable, M> implements Function<T, ObservableSource<? extends M>> {


    @Override
    public ObservableSource<M> apply(@NonNull T throwable) throws Exception {
        if (throwable instanceof StatusErrorThrowable) {
            StatusErrorThrowable statusErrorThrowable = (StatusErrorThrowable) throwable;
            if (statusErrorThrowable.getStatus() == 152) {
                RefreshToken refreshToken = new RefreshToken(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
                return refreshToken.buildUseCaseObservable(null)
                        .flatMap(new Function<AccessTokenModel, ObservableSource<M>>() {
                            @Override
                            public ObservableSource<M> apply(@NonNull AccessTokenModel accessTokenModel) throws Exception {
                                UserManager.getInstance().setAccessTokenBean(accessTokenModel);
                                return buidModelObservable();
                            }
                        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                                UserManager.signOut();
                                throw new StatusErrorThrowable(8080,"登录已过期");
                            }
                        });
            }
        }
        throw (Exception) throwable;
    }

    public ObservableSource<M> buidModelObservable() {
        return null;
    }


//    @Override
//    public ObservableSource<M> apply(@NonNull T throwable) throws Exception {
//        if (throwable instanceof StatusErrorThrowable) {
//            StatusErrorThrowable statusErrorThrowable = (StatusErrorThrowable) throwable;
//            if (statusErrorThrowable.getStatus() == 152) {
//                TokenSMS token = new TokenSMS(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
//                return token.buildUseCaseObservable(null)
//                        .flatMap(new Function<TokenModel, ObservableSource<M>>() {
//                            @Override
//                            public ObservableSource<M> apply(@NonNull TokenModel accessTokenBean) throws Exception {
//                                return buidModelObservable();
//                            }
//                        });
//            } else {
//                throw statusErrorThrowable;
//            }
//        } else {
//            throw (Exception) throwable;
//        }
//    }
}
