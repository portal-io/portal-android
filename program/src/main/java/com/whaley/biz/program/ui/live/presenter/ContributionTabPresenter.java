package com.whaley.biz.program.ui.live.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.presenter.TabIndicatorPresenter;
import com.whaley.biz.common.ui.view.TabIndicatorView;
import com.whaley.biz.common.ui.viewmodel.TabItemViewModel;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.CheckLogin;
import com.whaley.biz.program.interactor.GetTotalContribute;
import com.whaley.biz.program.model.GiftTotalCountModel;
import com.whaley.biz.program.model.response.GiftTotalCountResponse;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.ui.live.ContributionRankFragment;
import com.whaley.biz.program.ui.live.MemberRankFragment;
import com.whaley.biz.program.ui.live.MyContributionFragment;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.UseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/10/12 15:31.
 */

public class ContributionTabPresenter extends TabIndicatorPresenter<ContributionTabPresenter.ContributionTabView> {

    private static final int TYPE_MY_CONTRIBUTION = 0;
    private static final int TYPE_CONTRIBUTION_RANK = 1;
    private static final int TYPE_MEMBER_RANK = 2;


    private static final String APPEND_STR_FIRST = "本场直播共 ";
    private static final String APPEND_STR_SECOND = " 位粉丝，共送出了 ";
    private static final String APPEND_STR_THRID = " 件礼物，总价值 ";
    private static final String APPEND_STR_FOURTH = " 鲸币。";
    private static final int COLOR_TEXT = AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color15);

    @UseCase
    GetTotalContribute getTotalContribute;

    @UseCase
    CheckLogin checkLogin;

    String code;

    boolean hasMemberRank;

    public ContributionTabPresenter(ContributionTabView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        code = arguments.getString(ProgramConstants.KEY_PARAM_LIVE_CODE);
        hasMemberRank = arguments.getBoolean(ProgramConstants.KEY_PARAM_HAS_MEMBER_RANK, false);
    }

    private Observable<SpannableStringBuilder> getTotalContribution() {
        return getTotalContribute.buildUseCaseObservable(code)
                .map(new Function<GiftTotalCountResponse, SpannableStringBuilder>() {
                    @Override
                    public SpannableStringBuilder apply(@NonNull GiftTotalCountResponse giftTotalCountResponse) throws Exception {
                        GiftTotalCountModel data = giftTotalCountResponse.getData();
                        int fansCount = data.getTotalUserCount();
                        int giftCount = data.getTotalGiftCount();
                        int wcoinCount = data.getTotalWhaleyCurrentCount();
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(COLOR_TEXT);

                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        builder.append(APPEND_STR_FIRST);

                        SpannableString spannableStr = new SpannableString(String.valueOf(fansCount));
                        spannableStr.setSpan(colorSpan, 0, spannableStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.append(spannableStr);

                        builder.append(APPEND_STR_SECOND);

                        colorSpan = new ForegroundColorSpan(COLOR_TEXT);
                        spannableStr = new SpannableString(String.valueOf(giftCount));
                        spannableStr.setSpan(colorSpan, 0, spannableStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.append(spannableStr);

                        builder.append(APPEND_STR_THRID);

                        colorSpan = new ForegroundColorSpan(COLOR_TEXT);
                        spannableStr = new SpannableString(String.valueOf(wcoinCount));
                        spannableStr.setSpan(colorSpan, 0, spannableStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.append(spannableStr);

                        builder.append(APPEND_STR_FOURTH);
                        return builder;
                    }
                });
    }

    private Observable<Boolean> isLogin() {
        return checkLogin.buildUseCaseObservable(null)
                .map(new Function<UserModel, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull UserModel userModel) throws Exception {
                        return userModel.isLoginUser();
                    }
                });
    }

    @Override
    protected void getTitles() {
        Observable
                .zip(getTotalContribution(), isLogin(), new BiFunction<SpannableStringBuilder, Boolean, TitlesModel>() {
                    @Override
                    public TitlesModel apply(@NonNull SpannableStringBuilder spannableStringBuilder, @NonNull Boolean isLogin) throws Exception {
                        List<TabItemViewModel> tabItemViewModels = new ArrayList<>();
                        TabItemViewModel item;
                        if (isLogin) {
                            item = new TabItemViewModel("我的贡献", TYPE_MY_CONTRIBUTION);
                            tabItemViewModels.add(item);
                        }
                        item = new TabItemViewModel("贡献榜", TYPE_CONTRIBUTION_RANK);
                        tabItemViewModels.add(item);
                        if (hasMemberRank) {
                            item = new TabItemViewModel("成员榜", TYPE_MEMBER_RANK);
                            tabItemViewModels.add(item);
                        }
                        getTabIndicatorRepository().setTitles(tabItemViewModels);
                        TitlesModel tilesModel = new TitlesModel();
                        tilesModel.spannableString = spannableStringBuilder;
                        tilesModel.tabItemViewModels = tabItemViewModels;
                        return tilesModel;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<TitlesModel>(getUIView(), true, true) {
                    @Override
                    public void onNext(@NonNull TitlesModel titlesModel) {
                        super.onNext(titlesModel);
                        getUIView().updateTotalDes(titlesModel.spannableString);
                        getTabIndicatorRepository().setTitles(titlesModel.tabItemViewModels);
                        getTabIndicatorRepository().setCount(getTabIndicatorRepository().getTitles().size());
                        getUIView().updateTabs();
                        getUIView().getEmptyDisplayView().showContent();
                    }
                });
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        TabItemViewModel item = getTabIndicatorRepository().getTitles().get(position);
        int type = (int) item.getData();
        switch (type) {
            case TYPE_MY_CONTRIBUTION:
                fragment = new MyContributionFragment();
                break;
            case TYPE_CONTRIBUTION_RANK:
                fragment = new ContributionRankFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ProgramConstants.KEY_PARAM_LIVE_CODE, code);
                fragment.setArguments(bundle);
                break;
            default:
                fragment = new MemberRankFragment();
                bundle = new Bundle();
                bundle.putString(ProgramConstants.KEY_PARAM_LIVE_CODE, code);
                fragment.setArguments(bundle);
                break;
        }

        return fragment;
    }

    public interface ContributionTabView extends TabIndicatorView {
        void updateTotalDes(CharSequence text);
    }

    private class TitlesModel {
        private SpannableStringBuilder spannableString;

        private List<TabItemViewModel> tabItemViewModels;
    }
}
