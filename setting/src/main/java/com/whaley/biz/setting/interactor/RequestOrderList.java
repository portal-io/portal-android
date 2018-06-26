package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.util.RequestUtils;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.biz.setting.ui.repository.PayDetailRepository;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.biz.setting.api.PayAPI;
import com.whaley.biz.setting.response.PayDetailsResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/7/25.
 */

public class RequestOrderList extends BaseUseCase<PayDetailsResponse, RequestOrderList.RequestOrderListParam> {

    public RequestOrderList(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<PayDetailsResponse> buildUseCaseObservable(final UseCaseParam<RequestOrderListParam> param) {
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
                }).flatMap(new Function<UserModel, ObservableSource<PayDetailsResponse>>() {
            @Override
            public ObservableSource<PayDetailsResponse> apply(@NonNull UserModel userModel) throws Exception {
                return getRepositoryManager().obtainRemoteService(PayAPI.class)
                        .userCouponOrderList(userModel.getAccount_id(), param.getParam().getPage(),
                                param.getParam().getSize(),
                                RequestUtils.getPaySign(userModel.getAccount_id(), ""+param.getParam().getPage(),
                                        ""+param.getParam().getSize()));
            }
        }).map(new ResponseFunction<PayDetailsResponse, PayDetailsResponse>())
                .doOnNext(new Consumer<PayDetailsResponse>() {
                    @Override
                    public void accept(@NonNull PayDetailsResponse payDetailsResponse) throws Exception {
                        String price = SettingUtil.fromFenToYuan(payDetailsResponse.getData().getSumAmount());
                        getRepositoryManager().obtainMemoryService(PayDetailRepository.class)
                                .setBuyNum(String.format(AppContextProvider.getInstance().getContext().getString(R.string.tv_pay_num),
                                payDetailsResponse.getData().getTotalNum(),
                                payDetailsResponse.getData().getTotalNum(), price));
                    }
                });
    }

    public static class RequestOrderListParam{

        private int page;
        private int size;

        public RequestOrderListParam(int page, int size){
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

