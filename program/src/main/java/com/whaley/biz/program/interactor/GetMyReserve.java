package com.whaley.biz.program.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.LiveAPI;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.model.response.ReserveListResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/8/21.
 */

public class GetMyReserve extends UseCase<ReserveListResponse, GetReservation.Param> {

    public GetMyReserve(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<ReserveListResponse> buildUseCaseObservable(GetReservation.Param param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<ReserveListResponse>>() {
                    @Override
                    public ObservableSource<ReserveListResponse> apply(@NonNull UserModel userModel) throws Exception {
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
                                .requestReserveList(uid, token, device_id);
                    }
                }).map(new ResponseFunction<ReserveListResponse, ReserveListResponse>());
    }


    public static class Param {

    }

}
