package com.whaley.biz.program.ui.live.presenter;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.CheckLogin;
import com.whaley.biz.program.interactor.GetContributeRank;
import com.whaley.biz.program.model.GiftListCountModel;
import com.whaley.biz.program.model.response.GiftListCountResponse;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.ui.uimodel.ContributionViewModel;
import com.whaley.biz.program.ui.uimodel.HeadContributionViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.UseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/10/12 17:48.
 */

public class ContributionRankPresenter extends LoadPresenter<ContributionRankPresenter.ContributionRankView> {

    static final String STR_RANK_PREFIX = "NO.";

    private static final String APPEND_STR_FIRST = "贡献";
    private static final String APPEND_STR_SECOND = "鲸币";

    private static final int COLOR_TEXT = AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color15);

    ForegroundColorSpan colorSpan = new ForegroundColorSpan(COLOR_TEXT);

    @UseCase
    GetContributeRank getContributeRank;

    @UseCase
    CheckLogin checkLogin;

    String code;

    ContributionRankViewModel rankViewModelCache;

    public ContributionRankPresenter(ContributionRankView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null)
            code = arguments.getString(ProgramConstants.KEY_PARAM_LIVE_CODE);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        if (rankViewModelCache != null) {
            getUIView().updateOnRefresh(rankViewModelCache.loaderData.getLoadListData().getViewDatas());
            getUIView().showMyContribution(rankViewModelCache.myContribution,rankViewModelCache.loginUserPosition);
        } else {
            onRefresh();
        }
    }


    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
        refresh(getContributeRank.buildUseCaseObservable(code));
    }

    private Observable<UserModel> checkLogin() {
        return checkLogin.buildUseCaseObservable(null);
    }

    @Override
    protected Observable<LoaderUseCase.LoaderData> buildRefreshObservable(Observable observable) {
        return Observable
                .zip(observable
                        .map(getRefresh().buildFunction()), checkLogin(), new BiFunction<LoaderUseCase.LoaderData, UserModel, ContributionRankViewModel>() {
                    @Override
                    public ContributionRankViewModel apply(@NonNull LoaderUseCase.LoaderData loaderData, @NonNull UserModel userModel) throws Exception {
                        ContributionRankViewModel viewModel = new ContributionRankViewModel();
                        viewModel.loaderData = loaderData;
                        viewModel.userModel = userModel;
                        return viewModel;
                    }
                })
                .map(new Function<ContributionRankViewModel, ContributionRankViewModel>() {
                    @Override
                    public ContributionRankViewModel apply(@NonNull ContributionRankViewModel rankViewModel) throws Exception {
                        LoaderUseCase.LoaderData loaderData = rankViewModel.loaderData;
                        UserModel userModel = rankViewModel.userModel;
                        GiftListCountResponse response = (GiftListCountResponse) loaderData.getResponse();
                        List<GiftListCountModel.UserCountModel> userCountModels = response.getList();
                        List uiViewModels = new ArrayList();
                        HeadContributionViewModel headContributionViewModel = new HeadContributionViewModel();
                        uiViewModels.add(headContributionViewModel);
                        int loginUserPosition = 0;
                        for (GiftListCountModel.UserCountModel userCountModel : userCountModels) {
                            boolean isLoginUser = false;
                            String uid = userCountModel.getUid();
                            if (userModel.isLoginUser() && userModel.getAccount_id().equals(uid)) {
                                isLoginUser = true;
                            }
                            int rank = userCountModel.getRank();
                            if (rank > 3) {
                                loginUserPosition++;
                                ContributionViewModel contributionViewModel = createContributionViewModel(userCountModel);
                                uiViewModels.add(contributionViewModel);
                                if (isLoginUser) {
                                    contributionViewModel.setLoginUser(true);
                                    rankViewModel.myContribution = contributionViewModel;
                                    rankViewModel.loginUserPosition = loginUserPosition;
                                }
                                continue;
                            }

                            List<ContributionViewModel> contributionViewModels = headContributionViewModel.getContributionViewModels();
                            if (contributionViewModels == null) {
                                contributionViewModels = new ArrayList<>();
                                headContributionViewModel.setContributionViewModels(contributionViewModels);
                            }
                            ContributionViewModel contributionViewModel = createContributionViewModel(userCountModel);
                            contributionViewModels.add(contributionViewModel);
                            if (isLoginUser) {
                                contributionViewModel.setLoginUser(true);
                                rankViewModel.myContribution = contributionViewModel;
                                rankViewModel.loginUserPosition = loginUserPosition;
                            }
                        }
                        List<ContributionViewModel> contributionViewModels = headContributionViewModel.getContributionViewModels();
                        if (contributionViewModels != null && contributionViewModels.size() < 3) {
                            int fakeCount = 3 - contributionViewModels.size();
                            for (int i = 0, j = fakeCount - 1; i <= j; i++) {
                                ContributionViewModel viewModel = new ContributionViewModel();
                                viewModel.setName("暂时没有人");
                                viewModel.setRealData(false);
                                viewModel.setContribute(null);
                                contributionViewModels.add(viewModel);
                            }
                        }
                        loaderData.getLoadListData().setViewDatas(uiViewModels);
                        return rankViewModel;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ContributionRankViewModel, LoaderUseCase.LoaderData>() {
                    @Override
                    public LoaderUseCase.LoaderData apply(@NonNull ContributionRankViewModel rankViewModel) throws Exception {
                        rankViewModelCache = rankViewModel;
                        getUIView().showMyContribution(rankViewModel.myContribution, rankViewModel.loginUserPosition);
                        return rankViewModel.loaderData;
                    }
                });
    }

    private ContributionViewModel createContributionViewModel(GiftListCountModel.UserCountModel userCountModel) {
        ContributionViewModel viewModel = new ContributionViewModel();
        viewModel.setName(userCountModel.getNickName());
        viewModel.setImage(userCountModel.getUserHeadUrl());
        viewModel.setRank(STR_RANK_PREFIX + userCountModel.getRank());
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(APPEND_STR_FIRST);
        SpannableString spannableStr = new SpannableString(String.valueOf(userCountModel.getWhaleyCurrentCount()));
        spannableStr.setSpan(colorSpan, 0, spannableStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableStr);
        builder.append(APPEND_STR_SECOND);
        viewModel.setContribute(builder);
        return viewModel;
    }


    private class ContributionRankViewModel {
        private LoaderUseCase.LoaderData loaderData;
        private UserModel userModel;
        private ContributionViewModel myContribution;
        private int loginUserPosition = -1;
    }


    public interface ContributionRankView<DATA> extends LoaderView<List<DATA>> {
        void showMyContribution(ContributionViewModel viewModel, int position);
    }
}
