package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.api.ForgotApi;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: qxw
 * Date:2017/7/19
 * Introduction:忘记密码手机号验证
 */

public class CaptchaUserPhone extends BaseUseCase<UserModel, String> {


    public CaptchaUserPhone(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    public void execute(DisposableObserver<UserModel> observer, String phone) {
        UseCaseParam<String> useCaseParam = new UseCaseParam<>(phone);
        super.execute(observer, useCaseParam);
    }

    @Override
    public Observable<UserModel> buildUseCaseObservable(UseCaseParam<String> param) {
        return getRepositoryManager()
                .obtainRemoteService(ForgotApi.class)
                .user(param.getParam())
                .map(new CommonFunction<WhaleyResponse<UserModel>, UserModel>());
    }
}
