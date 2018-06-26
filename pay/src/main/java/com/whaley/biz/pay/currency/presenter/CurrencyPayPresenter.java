package com.whaley.biz.pay.currency.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.pay.PayPlugin;
import com.whaley.biz.pay.PayUtil;
import com.whaley.biz.pay.currency.CurrencyPayView;
import com.whaley.biz.pay.currency.model.CurrencyModel;
import com.whaley.biz.pay.currency.repository.CurrencyPayRepository;
import com.whaley.biz.pay.event.CurrencyPayEvent;
import com.whaley.biz.pay.interactor.Pay;
import com.whaley.biz.pay.model.PayEventModel;
import com.whaley.biz.pay.model.program.PayMethodModel;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.NetworkUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/21.
 */

public class CurrencyPayPresenter extends BasePagePresenter<CurrencyPayView> implements BIConstants {

    public final static String STR_PARAM_OBJECT = "str_param_object";

    @Repository
    CurrencyPayRepository repository;

    @UseCase
    Pay pay;

    private Disposable disposable;

    public CurrencyPayRepository getRepository() {
        return repository;
    }

    public CurrencyPayPresenter(CurrencyPayView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            String json = arguments.getString(STR_PARAM_OBJECT);
            if (!TextUtils.isEmpty(json)) {
                CurrencyModel currencyModel = GsonUtil.getGson().fromJson(json, CurrencyModel.class);
                getRepository().setCurrencyModel(currencyModel);
            }
            String payMethod = PayUtil.getPayMethod();
            if (TextUtils.isEmpty(payMethod)) {
                getRepository().setAlipay(true);
                getRepository().setWechat(true);
                return;
            }
            PayMethodModel payMethodModel = GsonUtil.getGson().fromJson(payMethod, PayMethodModel.class);
            if (payMethodModel.getAlipay() == 1) {
                getRepository().setAlipay(true);
            }
            if (payMethodModel.getWeixin() == 1) {
                getRepository().setWechat(true);
            }
        }
    }

    public void alipay() {
        onPay("alipay");
    }

    public void wechat() {
        onPay("weixin");
    }

    private void onPay(String payMethod) {
        if (!NetworkUtils.isNetworkAvailable()) {
            if (getUIView() != null) {
                getUIView().showToast("网络异常，创建订单失败");
            }
            return;
        }
        final CurrencyModel currencyModel = getRepository().getCurrencyModel();
        Pay.PayParam payParam = new Pay.PayParam(currencyModel.getCode(), "whaleyCurrency", getRepository().getRealPrice(), payMethod);
        dispose();
        disposable = pay.buildUseCaseObservable(payParam).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PayEventModel>() {
                    @Override
                    public void onNext(@NonNull PayEventModel payEventModel) {
                        getRepository().setPay(payEventModel.isPayed());
                        if(payEventModel!=null&&payEventModel.isPayed()) {
                            onPaySuccess();
                        }
                        if (getUIView() != null) {
                            getUIView().onPayfinish();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof NotLoggedInErrorException) {
                            TitleBarActivity.goPage(getStater(), 0, "/user/ui/login");
                        } else {
                            getRepository().setPay(false);
                            if (getUIView() != null) {
                                getUIView().onPayfinish();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        onClickPay();
    }

    public void onDismiss() {
        PayPlugin.getInstance().recycle();
        if(getRepository().isPay()){
            EventController.postEvent(new CurrencyPayEvent());
        }
    }

    private void dispose(){
        if(disposable!=null){
            disposable.dispose();
            disposable=null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispose();
    }

    //==============================BI埋点====================================//
    /**
     * 购买成功
     */
    private void onPaySuccess() {
        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                .setEventId(PAY_SUCCESS)
                .setCurrentPageId(ROOT_PAY_DETAILS)
                .putCurrentPagePropKeyValue(ROOT_RELATED_CODE, getRepository().getCurrencyModel().getCode())
                .putCurrentPagePropKeyValue(BIConstants.ROOT_VOUVHER_TYPE, "whaleyCurrency")
                .setNextPageId(ROOT_PAY_DETAILS);
        Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
    }

    /**
     * 点击购买
     */
    private void onClickPay() {
        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                .setEventId(ORDER_FORM)
                .setCurrentPageId(ROOT_PAY_DETAILS)
                .putCurrentPagePropKeyValue(ROOT_RELATED_CODE, getRepository().getCurrencyModel().getCode())
                .putCurrentPagePropKeyValue(BIConstants.ROOT_VOUVHER_TYPE, "whaleyCurrency")
                .setNextPageId(ROOT_PAY_DETAILS);
        Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
    }

}
