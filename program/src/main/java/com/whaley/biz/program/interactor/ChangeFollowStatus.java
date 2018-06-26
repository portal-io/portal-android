package com.whaley.biz.program.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.program.api.FollowApi;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.model.response.StringResponse;
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
 * Date:2017/8/15
 * Introduction:
 */

public class ChangeFollowStatus extends UseCase<String, ChangeFollowStatus.Param> {


    public ChangeFollowStatus() {
    }

    public ChangeFollowStatus(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(final Param param) {
        CheckLogin checkLogin = new CheckLogin(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        return checkLogin.buildUseCaseObservable(null).map(new Function<UserModel, String>() {
            @Override
            public String apply(@NonNull UserModel userModel) throws Exception {
                if (userModel.isLoginUser()) {
                    return userModel.getAccount_id();
                }
                throw new NotLoggedInErrorException("未登录");
            }
        }).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull String uid) throws Exception {
                return getRepositoryManager()
                        .obtainRemoteService(FollowApi.class)
                        .changeFollowStatus(uid, param.code, param.status)
                        .map(new CommonFunction<StringResponse, String>());
            }
        });
    }


    public static class Param {
        private String code;
        private String status;

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
