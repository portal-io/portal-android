package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.setting.util.RequestUtils;
import com.whaley.biz.setting.model.RedeemCodeDateModel;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.biz.setting.ui.repository.RedemptionCodeRepository;
import com.whaley.biz.setting.ui.viewmodel.RedemptionCodeViewModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.biz.setting.api.PayAPI;
import com.whaley.biz.setting.model.RedeemCodeModel;
import com.whaley.biz.setting.response.RedeemCodeResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/7/25.
 */

public class RequestRedeem extends UseCase<RedeemCodeDateModel, String> {

    public RequestRedeem(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<RedeemCodeDateModel> buildUseCaseObservable(String param) {
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
                }).flatMap(new Function<UserModel, ObservableSource<RedeemCodeResponse>>() {
            @Override
            public ObservableSource<RedeemCodeResponse> apply(@NonNull UserModel userModel) throws Exception {
                return getRepositoryManager().obtainRemoteService(PayAPI.class)
                        .listUserUnRedeemed(userModel.getAccount_id(), userModel.getMobile(),
                                RequestUtils.getPaySign(userModel.getAccount_id(), userModel.getMobile()));
            }
        }).map(new CommonFunction<RedeemCodeResponse, RedeemCodeDateModel>())
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<RedeemCodeDateModel>() {
                    @Override
                    public void accept(@NonNull RedeemCodeDateModel redeemCodeDateModel) throws Exception {
                        changeRedemptionCodeItemList();
                        if (redeemCodeDateModel == null || redeemCodeDateModel.getRedeemCodeList() == null
                                || redeemCodeDateModel.getRedeemCodeList().size() == 0) {
                            return;
                        }
                        getRepositoryManager().obtainMemoryService(RedemptionCodeRepository.class).getRedemptionCodeItemBox().isHaveCode = true;
                        List<RedeemCodeModel> redeemList = redeemCodeDateModel.getRedeemCodeList();
                        for (int i = 0; i < redeemList.size(); i++) {
                            RedeemCodeModel redeem = redeemList.get(i);
                            RedemptionCodeViewModel redemptionCodeItemCode = new RedemptionCodeViewModel(redeem, RedemptionCodeViewModel.TYPE_CODE);
                            if (i == redeemList.size() - 1) {
                                redemptionCodeItemCode.isLast = true;
                            }
                            getRepositoryManager().obtainMemoryService(RedemptionCodeRepository.class).getRedemptionList().add(redemptionCodeItemCode);
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    private void changeRedemptionCodeItemList(){
        List<RedemptionCodeViewModel> redemptionList = new ArrayList<>();
        RedemptionCodeViewModel redemptionCodeItemBox = new RedemptionCodeViewModel(RedemptionCodeViewModel.TYPE_BOX);
        getRepositoryManager().obtainMemoryService(RedemptionCodeRepository.class).setRedemptionCodeItemBox(redemptionCodeItemBox);
        redemptionList.add(redemptionCodeItemBox);
        getRepositoryManager().obtainMemoryService(RedemptionCodeRepository.class).setRedemptionList(redemptionList);
    }

}

