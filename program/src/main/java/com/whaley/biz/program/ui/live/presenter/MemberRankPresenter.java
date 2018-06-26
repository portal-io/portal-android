package com.whaley.biz.program.ui.live.presenter;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.GetMemberContributeRank;
import com.whaley.biz.program.model.GiftMemberCountModel;
import com.whaley.biz.program.model.response.GiftMemberCountResponse;
import com.whaley.biz.program.ui.uimodel.MemberRankItemViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.UseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/10/13 17:01.
 */

public class MemberRankPresenter extends LoadPresenter<LoaderView> {

    private static final int COLOR_NORANK_TEXT = AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color8);

    private static final int COLOR_HAS_RANK_NAME_TEXT = AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color3);

    private static final int COLOR_HAS_RANK_DES_TEXT = AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color5);

    private static final int COLOR_TEXT = AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color15);

    ForegroundColorSpan colorSpan = new ForegroundColorSpan(COLOR_TEXT);

    @UseCase
    GetMemberContributeRank getMemberContributeRank;

    String code;

    public MemberRankPresenter(LoaderView view) {
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
        onRefresh();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
        getMemberRanks();
    }

    private void getMemberRanks() {
        refresh(getMemberContributeRank.buildUseCaseObservable(code));
    }

    @Override
    protected Consumer<LoaderUseCase.LoaderData> getConvertConsumer() {
        return new Consumer<LoaderUseCase.LoaderData>() {
            @Override
            public void accept(@NonNull LoaderUseCase.LoaderData loaderData) throws Exception {
                GiftMemberCountResponse response = (GiftMemberCountResponse) loaderData.getResponse();
                GiftMemberCountModel data = response.getData();
                List<GiftMemberCountModel.MemberCountModel> memberList = data.getMemberCountList();
                if (memberList != null && memberList.size() > 0) {
                    List<MemberRankItemViewModel> viewModels = new ArrayList<>();
                    for (GiftMemberCountModel.MemberCountModel memberCountModel : memberList) {
                        viewModels.add(createViewModel(memberCountModel));
                    }
                    loaderData.getLoadListData().setViewDatas(viewModels);
                }
            }
        };
    }

    private MemberRankItemViewModel createViewModel(GiftMemberCountModel.MemberCountModel memberCountModel) {
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
        viewModel.setRank(isNoRank ?  "暂无排名" : "NO." + memberCountModel.getRank());
        if (isNoRank) {
            viewModel.setNameColor(COLOR_NORANK_TEXT);
            viewModel.setContributeColor(COLOR_NORANK_TEXT);
            viewModel.setRankColor(COLOR_NORANK_TEXT);
            viewModel.setContributeText("还没有收到粉丝打赏");
        } else {
            viewModel.setNameColor(COLOR_HAS_RANK_NAME_TEXT);
            viewModel.setContributeColor(COLOR_HAS_RANK_DES_TEXT);
            viewModel.setRankColor(COLOR_HAS_RANK_NAME_TEXT);
            List<GiftMemberCountModel.MemberCountModel.UserModel> userModels = memberCountModel.getUserList();
            if (userModels != null && userModels.size() > 0) {
                GiftMemberCountModel.MemberCountModel.UserModel userModel = userModels.get(0);
                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append("@");
                builder.append(userModel.getNickName());
                builder.append(" 等");
                builder.append(String.valueOf(memberCountModel.getUserCount()));
                builder.append("位粉丝贡献了");
                SpannableString spannableString = new SpannableString(String.valueOf(memberCountModel.getWhaleyCurrentCount()));
                spannableString.setSpan(colorSpan,0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(spannableString);
                builder.append("鲸币");
                viewModel.setContributeText(builder);
            }else {
                viewModel.setContributeText("还没有收到粉丝打赏");
            }
        }
        return viewModel;
    }
}
