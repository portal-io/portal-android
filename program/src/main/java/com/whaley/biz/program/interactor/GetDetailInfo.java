package com.whaley.biz.program.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.ProgramApi;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.model.response.ProgramDetailResponse;
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
 * Created by yangzhi on 2017/8/15.
 */

public class GetDetailInfo extends UseCase<ProgramDetailResponse, GetDetailInfo.Param> {

    CheckLogin checkLogin;

    public GetDetailInfo() {
        checkLogin = new CheckLogin();
    }

    public GetDetailInfo(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
        checkLogin = new CheckLogin();
    }

    @Override
    public Observable<ProgramDetailResponse> buildUseCaseObservable(final GetDetailInfo.Param param) {
        return checkLogin.buildUseCaseObservable(null)
                .onErrorReturn(new Function<Throwable, UserModel>() {
                    @Override
                    public UserModel apply(@NonNull Throwable throwable) throws Exception {
                        return new UserModel();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<UserModel, ObservableSource<ProgramDetailResponse>>() {
                    @Override
                    public ObservableSource<ProgramDetailResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager().obtainRemoteService(ProgramApi.class)
                                .requestProgramDetail(param.getCode(), userModel.getAccount_id())
                                .subscribeOn(Schedulers.io());
                    }
                }).map(new ResponseFunction<ProgramDetailResponse, ProgramDetailResponse>());
    }

    public static class Param {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
