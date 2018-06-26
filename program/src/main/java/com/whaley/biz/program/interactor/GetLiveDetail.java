package com.whaley.biz.program.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.LiveAPI;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.model.response.LiveDetailsResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/8/22 12:32.
 */

public class GetLiveDetail extends UseCase<LiveDetailsResponse, GetLiveDetail.Param> {

    CheckLogin checkLogin;

    public GetLiveDetail() {
        checkLogin = new CheckLogin();
    }

    public GetLiveDetail(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
        checkLogin = new CheckLogin();
    }

    @Override
    public Observable<LiveDetailsResponse> buildUseCaseObservable(final Param param) {
        return checkLogin.buildUseCaseObservable(null)
                .onErrorReturn(new Function<Throwable, UserModel>() {
                    @Override
                    public UserModel apply(@NonNull Throwable throwable) throws Exception {
                        return new UserModel();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<UserModel, ObservableSource<LiveDetailsResponse>>() {
                    @Override
                    public ObservableSource<LiveDetailsResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager().obtainRemoteService(LiveAPI.class)
                                .requestLiveDetail(param.getCode(), userModel.getAccount_id())
                                .subscribeOn(Schedulers.io());
                    }
                })
                .map(new ResponseFunction<LiveDetailsResponse, LiveDetailsResponse>());
    }

    public static class Param {
        private String code;

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
