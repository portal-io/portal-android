package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.common.response.ListTabResponse;
import com.whaley.biz.setting.response.RedemptionCodeRespose;
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

public class RequestCouponList extends UseCase<RedemptionCodeRespose, RequestCouponList.RequestCouponListParam> {

    public RequestCouponList(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<RedemptionCodeRespose> buildUseCaseObservable(final RequestCouponListParam param) {
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
                }).flatMap(new Function<UserModel, ObservableSource<RedemptionCodeRespose>>() {
            @Override
            public ObservableSource<RedemptionCodeRespose> apply(@NonNull UserModel userModel) throws Exception {
                return getRepositoryManager().obtainRemoteService(PayAPI.class)
                        .couponList(userModel.getAccount_id(), param.getPage(),
                                param.getSize(),
                                RequestUtils.getPaySign(userModel.getAccount_id(), ""+param.getPage(),
                                        ""+param.getSize()));
            }
        }) .map(new ResponseFunction<RedemptionCodeRespose,RedemptionCodeRespose>());
    }

    public static class RequestCouponListParam{

        private int page;
        private int size;

        public RequestCouponListParam(int page, int size){
            this.page = page;
            this.size = size;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

    }

}

