package com.whaley.biz.pay.interactor;

import com.whaley.biz.common.exception.ResponseErrorException;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.pay.PayApi;
import com.whaley.biz.pay.PayUtil;
import com.whaley.biz.pay.model.program.PayResultModel;
import com.whaley.biz.pay.model.user.UserModel;
import com.whaley.biz.pay.response.PayResultResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Author: qxw
 * Date:2017/7/20
 * Introduction:查询是否购买
 */

public class IsPayed extends UseCase<Boolean, IsPayed.GoodsPayedParam> {

    public IsPayed() {
    }

    public IsPayed(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }


    @Override
    public Observable<Boolean> buildUseCaseObservable(final GoodsPayedParam param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<PayResultResponse>>() {
                    @Override
                    public ObservableSource<PayResultResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager()
                                .obtainRemoteService(PayApi.class)
                                .goodsPayed(userModel.getAccount_id(), param.goodsNo, param.goodsType,
                                        PayUtil.getSign(userModel.getAccount_id(), param.goodsNo, param.goodsType));
                    }
                })
                .map(new CommonFunction<PayResultResponse, PayResultModel>())
                .flatMap(new Function<PayResultModel, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(@NonNull final PayResultModel payResultModel) throws Exception {
                        Observable observable = Observable.create(new ObservableOnSubscribe() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                                if (payResultModel == null) {
                                    e.onError(new ResponseErrorException());
                                } else {
                                    e.onNext(payResultModel.isResult());
                                }
                            }
                        });
                        return observable;
                    }
                });
    }

    public static class GoodsPayedParam {

        private String goodsNo;
        private String goodsType;


        public String getGoodsNo() {
            return goodsNo;
        }

        public void setGoodsNo(String goodsNo) {
            this.goodsNo = goodsNo;
        }

        public String getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(String goodsType) {
            this.goodsType = goodsType;
        }
    }
}
