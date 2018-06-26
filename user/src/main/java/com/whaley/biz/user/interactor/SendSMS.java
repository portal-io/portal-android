package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.model.CaptchaModel;
import com.whaley.biz.user.model.response.SendSMSResponse;
import com.whaley.biz.user.api.UserVrApi;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;


/**
 * Author: qxw
 * Date: 2017/7/17
 * 发送短信验证码
 */

public class SendSMS extends BaseUseCase<CaptchaModel, SendSMS.SendSMSParam> {


    public SendSMS(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }


    @Override
    public Observable<CaptchaModel> buildUseCaseObservable(UseCaseParam<SendSMSParam> param) {
        Observable<CaptchaModel> observable =
                getRepositoryManager()
                        .obtainRemoteService(UserVrApi.class)
                        .send(UserManager.getInstance().getDeviceId(),
                                param.getParam().token,
                                param.getParam().mobile,
                                UserConstants.NCODE,
                                param.getParam().captcha)
                        .map(new CommonFunction<SendSMSResponse, CaptchaModel>()
                        );
        return observable;
    }


    public static class SendSMSParam {
        private String deviceID;
        private String token;
        private String mobile;
        private String ncode = "86";
        private String captcha;

        public String getDeviceID() {
            return deviceID;
        }

        public void setDeviceID(String deviceID) {
            this.deviceID = deviceID;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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
