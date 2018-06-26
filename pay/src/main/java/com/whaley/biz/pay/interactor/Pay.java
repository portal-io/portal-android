package com.whaley.biz.pay.interactor;

import com.ipaynow.plugin.manager.route.dto.ResponseParams;
import com.ipaynow.plugin.manager.route.impl.ReceivePayResult;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.pay.PayApi;
import com.whaley.biz.pay.PayPlugin;
import com.whaley.biz.pay.PayUtil;
import com.whaley.biz.pay.model.NormalOrderModel;
import com.whaley.biz.pay.model.PayEventModel;
import com.whaley.biz.pay.model.user.UserModel;
import com.whaley.biz.pay.response.NormalOrderResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/7/20
 * Introduction:创建第三方支付订单
 */

public class Pay extends UseCase<PayEventModel, Pay.PayParam> {

    public Pay(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<PayEventModel> buildUseCaseObservable(final PayParam param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<NormalOrderResponse>>() {
                    @Override
                    public ObservableSource<NormalOrderResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager().obtainRemoteService(PayApi.class)
                                .createNormalOrder(userModel.getAccount_id(), param.goodsNo,
                                        param.goodsType,
                                        param.price,
                                        param.payMethod,
                                        PayUtil.getSign(userModel.getAccount_id(), param.goodsNo,
                                                param.goodsType,
                                                param.price,
                                                param.payMethod));
                    }
                })
                .map(new CommonFunction<NormalOrderResponse, NormalOrderModel>())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<NormalOrderModel, ObservableSource<PayEventModel>>() {
                    @Override
                    public ObservableSource<PayEventModel> apply(@NonNull final NormalOrderModel normalOrderModel) throws Exception{
                        return Observable.create(new ObservableOnSubscribe<PayEventModel>() {
                            @Override
                            public void subscribe(@NonNull final ObservableEmitter<PayEventModel> e) throws Exception {
                                PayPlugin.getInstance().getmIpaynowplugin().setCallResultReceiver(new ReceivePayResult() {
                                    @Override
                                    public void onIpaynowTransResult(ResponseParams responseParams) {
                                        String respCode = responseParams.respCode;
                                        final String errorCode = responseParams.errorCode;
                                        final String errorMsg = responseParams.respMsg;
                                        if ("00".equals(respCode)) {
                                            PayEventModel payEventModel = new PayEventModel();
                                            payEventModel.setPayed(true);
                                            payEventModel.setOrderNo(normalOrderModel.getOrderNo());
                                            payEventModel.setGoodsNo(param.getGoodsNo());
                                            e.onNext(payEventModel);
                                            e.onComplete();
                                        }else{
                                            PayEventModel payEventModel = new PayEventModel();
                                            payEventModel.setPayed(false);
                                            payEventModel.setErrorCode(errorCode);
                                            payEventModel.setErrorMsg(errorMsg);
                                            payEventModel.setOrderNo(normalOrderModel.getOrderNo());
                                            payEventModel.setGoodsNo(param.getGoodsNo());
                                            e.onNext(payEventModel);
                                            e.onComplete();
                                        }
                                    }
                                }).pay(normalOrderModel.getOrderToPayStr());
                            }
                        });
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<PayEventModel>() {
                    @Override
                    public void accept(@NonNull PayEventModel payEventModel) throws Exception {
                        if(payEventModel.isPayed()) {
                            getRepositoryManager().obtainRemoteService(PayApi.class).appPayFinishBack(payEventModel.getOrderNo(), param.payMethod,
                                    PayUtil.getSign(payEventModel.getOrderNo(), param.payMethod)).subscribe();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Function<Throwable, ObservableSource<PayEventModel>>() {
                    @Override
                    public ObservableSource<PayEventModel> apply(@NonNull final Throwable throwable) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<PayEventModel>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<PayEventModel> e) throws Exception {
                                PayEventModel payEventModel = new PayEventModel();
                                payEventModel.setPayed(false);
                                payEventModel.setGoodsNo(param.getGoodsNo());
                                e.onNext(payEventModel);
                                e.onComplete();
                            }
                        });
                    }
                });
    }

    public static class PayParam {
        private String goodsNo;
        private String goodsType;
        private String price;
        private String payMethod;

        public PayParam(String goodsNo, String goodsType, String price, String payMethod){
            this.goodsNo = goodsNo;
            this.goodsType = goodsType;
            this.price = price;
            this.payMethod = payMethod;
        }

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPayMethod() {
            return payMethod;
        }

        public void setPayMethod(String payMethod) {
            this.payMethod = payMethod;
        }
    }
}
