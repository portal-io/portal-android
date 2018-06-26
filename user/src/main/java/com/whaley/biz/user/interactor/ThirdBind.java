package com.whaley.biz.user.interactor;


import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.interactor.function.RetryWhenRefreshTokenFunction;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.api.UserVrApi;
import com.whaley.biz.user.model.response.WhaleyStringResponse;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/7/19
 * Introduction:
 */

public class ThirdBind extends BaseUseCase<String, UserModel> {


    public ThirdBind(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }


    @Override
    public Observable<String> buildUseCaseObservable(final UseCaseParam<UserModel> param) {

        return getRepositoryManager()
                .obtainRemoteService(UserVrApi.class)
                .bind(UserManager.getInstance().getDeviceId(),
                        UserManager.getInstance().getAccesstoken(),
                        param.getParam().getOpenid(),
                        param.getParam().getOrgin(),
                        param.getParam().getUnionid(),
                        param.getParam().getNickname(),
                        param.getParam().getAvatar(),
                        param.getParam().getArea(),
                        param.getParam().getGender())
                .map(new CommonFunction<WhaleyStringResponse, String>())
                .retryWhen(new RetryWhenRefreshTokenFunction<Observable<Throwable>, String>() {
                    @Override
                    public ObservableSource<String> retryWhenThrowable() {
                        return buildUseCaseObservable(param);
                    }
                });
    }

}
