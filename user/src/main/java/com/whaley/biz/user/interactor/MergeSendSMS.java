package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.model.CaptchaModel;
import com.whaley.biz.user.model.TokenModel;
import com.whaley.biz.user.ui.repository.RegisterRepository;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: qxw
 * Date: 2017/7/17
 * 获取短信token和发送短信验证码一体化
 */

public class MergeSendSMS extends UseCase<CaptchaModel, SendSMS.SendSMSParam> {

    @com.whaley.core.inject.annotation.UseCase
    TokenSMS TokenSMS;
    @com.whaley.core.inject.annotation.UseCase
    SendSMS sendSMS;

    public MergeSendSMS(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);

    }


    @Override
    public Observable<CaptchaModel> buildUseCaseObservable(final SendSMS.SendSMSParam sendSMSParam) {
        return TokenSMS.buildUseCaseObservable(null)
                .flatMap(new Function<TokenModel, ObservableSource<CaptchaModel>>() {
                    @Override
                    public ObservableSource<CaptchaModel> apply(@NonNull TokenModel tokenBean) throws Exception {
                        sendSMSParam.setToken(tokenBean.getSms_token());
                        return sendSMS.buildUseCaseObservable(new UseCaseParam(sendSMSParam));
                    }
                });
    }


    public void executeSendSMS(DisposableObserver<CaptchaModel> observer) {
        RegisterRepository registerRepository = getRepositoryManager().obtainMemoryService(RegisterRepository.class);
        SendSMS.SendSMSParam sendSMSParam = new SendSMS.SendSMSParam();
        sendSMSParam.setMobile(registerRepository.getPhone());
        sendSMSParam.setCaptcha(registerRepository.getImageCode());
        execute(observer, sendSMSParam);

    }
}
