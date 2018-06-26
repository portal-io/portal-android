package com.whaley.biz.pay.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.pay.currency.response.CurrencyCostResponse;
import com.whaley.biz.pay.interactor.UserCost;
import com.whaley.biz.pay.model.UserCostModel;
import com.whaley.biz.pay.model.UserCostResultModel;
import com.whaley.core.router.Executor;

import io.reactivex.annotations.NonNull;

/**
 * Created by dell on 2017/10/16.
 */

@Route(path = "/pay/service/usercost")
public class UserCostService implements Executor<UserCostModel> {

    @Override
    public void excute(UserCostModel userCostModel, final Callback callback) {
        UserCost userCost = new UserCost();
        userCost.execute(new ErrorHandleObserver<CurrencyCostResponse>() {
            @Override
            public void onNext(@NonNull CurrencyCostResponse currencyCostResponse) {
                //
            }

            @Override
            public void onComplete() {
                if (callback != null) {
                    callback.onCall(new UserCostResultModel(true));
                }
            }

            @Override
            public void onFinalError(Throwable throwable) {
                if (callback != null) {
                    callback.onCall(new UserCostResultModel(false));
                }
            }

            @Override
            public void onStatusError(int status, String message) {

            }

            @Override
            public void onStatusError(int status, String message, String subCode, CurrencyCostResponse data) {
                if (callback != null) {
                    callback.onCall(new UserCostResultModel(false, message, status, subCode));
                }
            }

            @Override
            public void onNoDataError() {
                if (callback != null) {
                    callback.onCall(new UserCostResultModel(false));
                }
            }
        }, userCostModel);
    }

    @Override
    public void init(Context context) {

    }

}
