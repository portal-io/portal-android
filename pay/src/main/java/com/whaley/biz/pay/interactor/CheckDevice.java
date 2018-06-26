package com.whaley.biz.pay.interactor;

import com.whaley.biz.common.interactor.CommonCMSFunction;
import com.whaley.biz.pay.UserPlayDeviceApi;
import com.whaley.biz.pay.model.user.UserModel;
import com.whaley.biz.pay.response.DeviceResponse;
import com.whaley.biz.sign.Sign;
import com.whaley.biz.sign.SignType;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.MD5Util;
import com.whaleyvr.core.network.http.HttpManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/9/19.
 */

public class CheckDevice extends UseCase<Boolean, String> {

    CheckLogin checkLogin;

    public CheckDevice() {
        super(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        checkLogin = new CheckLogin();
    }

    @Override
    public Observable<Boolean> buildUseCaseObservable(final String s) {
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<DeviceResponse>>() {
                    @Override
                    public ObservableSource<DeviceResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager().obtainRemoteService(UserPlayDeviceApi.class).query(userModel.getAccount_id(), AppUtil.getDeviceId(),
                                MD5Util.getMD5String(userModel.getAccount_id() + AppUtil.getDeviceId() +
                                        Sign.getSign(HttpManager.getInstance().isTest() ? SignType.TYPE_TEST_WHALEYVR_PAY : SignType.TYPE_WHALEYVR_PAY)));
                    }
                })
                .map(new CommonCMSFunction<DeviceResponse, Boolean>());
    }

}
