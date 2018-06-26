package com.whaley.biz.program.ui.live.presenter;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.whaley.biz.common.exception.ResponseErrorException;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.biz.program.R;
import com.whaley.biz.program.interactor.GetMyContribute;
import com.whaley.biz.program.model.GiftMyCountModel;
import com.whaley.biz.program.model.response.GiftMyCountResponse;
import com.whaley.biz.program.ui.uimodel.MyContributionViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.UseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/10/13 15:58.
 */

public class MyContributionPresenter extends BasePagePresenter<MyContributionPresenter.MyContributionView> {

    private static final int COLOR_TEXT = AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color15);

    ForegroundColorSpan colorSpan = new ForegroundColorSpan(COLOR_TEXT);

    @UseCase
    GetMyContribute getMyContribute;

    MyContributionViewModel contributionViewModel;

    Disposable disposable;

    public MyContributionPresenter(MyContributionView view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        if (contributionViewModel != null) {
            getUIView().update(contributionViewModel);
        } else {
            getMyContribution();
        }
    }

    public void onRetry() {
        getMyContribution();
    }

    public void onRefresh() {
        getMyContribution();
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        dispose();
    }

    private void getMyContribution() {
        dispose();
        disposable = getMyContribute.buildUseCaseObservable(null)
                .map(getConvertFunc())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<MyContributionViewModel>(getUIView(), true, contributionViewModel == null) {
                    @Override
                    public void onNext(@NonNull MyContributionViewModel viewModel) {
                        super.onNext(viewModel);
                        contributionViewModel = viewModel;
                        getUIView().update(contributionViewModel);
                        getUIView().stopRefresh();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getUIView().stopRefresh();
                        super.onError(e);
                    }
                });
    }

    private Function<GiftMyCountResponse, MyContributionViewModel> getConvertFunc() {
        return new Function<GiftMyCountResponse, MyContributionViewModel>() {
            @Override
            public MyContributionViewModel apply(@NonNull GiftMyCountResponse giftMyCountResponse) throws Exception {
                GiftMyCountModel data = giftMyCountResponse.getData();
                if (data == null) {
                    throw new ResponseErrorException();
                }
                MyContributionViewModel viewModel = new MyContributionViewModel();
                viewModel.setImageUrl(data.getUserHeadUrl());
                viewModel.setLiveCount(formatLiveCount(data.getLiveCount()));
                viewModel.setGiftCount(formatGiftCount(data.getGiftCount()));
                viewModel.setWcoinCount(formatWCoinCount(data.getWhaleyCurrentCount()));
                viewModel.setRank(String.valueOf(data.getTotalRank()));
                List<GiftMyCountModel.FavoriteMemberModel> memberModels = data.getFavoriteMemberList();
                if (memberModels != null && memberModels.size() > 0) {
                    List<MyContributionViewModel.UserModel> favoriteUsers = new ArrayList<>();
                    viewModel.setFavoriteUsers(favoriteUsers);
                    for (GiftMyCountModel.FavoriteMemberModel memberModel : memberModels) {
                        MyContributionViewModel.UserModel userModel = new MyContributionViewModel.UserModel();
                        userModel.setImageUrl(memberModel.getMemberHeadUrl());
                        userModel.setUserName(memberModel.getMemberName());
                        favoriteUsers.add(userModel);
                    }
                }
                return viewModel;
            }
        };
    }

    private CharSequence formatLiveCount(int count) {
        return format(count, "总计 ", " 场直播");
    }

    private CharSequence formatGiftCount(int count) {
        return format(count, "送出 ", " 件礼物");
    }

    private CharSequence formatWCoinCount(int count) {
        return format(count, "价值 ", " 鲸币");
    }

    private CharSequence format(int count, CharSequence prefix, CharSequence endfix) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(prefix);
        SpannableString spannableStr = new SpannableString(String.valueOf(count));
        spannableStr.setSpan(colorSpan, 0, spannableStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableStr);
        builder.append(endfix);
        return builder;
    }

    public interface MyContributionView extends BasePageView {
        void update(MyContributionViewModel contributionViewModel);

        void stopRefresh();
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

}
