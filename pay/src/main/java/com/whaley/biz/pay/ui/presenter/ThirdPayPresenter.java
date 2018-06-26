package com.whaley.biz.pay.ui.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.pay.PayPlugin;
import com.whaley.biz.pay.PayUtil;
import com.whaley.biz.pay.currency.response.CurrencyCostResponse;
import com.whaley.biz.pay.event.PayEvent;
import com.whaley.biz.pay.interactor.Pay;
import com.whaley.biz.pay.interactor.UserCost;
import com.whaley.biz.pay.model.CouponModel;
import com.whaley.biz.pay.model.PayEventModel;
import com.whaley.biz.pay.model.ThirdPayModel;
import com.whaley.biz.pay.model.UserCostModel;
import com.whaley.biz.pay.model.program.PayMethodModel;
import com.whaley.biz.pay.ui.adapter.ThirdPayView;
import com.whaley.biz.pay.ui.repository.ThirdPayRepository;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.NetworkUtils;

import java.util.List;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/21.
 */

public class ThirdPayPresenter extends BasePagePresenter<ThirdPayView> implements BIConstants {

    public final static String STR_PARAM_OBJECT = "str_param_object";

    @Repository
    ThirdPayRepository repository;

    @UseCase
    Pay pay;

    @UseCase
    UserCost userCost;

    private Disposable disposable;

    public ThirdPayRepository getRepository() {
        return repository;
    }

    public ThirdPayPresenter(ThirdPayView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
//            String json = arguments.getString(STR_PARAM_OBJECT);
//            if (!TextUtils.isEmpty(json)) {
            ThirdPayModel thirdPayModel = arguments.getParcelable(STR_PARAM_OBJECT);
            if (thirdPayModel != null) {
                getRepository().setProgramcode(thirdPayModel.getCode());
                getRepository().setCouponModels(thirdPayModel.getPacksCoupons());
                getRepository().setContent(thirdPayModel.getContent());
                getRepository().setDisplayMode(thirdPayModel.getDisplayMode());
                getRepository().setUnity(thirdPayModel.isUnity());
                getRepository().setWhaleyMoney(thirdPayModel.getWhaleyMoney());
                getRepository().setFromType(thirdPayModel.getType());
            }
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
        if (payMethodModel.getWhaleyCurrency() == 1) {
            getRepository().setWhaleyCurrency(true);
        }
        if (getRepository().isAlipay() && getRepository().isWechat() && getRepository().isWhaleyCurrency()) {
            getRepository().setThreeKindsPay(true);
        }
    }

    public void alipay() {
        onPay("alipay");
    }

    public void wechat() {
        onPay("weixin");
    }

    public void jinbi() {
        final CouponModel couponModel = getRepository().getCouponModel();
        if (couponModel.getJingbiPrice() > getRepository().getWhaleyMoney()) {
            getUIView().showToast("鲸币不足以购买当前节目");
            return;
        }
        UserCostModel userCostModel = new UserCostModel();
        userCostModel.setBuyType("REDUCE_COUPON");
        userCostModel.setBizParams(couponModel.getDisplayName());
        userCostModel.setBuyParams(couponModel.getCode());
        dispose();
        disposable = userCost.buildUseCaseObservable(userCostModel).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CurrencyCostResponse>() {
                    @Override
                    public void onNext(CurrencyCostResponse currencyCostResponse) {
                        PayEventModel payEventModel = new PayEventModel();
                        payEventModel.setPayed(true);
                        payEventModel.setGoodsNo(couponModel.getCode());
                        getRepository().setPayEventModel(payEventModel);
                        getRepository().setWhaleyMoney(getRepository().getWhaleyMoney() - couponModel.getJingbiPrice());
                        onPaySuccess();
                        if (getUIView() != null) {
                            getUIView().onPayfinish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NotLoggedInErrorException) {
                            TitleBarActivity.goPage(getStater(), 0, "/user/ui/login");
                        } else {
                            PayEventModel payEventModel = new PayEventModel();
                            payEventModel.setPayed(false);
                            payEventModel.setGoodsNo(couponModel.getCode());
                            getRepository().setPayEventModel(payEventModel);
                            if (getUIView() != null) {
                                getUIView().onPayfinish();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void onPay(String payMethod) {
        if (!NetworkUtils.isNetworkAvailable()) {
            if (getUIView() != null) {
                getUIView().showToast("网络异常，创建订单失败");
            }
            return;
        }
        final CouponModel couponModel = getRepository().getCouponModel();
        Pay.PayParam payParam = new Pay.PayParam(couponModel.getCode(), "coupon", couponModel.getPrice(), payMethod);
        dispose();
        disposable = pay.buildUseCaseObservable(payParam).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PayEventModel>() {
                    @Override
                    public void onNext(@NonNull PayEventModel payEventModel) {
                        getRepository().setPayEventModel(payEventModel);
                        if (payEventModel != null && payEventModel.isPayed()) {
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
                            PayEventModel payEventModel = new PayEventModel();
                            payEventModel.setPayed(false);
                            payEventModel.setGoodsNo(couponModel.getCode());
                            getRepository().setPayEventModel(payEventModel);
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

    public void onItemClick(int position) {
        CouponModel coupon = getRepository().getCouponModels().get(position);
        if (!coupon.isSelect) {
            getRepository().getCouponModel().isSelect = false;
            coupon.isSelect = true;
            getRepository().setCouponModel(coupon);
            getUIView().updateContentViewVisible(!"content_packge".equals(coupon.getRelatedType()));
        }
        getUIView().updataView();
    }

    public void onPackgeClick(CouponModel couponModel) {
        Bundle bundle = new Bundle();
        bundle.putString("key_param_id", couponModel.getRelatedCode());
        Router.getInstance().buildNavigation("/program/ui/package")
                .setStarter(getStater())
                .with(bundle)
                .withInt(RouterConstants.KEY_ACTIVITY_TYPE, 1)
                .navigation();
    }

    public void onDismiss() {
        PayPlugin.getInstance().recycle();
        PayEventModel payEventModel = getRepository().getPayEventModel();
        if (payEventModel == null) {
            payEventModel = new PayEventModel();
            payEventModel.setPayed(false);
        }
        payEventModel.setGoodsNo(getRepository().getCouponModel().getCode());
        payEventModel.setProgramCode(getRepository().getProgramcode());
        EventController.postEvent(new PayEvent(payEventModel));
    }

    public void setDisplayMode(int displayMode) {
        repository.setDisplayMode(displayMode);
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
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
                .putCurrentPagePropKeyValue(ROOT_RELATED_CODE, getRepository().getCouponModel().getRelatedCode())
                .putCurrentPagePropKeyValue(BIConstants.ROOT_VOUVHER_TYPE, getRepository().getCouponModel().getRelatedType())
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
                .putCurrentPagePropKeyValue(ROOT_RELATED_CODE, getRepository().getCouponModel().getRelatedCode())
                .putCurrentPagePropKeyValue(BIConstants.ROOT_VOUVHER_TYPE, getRepository().getCouponModel().getRelatedType())
                .setNextPageId(ROOT_PAY_DETAILS);
        Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
    }


}
