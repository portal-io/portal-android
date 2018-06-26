package com.whaley.biz.program.interactor;

import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.program.api.FollowApi;
import com.whaley.biz.program.interactor.mapper.PublisheretailMapper;
import com.whaley.biz.program.model.CpDetailModel;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.model.response.CpDetailResponse;
import com.whaley.biz.program.ui.uimodel.PublisherDetailViewModel;
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
 * Date:2017/8/16
 * Introduction:
 */

public class GetCpDetail extends UseCase<CpDetailModel, String> {


    CheckLogin checkLogin;


    public GetCpDetail() {

    }

    public GetCpDetail(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<CpDetailModel> buildUseCaseObservable(final String code) {
        checkLogin = new CheckLogin(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        return checkLogin.buildUseCaseObservable(null).map(new Function<UserModel, String>() {
            @Override
            public String apply(@NonNull UserModel userModel) throws Exception {
                if (userModel.isLoginUser()) {
                    return userModel.getAccount_id();
                } else {
                    return "";
                }
            }
        }).flatMap(new Function<String, ObservableSource<CpDetailModel>>() {
            @Override
            public ObservableSource<CpDetailModel> apply(@NonNull String uid) throws Exception {
                return getRepositoryManager()
                        .obtainRemoteService(FollowApi.class)
                        .requestCpDetail(code, uid)
                        .map(new CommonFunction<CpDetailResponse, CpDetailModel>());
            }
        });
    }
}
