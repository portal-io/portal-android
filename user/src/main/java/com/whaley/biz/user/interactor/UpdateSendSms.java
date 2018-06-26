package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.model.CaptchaModel;
import com.whaley.biz.user.model.response.SendSMSResponse;
import com.whaley.biz.user.api.UserInfoApi;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/7/19
 * Introduction:
 */

public class UpdateSendSms extends BaseUseCase<CaptchaModel, UpdateSendSms.UpdateSendSmsParam> {

    public UpdateSendSms(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<CaptchaModel> buildUseCaseObservable(UseCaseParam<UpdateSendSmsParam> param) {
        return getRepositoryManager()
                .obtainRemoteService(UserInfoApi.class)
                .changeNewMobileSms(UserManager.getInstance().getDeviceId(),
                        param.getParam().token,
                        param.getParam().newMobile,
                        param.getParam().ncode,
                        param.getParam().captcha)
                .map(new CommonFunction<SendSMSResponse, CaptchaModel>());
    }

    public static class UpdateSendSmsParam {
        private String token;
        private String newMobile;
        private String ncode = "86";
        private String captcha;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getNewMobile() {
            return newMobile;
        }

        public void setNewMobile(String newMobile) {
            this.newMobile = newMobile;
        }

        public String getNcode() {
            return ncode;
        }

        public void setNcode(String ncode) {
            this.ncode = ncode;
        }

        public String getCaptcha() {
            return captcha;
        }

        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }
    }


}
