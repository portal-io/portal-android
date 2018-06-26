package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.api.UserInfoApi;
import com.whaley.biz.user.model.response.WhaleyStringResponse;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/7/19
 * Introduction:
 */

public class UpdatePhone extends BaseUseCase<String, UpdatePhone.UpdatePhoneParam> {


    public UpdatePhone(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(UseCaseParam<UpdatePhoneParam> param) {
        return getRepositoryManager()
                .obtainRemoteService(UserInfoApi.class)
                .updateMobileNext(UserManager.getInstance().getDeviceId(),
                        UserManager.getInstance().getAccesstoken(),
                        param.getParam().phone,
                        param.getParam().ncode,
                        param.getParam().code)
                .map(new CommonFunction<WhaleyStringResponse, String>());
    }

    public class UpdatePhoneParam {
        private String phone;
        private String ncode = "86";
        private String code;

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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
