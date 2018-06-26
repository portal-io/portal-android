package com.whaley.biz.pay.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.pay.PayApi;
import com.whaley.biz.pay.model.user.UserModel;
import com.whaley.biz.pay.response.CouponInfoResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/9/7 19:06.
 */

public class GetCouponInfo extends UseCase<CouponInfoResponse, String> {

    CheckLogin checkLogin;

    public GetCouponInfo() {
        super(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        checkLogin = new CheckLogin();
    }

    @Override
    public Observable<CouponInfoResponse> buildUseCaseObservable(final String code) {
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<CouponInfoResponse>>() {
                    @Override
                    public ObservableSource<CouponInfoResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager().obtainRemoteService(PayApi.class).getCouponInfo(code, userModel.getAccount_id());
                    }
                })
                .map(new ResponseFunction<CouponInfoResponse, CouponInfoResponse>());
    }
}
