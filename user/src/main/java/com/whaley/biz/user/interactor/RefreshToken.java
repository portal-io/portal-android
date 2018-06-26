package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.api.UserVrApi;
import com.whaley.biz.user.model.AccessTokenModel;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;


/**
 * Created by Administrator on 2017/7/18.
 * 刷新token
 */

public class RefreshToken extends BaseUseCase<AccessTokenModel, Object> {


    public RefreshToken(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }


    @Override
    public Observable<AccessTokenModel> buildUseCaseObservable(UseCaseParam<Object> param) {
        return getRepositoryManager()
                .obtainRemoteService(UserVrApi.class)
                .refreshToken(UserManager.getInstance().getDeviceId(),
                        UserManager.getInstance().getAccesstoken(),
                        UserManager.getInstance().getRefreshtoken())
                .map(new CommonFunction<WhaleyResponse<AccessTokenModel>, AccessTokenModel>());
    }

//    public class RefreshTokenParam {
//        private String deviceID;
//        private String accesstoken;
//        private String refreshtoken;
//
//
//        public String getDeviceID() {
//            return deviceID;
//        }
//
//        public void setDeviceID(String deviceID) {
//            this.deviceID = deviceID;
//        }
//
//        public String getAccesstoken() {
//            return accesstoken;
//        }
//
//        public void setAccesstoken(String accesstoken) {
//            this.accesstoken = accesstoken;
//        }
//
//        public String getRefreshtoken() {
//            return refreshtoken;
//        }
//
//        public void setRefreshtoken(String refreshtoken) {
//            this.refreshtoken = refreshtoken;
//        }
//    }
}
