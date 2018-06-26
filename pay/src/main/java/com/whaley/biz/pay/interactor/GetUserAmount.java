package com.whaley.biz.pay.interactor;

import com.whaley.biz.common.interactor.CommonCMSFunction;
import com.whaley.biz.pay.currency.api.CurrencyAPI;
import com.whaley.biz.pay.currency.response.UserAmountResponse;
import com.whaley.biz.pay.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/10/12.
 */

public class GetUserAmount extends UseCase<Integer, String> {

    public GetUserAmount() {
        super(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Integer> buildUseCaseObservable(final String param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<UserAmountResponse>>() {
                    @Override
                    public ObservableSource<UserAmountResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager().obtainRemoteService(CurrencyAPI.class)
                                .userAmount(userModel.getAccount_id());
                    }
                }).map(new CommonCMSFunction<UserAmountResponse, Integer>());
    }

}
