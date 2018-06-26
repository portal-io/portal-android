package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.setting.util.RequestUtils;
import com.whaley.biz.setting.api.UserHistoryApi;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.biz.setting.response.UserHistoryResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.AppUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/8/24.
 */

public class RequestHistory extends UseCase<UserHistoryResponse, RequestHistory.Param> {

    public RequestHistory(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<UserHistoryResponse> buildUseCaseObservable(final RequestHistory.Param param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<UserHistoryResponse>>() {
            @Override
            public ObservableSource<UserHistoryResponse> apply(@NonNull UserModel userModel) throws Exception {
                final String uid;
                final String deviceId;
                if(userModel.isLoginUser()){
                    uid = userModel.getAccount_id();
                    deviceId = userModel.getDeviceId();
                }else{
                    uid = null;
                    deviceId = AppUtil.getDeviceId();
                }
                return getRepositoryManager().obtainRemoteService(UserHistoryApi.class)
                        .pageSearch(uid, deviceId, ""+param.getPage(), ""+param.getSize(), param.getDataSource(),
                                RequestUtils.getHistorySign(uid, deviceId, ""+param.getPage(), ""+param.getSize(), param.getDataSource()));
            }
        }).map(new ResponseFunction<UserHistoryResponse,UserHistoryResponse>());
    }

    public static class Param{

        private int page;
        private int size;
        private String dataSource;

        public Param(int page, int size, String dataSource){
            this.page = page;
            this.size = size;
            this.dataSource = dataSource;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getDataSource() {
            return dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }
    }

}
