package com.whaley.biz.program.interactor;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.LiveAPI;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.response.LiveListResponse;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/8/14.
 */

@Route(path = ProgramRouterPath.USECASE_RESERVE)
public class GetReservation extends UseCase<LiveListResponse, GetReservation.Param> implements IProvider {

    public GetReservation() {

    }

    public GetReservation(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<LiveListResponse> buildUseCaseObservable(Param param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<LiveListResponse>>() {
                    @Override
                    public ObservableSource<LiveListResponse> apply(@NonNull UserModel userModel) throws Exception {
                        String uid = null;
                        String token = null;
                        String device_id = null;
                        if (userModel != null) {
                            uid = userModel.getAccount_id();
                            if (userModel.getAccessTokenModel() != null) {
                                token = userModel.getAccessTokenModel().getAccesstoken();
                            }
                            device_id = userModel.getDeviceId();
                        }
                        return getRepositoryManager().obtainRemoteService(LiveAPI.class)
                                .reservation(uid, token, device_id).map(new ResponseFunction<LiveListResponse, LiveListResponse>());
                    }
                });
    }

    @Override
    public void init(Context context) {

    }

    public static class Param {

    }

}
