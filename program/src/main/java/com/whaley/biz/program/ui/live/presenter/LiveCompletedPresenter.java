package com.whaley.biz.program.ui.live.presenter;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.GetContributeRank;
import com.whaley.biz.program.interactor.GetLiveDetail;
import com.whaley.biz.program.interactor.GetMemberContributeRank;
import com.whaley.biz.program.interactor.GetTotalContribute;
import com.whaley.biz.program.model.GiftListCountModel;
import com.whaley.biz.program.model.GiftMemberCountModel;
import com.whaley.biz.program.model.GiftTotalCountModel;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.model.StatQueryModel;
import com.whaley.biz.program.model.response.GiftListCountResponse;
import com.whaley.biz.program.model.response.GiftMemberCountResponse;
import com.whaley.biz.program.model.response.GiftTotalCountResponse;
import com.whaley.biz.program.model.response.LiveDetailsResponse;
import com.whaley.biz.program.ui.uimodel.ContributionViewModel;
import com.whaley.biz.program.ui.uimodel.LiveCompletedViewModel;
import com.whaley.biz.program.ui.uimodel.MemberRankItemViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.StrUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/10/16 12:56.
 */

public class LiveCompletedPresenter extends BasePagePresenter<LiveCompletedPresenter.LiveCompletedView> implements BIConstants {

    private static final int COLOR_HAS_RANK_NAME_TEXT = AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color12);

    private static final int COLOR_HAS_RANK_DES_TEXT = AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color12);

    private static final int COLOR_TEXT = AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color12);

    @UseCase
    GetLiveDetail getLiveDetail;

    @UseCase
    GetTotalContribute getTotalContribute;

    @UseCase
    GetContributeRank getContributeRank;

    @UseCase
    GetMemberContributeRank getMemberContributeRank;

    String code;

    String backgroundImage;

    LiveCompletedViewModel liveCompletedViewModel;

    Disposable disposable;

    public LiveCompletedPresenter(LiveCompletedView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        backgroundImage = arguments.getString(ProgramConstants.KEY_PARAM_LIVE_BACKGROUND_IMAGE);
        code = arguments.getString(ProgramConstants.KEY_PARAM_LIVE_CODE);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        getUIView().loadBackImage(backgroundImage);
        doRequests();
    }

    public void onRetry() {
        doRequests();
    }

    private void doRequests() {
        dispose();
        disposable = getLiveDetail()
                .flatMap(new Function<LiveCompletedViewModel, ObservableSource<LiveCompletedViewModel>>() {
                    @Override
                    public ObservableSource<LiveCompletedViewModel> apply(@NonNull LiveCompletedViewModel viewModel) throws Exception {
                        if (!viewModel.isContribution()) {
                            return Observable.just(viewModel);
                        }
                        return getZipData(viewModel);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<LiveCompletedViewModel>(getUIView(), true, true) {
                    @Override
                    public void onNext(@NonNull LiveCompletedViewModel viewModel) {
                        super.onNext(viewModel);
                        liveCompletedViewModel = viewModel;
                        getUIView().update(liveCompletedViewModel);
                        browse(true);
                    }
                });
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        dispose();
    }

    private Observable<LiveCompletedViewModel> getZipData(final LiveCompletedViewModel viewModel) {
        Observable<GiftTotalCountResponse> totalContributionObserver = getTotalContribute.buildUseCaseObservable(code)
                .subscribeOn(Schedulers.io());
        Observable<GiftListCountResponse> contributionRank = getContributeRank.buildUseCaseObservable(code)
                .subscribeOn(Schedulers.io());


        final List<ObservableSource<?>> observableSources = new ArrayList<>();
        observableSources.add(totalContributionObserver);
        observableSources.add(contributionRank);
        if (viewModel.isHasMemberRank()) {
            Observable<GiftMemberCountResponse> memberContributionRank = getMemberContributeRank.buildUseCaseObservable(code)
                    .subscribeOn(Schedulers.io());
            observableSources.add(memberContributionRank);
        }

        return Observable
                .zip(new Iterable<ObservableSource<?>>() {
                    @Override
                    public Iterator<ObservableSource<?>> iterator() {
                        return observableSources.iterator();
                    }
                }, new Function<Object[], LiveCompletedViewModel>() {
                    @Override
                    public LiveCompletedViewModel apply(@NonNull Object[] objects) throws Exception {
                        GiftTotalCountResponse giftTotalCountResponse = (GiftTotalCountResponse) objects[0];
                        GiftListCountResponse giftListCountResponse = (GiftListCountResponse) objects[1];
                        GiftMemberCountResponse giftMemberCountResponse = null;
                        if (objects.length == 3) {
                            giftMemberCountResponse = (GiftMemberCountResponse) objects[2];
                        }
                        GiftTotalCountModel totalData = giftTotalCountResponse.getData();
                        viewModel.setFansCount(totalData.getTotalUserCount());
                        viewModel.setGiftCount(totalData.getTotalGiftCount());
                        viewModel.setwCoinCount(totalData.getTotalWhaleyCurrentCount());
                        List<LiveCompletedViewModel.TabItemViewModel> tabItemViewModels = new ArrayList<>();
                        viewModel.setTabItemViewModels(tabItemViewModels);
                        List<GiftListCountModel.UserCountModel> userCountModels = giftListCountResponse.getList();
                        if (userCountModels != null && userCountModels.size() > 0) {
                            LiveCompletedViewModel.TabItemViewModel tabItemViewModel = new LiveCompletedViewModel.TabItemViewModel();
                            tabItemViewModel.setType(LiveCompletedViewModel.TabItemViewModel.TYPE_CONTRIBUTION_RANK);
                            tabItemViewModel.setTitle("贡献榜");
                            tabItemViewModels.add(tabItemViewModel);
                            List<ContributionViewModel> contributionList = new ArrayList<>();
                            tabItemViewModel.setDatas(contributionList);
                            for (GiftListCountModel.UserCountModel userCountModel : userCountModels) {
                                ContributionViewModel contributionViewModel = new ContributionViewModel();
                                contributionViewModel.setName(userCountModel.getNickName());
                                contributionViewModel.setContribute("贡献" + userCountModel.getWhaleyCurrentCount() + "鲸币");
                                contributionViewModel.setImage(userCountModel.getUserHeadUrl());
                                contributionViewModel.setRank("NO." + userCountModel.getRank());
                                contributionList.add(contributionViewModel);
                            }
                        }
                        if (giftMemberCountResponse != null) {
                            List<GiftMemberCountModel.MemberCountModel> memberCountsModels = giftMemberCountResponse.getList();
                            if (memberCountsModels != null && memberCountsModels.size() > 0) {
                                LiveCompletedViewModel.TabItemViewModel tabItemViewModel = new LiveCompletedViewModel.TabItemViewModel();
                                tabItemViewModel.setType(LiveCompletedViewModel.TabItemViewModel.TYPE_MEMBER_RANK);
                                tabItemViewModel.setTitle("成员榜");
                                tabItemViewModels.add(tabItemViewModel);
                                List<MemberRankItemViewModel> memberRankViewModels = new ArrayList<>();
                                tabItemViewModel.setDatas(memberRankViewModels);
                                for (GiftMemberCountModel.MemberCountModel memberCountModel : memberCountsModels) {
                                    MemberRankItemViewModel viewModel = createMemberRankItemViewModel(memberCountModel);
                                    memberRankViewModels.add(viewModel);
                                }
                            }
                        }
                        return viewModel;
                    }
                });
    }

    private MemberRankItemViewModel createMemberRankItemViewModel(GiftMemberCountModel.MemberCountModel memberCountModel) {
        MemberRankItemViewModel viewModel = new MemberRankItemViewModel();
        viewModel.setName(memberCountModel.getMemberName());
        viewModel.setImageUrl(memberCountModel.getMemberHeadUrl());
        boolean isNoRank = false;
        switch (memberCountModel.getRank()) {
            case 1:
                viewModel.setImageBg(R.mipmap.bg_contribute_head_first);
                break;
            case 2:
                viewModel.setImageBg(R.mipmap.bg_contribute_head_seconed);
                break;
            case 3:
                viewModel.setImageBg(R.mipmap.bg_contribute_head_thrid);
                break;
            case 0:
                isNoRank = true;
            default:
                viewModel.setImageBg(0);
                break;
        }
        viewModel.setRank(isNoRank ? "暂无排名" : "NO." + memberCountModel.getRank());
        viewModel.setNameColor(COLOR_HAS_RANK_NAME_TEXT);
        viewModel.setContributeColor(COLOR_HAS_RANK_DES_TEXT);
        viewModel.setRankColor(COLOR_HAS_RANK_NAME_TEXT);
        if (isNoRank) {
            viewModel.setContributeText("还没有收到粉丝打赏");
        } else {
            List<GiftMemberCountModel.MemberCountModel.UserModel> userModels = memberCountModel.getUserList();
            if (userModels != null && userModels.size() > 0) {
                GiftMemberCountModel.MemberCountModel.UserModel userModel = userModels.get(0);
                viewModel.setContributeText("@" + userModel.getNickName() + " 等" + String.valueOf(memberCountModel.getUserCount()) + "位粉丝贡献了" + String.valueOf(memberCountModel.getWhaleyCurrentCount()) + "鲸币");
            } else {
                viewModel.setContributeText("还没有收到粉丝打赏");
            }
        }
        return viewModel;
    }

    private Observable<LiveCompletedViewModel> getLiveDetail() {
        GetLiveDetail.Param param = new GetLiveDetail.Param();
        param.setCode(code);
        return getLiveDetail.buildUseCaseObservable(param)
                .map(new Function<LiveDetailsResponse, LiveCompletedViewModel>() {
                    @Override
                    public LiveCompletedViewModel apply(@NonNull LiveDetailsResponse liveDetailsResponse) throws Exception {
                        LiveDetailsModel data = liveDetailsResponse.getData();
                        StatQueryModel statQueryModel = data.getStat();
                        LiveCompletedViewModel viewModel = new LiveCompletedViewModel();
                        viewModel.convert(data);
                        viewModel.setPlayCount(statQueryModel.getPlayCount());
                        boolean isContribution = data.getIsGift() != 0 && !StrUtil.isEmpty(data.getGiftTemplate());
                        viewModel.setContribution(isContribution);
                        viewModel.setHasMemberRank(data.isTip());
                        return viewModel;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


    public interface LiveCompletedView extends BasePageView {
        void loadBackImage(String url);

        void update(LiveCompletedViewModel viewModel);

    }

    //================bi埋点==================//

    @Override
    protected LogInfoParam.Builder getGeneralBuilder(String eventId) {
        if (liveCompletedViewModel != null) {
            LiveDetailsModel model = liveCompletedViewModel.getSeverModel();
            if (model != null) {
                LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                        .setEventId(eventId)
                        .setCurrentPageId(ROOT_LIVE_END)
                        .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, model.getCode())
                        .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, model.getDisplayName());
                return builder;
            }
        }
        return null;
    }

    @Override
    protected boolean isNeedBrowseBuried() {
        return true;
    }

}
