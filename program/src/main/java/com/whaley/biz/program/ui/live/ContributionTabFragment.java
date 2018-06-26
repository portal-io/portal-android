package com.whaley.biz.program.ui.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.TabIndicatorFragment;
import com.whaley.biz.common.ui.presenter.TabIndicatorPresenter;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.live.presenter.ContributionTabPresenter;

import butterknife.BindView;

/**
 * Created by YangZhi on 2017/10/12 15:17.
 */

@Route(path = ProgramRouterPath.CONTRIBUTION_TAB)
public class ContributionTabFragment extends TabIndicatorFragment implements ContributionTabPresenter.ContributionTabView{

    @BindView(R2.id.tv_total_des)
    TextView tvTotalDes;

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if(getTitleBar()!=null) {
            getTitleBar().setBackgroundColor(getResources().getColor(R.color.color12));
            getTitleBar().setBottomLineHeight(0);
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener() {
                @Override
                public void onRightClick(View view) {
                    finish();
                }
            });
        }
    }

    @Override
    public void updateTotalDes(CharSequence text) {
        tvTotalDes.setText(text);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_contribution_tab;
    }

    @Override
    protected TabIndicatorPresenter onCreatePresenter() {
        return new ContributionTabPresenter(this);
    }
}
