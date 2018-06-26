package com.whaley.biz.program.ui.live;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.TabIndicatorFragment;
import com.whaley.biz.common.ui.presenter.TabIndicatorPresenter;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.ui.live.presenter.LiveMainPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/10.
 */

@Route(path = "/program/ui/liveTab")
public class LiveMainFragment extends TabIndicatorFragment implements LiveMainView{

    @BindView(R2.id.vs_tips)
    ViewStub vsTips;

    private LiveMainPresenter liveMainPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_live;
    }

    @Override
    protected TabIndicatorPresenter onCreatePresenter() {
        liveMainPresenter = new LiveMainPresenter(this);
        return liveMainPresenter;
    }

    @OnClick(R2.id.btn_reserve)
    public void onClick() {
        liveMainPresenter.onReserveClick();
    }

    @Override
    public void showTips() {
        if (vsTips != null) {
            vsTips.inflate();
        }
        final View view_tips = getRootView().findViewById(R.id.view_tips);
        if (view_tips == null)
            return;
        view_tips.setBackgroundResource(R.mipmap.bg_guide_live_tab);
        view_tips.setVisibility(View.VISIBLE);
        view_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesUtil.setShowTips(true);
                view_tips.setBackgroundResource(0);
                view_tips.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void hideStatusBar() {
        if (getActivity() != null && isFragmentVisible()) {
            StatusBarUtil.setWhiteFullStatusBar(getActivity().getWindow(), getSystemBarManager());
        }
    }

}
