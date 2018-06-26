package com.whaley.biz.program.ui.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.live.presenter.LiveRecommendPresenter;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.adapter.BannerGalleryUIAdapter;
import com.whaley.biz.program.uiview.fragment.RefreshUIViewListFragment;

/**
 * Created by dell on 2017/8/10.
 */

public class LiveRecommendFragment extends RefreshUIViewListFragment<LiveRecommendPresenter> {

    public static Fragment newInstance(String code) {
        LiveRecommendFragment fragment = new LiveRecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProgramConstants.KEY_PARAM_ID, code);
        bundle.putString(ProgramConstants.KEY_PARAM_GET_DATA_USECASE_PATH, ProgramRouterPath.USECASE_RECOMMEND_UID);
        bundle.putString(ProgramConstants.KEY_PARAM_MAPPER_PATH, ProgramRouterPath.MAPPER_LIVE_RECOMMEND);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        getUiAdapter().setOnUIViewClickListener(this);
    }
}
