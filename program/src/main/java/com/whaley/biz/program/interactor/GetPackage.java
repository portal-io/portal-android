package com.whaley.biz.program.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.response.BaseResponse;
import com.whaley.biz.program.api.ArrangeApi;
import com.whaley.biz.program.api.DiscoveryAPI;
import com.whaley.biz.program.model.response.PackageResponse;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:节目包
 */

public class GetPackage extends UseCase<PackageResponse, String> {


    @com.whaley.core.inject.annotation.UseCase
    CheckLogin checkLogin;

    public GetPackage() {
    }

    public GetPackage(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<PackageResponse> buildUseCaseObservable(final String code) {
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<PackageResponse>>() {
                    @Override
                    public ObservableSource<PackageResponse> apply(@NonNull UserModel userModel) throws Exception {
                        String uid = null;
                        if (userModel.isLoginUser()) {
                            uid = userModel.getAccount_id();
                        }
                        return getRepositoryManager().obtainRemoteService(DiscoveryAPI.class).requestProgramList(code, uid)
                                .map(new ResponseFunction<PackageResponse, PackageResponse>());
                    }
                });

    }
}
