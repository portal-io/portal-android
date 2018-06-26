package com.whaley.biz.longconnection.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.longconnection.api.SocketAPI;
import com.whaley.biz.longconnection.model.AuthResponse;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/7/24.
 */

public class GuestAuth extends BaseUseCase<AuthResponse, GuestAuth.GusetAuthParam> {


    public GuestAuth() {
        super(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
    }

    public GuestAuth(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<AuthResponse> buildUseCaseObservable(UseCaseParam<GusetAuthParam> param) {
        return getRepositoryManager().obtainRemoteService(SocketAPI.class)
                .guestAuth(param.getParam().getDevice_id(),
                        param.getParam().getModel(), param.getParam().getTimestamp())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ResponseFunction<AuthResponse, AuthResponse>());
    }

    public static class GusetAuthParam {

        private String device_id;
        private String model;
        private String timestamp;

        public GusetAuthParam(String device_id, String model, String timestamp) {
            this.device_id = device_id;
            this.model = model;
            this.timestamp = timestamp;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

}

