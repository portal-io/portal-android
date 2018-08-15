package com.whaley.biz.portal.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.portal.api.PortalApi;
import com.whaley.biz.portal.model.user.UserModel;
import com.whaley.biz.portal.response.PortalRecordResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2018/8/9.
 */

public class GetPortalRecord extends UseCase<PortalRecordResponse, GetPortalRecord.Param> {

    public GetPortalRecord(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<PortalRecordResponse> buildUseCaseObservable(Param param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .map(new Function<UserModel, UserModel>() {
                    @Override
                    public UserModel apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return userModel;
                        }
                        throw new NotLoggedInErrorException("未登录");
                    }
                }).flatMap(new Function<UserModel, ObservableSource<PortalRecordResponse>>() {
                    @Override
                    public ObservableSource<PortalRecordResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager().obtainRemoteService(PortalApi.class)
                                .records(userModel.getPortalAccessToken());
                    }
                }) .map(new ResponseFunction<PortalRecordResponse,PortalRecordResponse>());
    }

    public static class Param{

    }

}
