package com.whaleyvr.biz.danmu.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.core.repository.IRepositoryManager;
import com.whaleyvr.biz.danmu.api.DanmuAPI;
import com.whaleyvr.biz.danmu.response.AuthResponse;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UseAuth extends BaseUseCase<AuthResponse, UseAuth.AuthParam> {

    public UseAuth(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<AuthResponse> buildUseCaseObservable(UseCaseParam<AuthParam> param) {
        return getRepositoryManager().obtainRemoteService(DanmuAPI.class)
                .auth(param.getParam().getAccesstoken(), param.getParam().getDevice_id(),
                        param.getParam().getModel(), param.getParam().getTimestamp())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ResponseFunction<AuthResponse, AuthResponse>());
    }

    public static class AuthParam{

        private String accesstoken;
        private String device_id;
        private String model;
        private String timestamp;

        public AuthParam(String accesstoken, String device_id, String model, String timestamp){
            this.accesstoken = accesstoken;
            this.device_id = device_id;
            this.model = model;
            this.timestamp = timestamp;
        }

        public String getAccesstoken() {
            return accesstoken;
        }

        public void setAccesstoken(String accesstoken) {
            this.accesstoken = accesstoken;
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
