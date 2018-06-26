package com.whaley.biz.program.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.program.api.CollectionAPI;
import com.whaley.biz.program.model.response.StringResponse;
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

public class DeleteCollection extends UseCase<String, String> {

    public DeleteCollection() {
    }

    public DeleteCollection(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(final String code) {
        CheckLogin checkLogin = new CheckLogin(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return getRepositoryManager().obtainRemoteService(CollectionAPI.class)
                                    .deleCollection(userModel.getAccount_id(), code)
                                    .map(new CommonFunction<StringResponse, String>());
                        } else {
                            throw new NotLoggedInErrorException("未登录");
                        }
                    }
                });
    }
}
