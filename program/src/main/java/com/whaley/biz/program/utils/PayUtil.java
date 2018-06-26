package com.whaley.biz.program.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.whaley.biz.program.model.CouponModel;
import com.whaley.biz.program.model.pay.ThirdPayModel;
import com.whaley.biz.sign.Sign;
import com.whaley.biz.sign.SignType;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.RemoteRepository;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.MD5Util;
import com.whaley.core.utils.StrUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/21.
 */

public class PayUtil {

    public final static String STR_PARAM_OBJECT = "str_param_object";

    public static Fragment showPayDialog(FragmentManager fm, String json) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag("thirdPay");
        if (null != fragment) {
            ft.remove(fragment);
        }
        Fragment f = Router.getInstance().buildObj("/pay/ui/paydialog").getObj();
        Bundle bundle = new Bundle();
        bundle.putString(STR_PARAM_OBJECT, json);
        f.setArguments(bundle);
        ft.add(f, "thirdPay");
        ft.commitAllowingStateLoss();
        return f;
    }

    public static Fragment showPayDialog(FragmentManager fm, String code, List<CouponModel> packsCoupons, String content, int displayMode, boolean isUnity, String type) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag("thirdPay");
        if (null != fragment) {
            ft.remove(fragment);
        }
        Fragment f = Router.getInstance().buildObj("/pay/ui/paydialog").getObj();
        Bundle bundle = new Bundle();
        ThirdPayModel thirdPayModel = new ThirdPayModel();
        thirdPayModel.setCode(code);
        thirdPayModel.setPacksCoupons(packsCoupons);
        thirdPayModel.setContent(content);
        thirdPayModel.setDisplayMode(displayMode);
        thirdPayModel.setUnity(isUnity);
        thirdPayModel.setType(type);
        bundle.putString(STR_PARAM_OBJECT, GsonUtil.getGson().toJson(thirdPayModel));
        f.setArguments(bundle);
        ft.add(f, "thirdPay");
        ft.commitAllowingStateLoss();
        return f;
    }

    public static void payMethod(final ThirdPayMethodListener thirdPayMethodListener) {
        UseCase useCase = Router.getInstance().buildObj("/pay/usecase/paymethodlist").getObj();
        useCase.setRepositoryManager(RepositoryManager.create(RemoteRepository.getInstance(), null, null));
        useCase.buildUseCaseObservable(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

    public static String getSign(String... param) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : param) {
            stringBuilder.append(s);
        }
        stringBuilder.append(Sign.getSign(SignType.TYPE_TEST_WHALEYVR_PAY));
        return MD5Util.getMD5String(stringBuilder.toString());
    }


    public static String add(String original, String add) {
        if (StrUtil.isEmpty(original)) {
            original = add;
        } else {
            original = original + "," + add;
        }
        return original;
    }

}
