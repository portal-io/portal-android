package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.api.ForgotApi;
import com.whaley.biz.user.model.response.WhaleyStringResponse;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: qxw
 * Date:2017/7/19
 * Introduction:重置密码
 */

public class CaptchaReset extends BaseUseCase<String, CaptchaReset.CaptchaResetParam> {

    public CaptchaReset(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(UseCaseParam<CaptchaResetParam> param) {
        return getRepositoryManager()
                .obtainRemoteService(ForgotApi.class)
                .reset(param.getParam().code, param.getParam().password)
                .map(new CommonFunction<WhaleyStringResponse, String>());
    }

    public void executeReset(DisposableObserver<String> observer, String code, String password) {
        super.execute(observer, new UseCaseParam<>(new CaptchaResetParam(code, password)));
    }

    static class CaptchaResetParam {
        private String code;
        private String password;

        CaptchaResetParam(String code, String password) {
            this.code = code;
            this.password = password;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
