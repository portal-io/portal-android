package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.model.TokenModel;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.api.UserVrApi;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date: 2017/7/14
 * 获取短信token
 */

public class TokenSMS extends UseCase<TokenModel, String> {

    public TokenSMS(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<TokenModel> buildUseCaseObservable(String s) {
        Observable<TokenModel> observable =
                getRepositoryManager()
                        .obtainRemoteService(UserVrApi.class)
                        .getToken(UserManager.getInstance().getDeviceId())
                        .map(new CommonFunction<WhaleyResponse<TokenModel>, TokenModel>());
        return observable;
    }


}
