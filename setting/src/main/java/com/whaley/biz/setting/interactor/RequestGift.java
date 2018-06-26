package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.biz.sign.SignType;
import com.whaley.biz.sign.SignUtil;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.biz.setting.api.GiftAPI;
import com.whaley.biz.setting.response.GiftListResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/7/25.
 */

public class RequestGift extends BaseUseCase<GiftListResponse, RequestGift.RequestGiftParam> {

    public RequestGift(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<GiftListResponse> buildUseCaseObservable(final UseCaseParam<RequestGiftParam> param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .map(new Function<UserModel, UserModel>() {
                    @Override
                    public UserModel apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return userModel;
                        }
                        throw new NotLoggedInErrorException("未登录");
                    }
                }).flatMap(new Function<UserModel, ObservableSource<GiftListResponse>>() {
            @Override
            public ObservableSource<GiftListResponse> apply(@NonNull UserModel userModel) throws Exception {
                return getRepositoryManager().obtainRemoteService(GiftAPI.class)
                        .requestMyGift(userModel.getAccount_id(), param.getParam().getTimestamp());
            }
        }) .map(new ResponseFunction<GiftListResponse,GiftListResponse>());
    }

    public static class RequestGiftParam{

        private String timestamp;

        public RequestGiftParam(String timestamp){
            this.timestamp = timestamp;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

    }

}

