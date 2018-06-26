package com.whaley.biz.program.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.response.BaseResponse;
import com.whaley.biz.program.api.CollectionAPI;
import com.whaley.biz.program.model.response.CollectionResponse;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/9/1
 * Introduction:
 */

public class GetCollection extends UseCase<Boolean, String> {


    public GetCollection() {
    }

    public GetCollection(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<Boolean> buildUseCaseObservable(final String code) {
        CheckLogin checkLogin = new CheckLogin(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return getRepositoryManager().obtainRemoteService(CollectionAPI.class).getCollection(userModel.getAccount_id(), code)
                                    .map(new ResponseFunction<CollectionResponse, CollectionResponse>())
                                    .map(new Function<CollectionResponse, Boolean>() {
                                        @Override
                                        public Boolean apply(@NonNull CollectionResponse collectionResponse) throws Exception {
                                            if (collectionResponse.getData() == null) {
                                                return false;
                                            }
                                            return true;
                                        }
                                    });
                        } else {
                            throw new NotLoggedInErrorException("未登录");
                        }
                    }
                });
    }

}
