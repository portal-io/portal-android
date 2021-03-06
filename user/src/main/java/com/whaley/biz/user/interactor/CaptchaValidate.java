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

/**
 * Author: qxw
 * Date:2017/7/19
 * Introduction:忘记密码验证码验证
 */

public class CaptchaValidate extends BaseUseCase<String, String> {
    public CaptchaValidate(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(UseCaseParam<String> param) {
        return getRepositoryManager()
                .obtainRemoteService(ForgotApi.class)
                .validateCaptcha(param.getParam())
                .map(new CommonFunction<WhaleyStringResponse,String>());
    }


}
