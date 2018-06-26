package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.api.UserInfoApi;
import com.whaley.biz.user.model.response.WhaleyStringResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/7/19
 * Introduction:
 */

public class UpdateValidateMobile extends UseCase<WhaleyStringResponse, String> {

    public UpdateValidateMobile(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<WhaleyStringResponse> buildUseCaseObservable(String phone) {
        return getRepositoryManager()
                .obtainRemoteService(UserInfoApi.class)
                .validateNewMobile(UserManager.getInstance().getDeviceId(),
                        UserManager.getInstance().getAccesstoken(),
                        phone,
                        UserConstants.NCODE)
                .map(new ResponseFunction<WhaleyStringResponse, WhaleyStringResponse>());
    }


    public static class UpdateValidateMobileParam {
        private String phone;
        private String ncode = "86";

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNcode() {
            return ncode;
        }

        public void setNcode(String ncode) {
            this.ncode = ncode;
        }
    }
}
