package com.whaley.biz.pay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.whaley.biz.pay.model.CouponModel;
import com.whaley.biz.pay.model.ThirdPayModel;
import com.whaley.biz.sign.Sign;
import com.whaley.biz.sign.SignType;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.RemoteRepository;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.MD5Util;
import com.whaleyvr.core.network.http.HttpManager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/7/20
 * Introduction:
 */

public class PayUtil {

    public static String getSign(String... param) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : param) {
            stringBuilder.append(s);
        }
        stringBuilder.append(Sign.getSign(HttpManager.getInstance().isTest() ? SignType.TYPE_TEST_WHALEYVR_PAY : SignType.TYPE_WHALEYVR_PAY));
        return MD5Util.getMD5String(stringBuilder.toString());
    }

    public final static String STR_PARAM_OBJECT = "str_param_object";
    public static Fragment showPayDialog(FragmentManager fragmentManager, ThirdPayModel thirdPayModel) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag("thirdPay");
        if (null != fragment) {
            ft.remove(fragment);
        }
        Fragment f = Router.getInstance().buildObj("/pay/ui/paydialog").getObj();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STR_PARAM_OBJECT, thirdPayModel);
        f.setArguments(bundle);
        ft.add(f, "thirdPay");
        ft.commitAllowingStateLoss();
        return f;
    }
//    public static Fragment showPayDialog(FragmentManager fm, String json) {
//        FragmentTransaction ft = fm.beginTransaction();
//        Fragment fragment = fm.findFragmentByTag("thirdPay");
//        if (null != fragment) {
//            ft.remove(fragment);
//        }
//        Fragment f = Router.getInstance().buildObj("/pay/ui/paydialog").getObj();
//        Bundle bundle = new Bundle();
//        bundle.putString(STR_PARAM_OBJECT, json);
//        f.setArguments(bundle);
//        ft.add(f, "thirdPay");
//        ft.commitAllowingStateLoss();
//        return f;
//    }

//    public static Fragment showPayDialog(FragmentManager fm, List<CouponModel> packsCoupons, String content, int displayMode, boolean isUnity, String type) {
//        FragmentTransaction ft = fm.beginTransaction();
//        Fragment fragment = fm.findFragmentByTag("thirdPay");
//        if (null != fragment) {
//            ft.remove(fragment);
//        }
//        Fragment f = Router.getInstance().buildObj("/pay/ui/paydialog").getObj();
//        Bundle bundle = new Bundle();
//        ThirdPayModel thirdPayModel = new ThirdPayModel();
//        thirdPayModel.setPacksCoupons(packsCoupons);
//        thirdPayModel.setContent(content);
//        thirdPayModel.setDisplayMode(displayMode);
//        thirdPayModel.setUnity(isUnity);
//        thirdPayModel.setType(type);
//        bundle.putString(STR_PARAM_OBJECT, GsonUtil.getGson().toJson(thirdPayModel));
//        f.setArguments(bundle);
//        ft.add(f, "thirdPay");
//        ft.commitAllowingStateLoss();
//        return f;
//    }

    public static void payMethod(final ThirdPayMethodListener thirdPayMethodListener) {
        UseCase useCase = Router.getInstance().buildObj("/pay/usecase/paymethodlist").getObj();
        useCase.setRepositoryManager(RepositoryManager.create(RemoteRepository.getInstance(), null, null));
        useCase.buildUseCaseObservable(null).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver() {
                    @Override
                    public void onNext(@NonNull Object o) {
                        if (thirdPayMethodListener != null) {
                            thirdPayMethodListener.onComeBack();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (thirdPayMethodListener != null) {
                            thirdPayMethodListener.onComeBack();
                        }
                    }

                    @Override
                    public void onComplete() {
                        //
                    }
                });
    }



    public interface ThirdPayMethodListener {
        void onComeBack();
    }

    /**
     * 存储支付模式
     *
     * @param payMethod
     */
    public static void setPayMethod(String payMethod) {
        SharedPreferences preferences = AppContextProvider.getInstance().getContext().getSharedPreferences(
                PayConstants.PRE_PAY_METHOD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PayConstants.KEY_PAY_METHOD, payMethod);
        editor.commit();
    }

    public static String getPayMethod() {
        SharedPreferences preference = AppContextProvider.getInstance().getContext().getSharedPreferences(PayConstants.PRE_PAY_METHOD,
                Context.MODE_PRIVATE);
        return preference.getString(PayConstants.KEY_PAY_METHOD, "");
    }

    /**
     * 转化金钱数目 ===  分>元
     *
     * @param fen
     * @return
     */
    public static String fromFenToYuan(final String fen) {
        String yuan = "";
        final int MULTIPLIER = 100;
        try {
            yuan = new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
        } catch (Exception e) {
            //
        }
        return yuan;
    }

    public static String getDateFromMileSeconds(long seconds) {
        if (seconds <= 0)
            return "";
        else {
            Date date = new Date();
            try {
                date.setTime(seconds);
            } catch (NumberFormatException nfe) {
                return "";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
            return sdf.format(date);
        }
    }

}
