package com.whaley.biz.program.ui.live;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.live.presenter.FootballRecommendPresenter;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.fragment.RefreshUIViewListFragment;

/**
 * Created by dell on 2017/8/10.
 */

public class FootballRecommendFragment extends RefreshUIViewListFragment<FootballRecommendPresenter> {

    public static Fragment newInstance(String code) {
        FootballRecommendFragment fragment = new FootballRecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProgramConstants.KEY_PARAM_ID, code);
        bundle.putString(ProgramConstants.KEY_PARAM_GET_DATA_USECASE_PATH, ProgramRouterPath.USECASE_RECOMMEND_UID);
        bundle.putString(ProgramConstants.KEY_PARAM_MAPPER_PATH, ProgramRouterPath.MAPPER_FOOTBALL_RECOMMEND);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewClick(ClickableUIViewHolder uiViewHolder) {
      getPresenter().onViewClick(uiViewHolder.getData());
    }
}

