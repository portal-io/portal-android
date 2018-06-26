package com.whaley.biz.pay.interactor;

import com.whaley.biz.common.interactor.ResponseCMSFunction;
import com.whaley.biz.pay.currency.api.CurrencyAPI;
import com.whaley.biz.pay.currency.response.CurrencyCostResponse;
import com.whaley.biz.pay.model.UserCostModel;
import com.whaley.biz.pay.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/10/16.
 */

public class UserCost extends UseCase<CurrencyCostResponse, UserCostModel> {

    public UserCost() {
        super(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
    }


    public UserCost(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }


    @Override
    public Observable<CurrencyCostResponse> buildUseCaseObservable(final UserCostModel userCostModel) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<CurrencyCostResponse>>() {
                    @Override
                    public ObservableSource<CurrencyCostResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager().obtainRemoteService(CurrencyAPI.class).userCost(userModel.getAccount_id(),
                                userModel.getNickname(), userModel.getAvatar(), userCostModel.getBuyType(), userCostModel.getBuyParams(),
                                userCostModel.getBizParams());
                    }
                })
                .map(new ResponseCMSFunction<CurrencyCostResponse, CurrencyCostResponse>());
    }

}
