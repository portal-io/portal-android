package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonCMSFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.setting.util.RequestUtils;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.biz.setting.api.PayAPI;
import com.whaley.biz.setting.model.RedemptionCodeModel;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/7/25.
 */

public class ConvertRedeem extends UseCase<RedemptionCodeModel, ConvertRedeem.ConvertRedeemParam> {

    public ConvertRedeem(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<RedemptionCodeModel> buildUseCaseObservable(final ConvertRedeemParam param) {
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
                }).flatMap(new Function<UserModel, ObservableSource<CMSResponse<RedemptionCodeModel>>>() {
            @Override
            public ObservableSource<CMSResponse<RedemptionCodeModel>> apply(@NonNull UserModel userModel) throws Exception {
                return getRepositoryManager().obtainRemoteService(PayAPI.class)
                        .userDoRedeem(userModel.getAccount_id(), param.getRedeemCode(),
                                RequestUtils.getPaySign(userModel.getAccount_id(), param.getRedeemCode())
                        );
            }
        }).map(new CommonCMSFunction<CMSResponse<RedemptionCodeModel>, RedemptionCodeModel>());
    }

    public static class ConvertRedeemParam{

        private String redeemCode;

        public ConvertRedeemParam(String redeemCode){
            this.redeemCode = redeemCode;
        }

        public String getRedeemCode() {
            return redeemCode;
        }

        public void setRedeemCode(String redeemCode) {
            this.redeemCode = redeemCode;
        }

    }

}

