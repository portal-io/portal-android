package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.interactor.function.RetryWhenRefreshTokenFunction;
import com.whaley.biz.user.model.CaptchaModel;
import com.whaley.biz.user.model.TokenModel;
import com.whaley.biz.user.model.response.WhaleyStringResponse;
import com.whaley.biz.user.ui.repository.EditPhoneRepository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: qxw
 * Date:2017/8/4
 * Introduction:
 */

public class MergeUpdatePhone extends BaseUseCase<CaptchaModel, UpdateSendSms.UpdateSendSmsParam> {

    @UseCase
    UpdateValidateMobile updateValidateMobile;


    @UseCase
    TokenSMS TokenSMS;

    @UseCase
    UpdateSendSms sendSms;

    public MergeUpdatePhone(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<CaptchaModel> buildUseCaseObservable(final UseCaseParam<UpdateSendSms.UpdateSendSmsParam> param) {
        return updateValidateMobile.buildUseCaseObservable(param.getParam().getNewMobile())
                .flatMap(new Function<WhaleyStringResponse, ObservableSource<CaptchaModel>>() {
                    @Override
                    public ObservableSource<CaptchaModel> apply(@NonNull WhaleyStringResponse s) throws Exception {
                        return TokenSMS.buildUseCaseObservable(null)
                                .flatMap(new Function<TokenModel, ObservableSource<CaptchaModel>>() {
                                    @Override
                                    public ObservableSource<CaptchaModel> apply(@NonNull TokenModel tokenBean) throws Exception {
                                        param.getParam().setToken(tokenBean.getSms_token());
                                        return sendSms.buildUseCaseObservable(param);
                                    }
                                });
                    }
                })
                .retryWhen(new RetryWhenRefreshTokenFunction<Observable<Throwable>, CaptchaModel>() {
                               @Override
                               public ObservableSource<CaptchaModel> retryWhenThrowable() {
                                   return buildUseCaseObservable(param);
                               }
                           }
                );
    }


    public void executeSendSMS(DisposableObserver<CaptchaModel> observer) {
        EditPhoneRepository registerRepository = getRepositoryManager().obtainMemoryService(EditPhoneRepository.class);
        UpdateSendSms.UpdateSendSmsParam sendSMSParam = new UpdateSendSms.UpdateSendSmsParam();
        sendSMSParam.setNewMobile(registerRepository.getNewPhone());
        sendSMSParam.setCaptcha(registerRepository.getImageCode());
        execute(observer, new UseCaseParam(sendSMSParam));
    }

}
