package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.api.CurrencyAPI;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.biz.setting.response.CurrencyDetailsResponse;
import com.whaley.biz.setting.ui.repository.CurrencyDetailRepository;
import com.whaley.biz.setting.ui.repository.PayDetailRepository;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/10/13.
 */

public class RequestCurrencyList extends UseCase<CurrencyDetailsResponse, RequestCurrencyList.RequestCurrencyListParam> {

    public RequestCurrencyList(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<CurrencyDetailsResponse> buildUseCaseObservable(final RequestCurrencyList.RequestCurrencyListParam param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .map(new Function<UserModel, UserModel>() {
                    @Override
                    public UserModel apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return userModel;
                        }
                        throw new NotLoggedInErrorException("未登录");
                    }
                }).flatMap(new Function<UserModel, ObservableSource<CurrencyDetailsResponse>>() {
                    @Override
                    public ObservableSource<CurrencyDetailsResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager().obtainRemoteService(CurrencyAPI.class)
                                .userWhaleyCurrencyOrderList(userModel.getAccount_id(), param.getPage(),
                                        param.getSize());
                    }
                }).map(new ResponseFunction<CurrencyDetailsResponse, CurrencyDetailsResponse>())
                .doOnNext(new Consumer<CurrencyDetailsResponse>() {
                    @Override
                    public void accept(@NonNull CurrencyDetailsResponse currencyDetailsResponse) throws Exception {
                        String price = SettingUtil.fromFenToYuan(currencyDetailsResponse.getData().getPriceAmount());
                        getRepositoryManager().obtainMemoryService(CurrencyDetailRepository.class)
                                .setBuyNum(String.format(AppContextProvider.getInstance().getContext().getString(R.string.tv_currency_num),
                                        currencyDetailsResponse.getData().getTotalNum(),
                                        currencyDetailsResponse.getData().getWhaleyCurrencyAmount(), price));
                    }
                });
    }

    public static class RequestCurrencyListParam{

        private int page;
        private int size;

        public RequestCurrencyListParam(int page, int size){
            this.page = page;
            this.size = size;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

    }

}


