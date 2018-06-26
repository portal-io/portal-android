package com.whaley.biz.program.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.CommonApi;
import com.whaley.biz.program.model.response.StringResponse;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.AppUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.http.Field;

/**
 * Author: qxw
 * Date:2017/9/12
 * Introduction:
 */

public class AddUserHistory extends UseCase<StringResponse, AddUserHistory.Param> {

    public AddUserHistory() {
        super();
    }

    public AddUserHistory(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<StringResponse> buildUseCaseObservable(final Param param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<StringResponse>>() {
                    @Override
                    public ObservableSource<StringResponse> apply(@NonNull UserModel userModel) throws Exception {
                        String uid = null;
                        String devideId;
                        if (userModel.isLoginUser()) {
                            uid = userModel.getAccount_id();
                            devideId = userModel.getDeviceId();
                        } else {
                            devideId = AppUtil.getDeviceId();
                        }
                        return getRepositoryManager().obtainRemoteService(CommonApi.class).add(uid,
                                devideId,
                                param.playTime, param.playStatus, param.programCode, param.programType, param.dataSource, param.totalPlayTime)
                                .map(new ResponseFunction<StringResponse, StringResponse>());
                    }
                });
    }

    public static class Param {
        String playTime;
        String playStatus;
        String programCode;
        String programType;
        String dataSource="app";
        String totalPlayTime;

        public String getPlayTime() {
            return playTime;
        }

        public void setPlayTime(String playTime) {
            this.playTime = playTime;
        }

        public String getPlayStatus() {
            return playStatus;
        }

        public void setPlayStatus(String playStatus) {
            this.playStatus = playStatus;
        }

        public String getProgramCode() {
            return programCode;
        }

        public void setProgramCode(String programCode) {
            this.programCode = programCode;
        }

        public String getProgramType() {
            return programType;
        }

        public void setProgramType(String programType) {
            this.programType = programType;
        }

        public String getDataSource() {
            return dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }

        public String getTotalPlayTime() {
            return totalPlayTime;
        }

        public void setTotalPlayTime(String totalPlayTime) {
            this.totalPlayTime = totalPlayTime;
        }
//        public Param(String playStatus, String programCode, String programType) {
//            this.playStatus = playStatus;
//            this.programCode = programCode;
//            this.programType = programType;
//        }
//
//        public Param(String playTime, String playStatus, String programCode, String programType, String totalPlayTime) {
//            this.playTime = playTime;
//            this.playStatus = playStatus;
//            this.programCode = programCode;
//            this.programType = programType;
//            this.totalPlayTime = totalPlayTime;
//        }
    }

}
