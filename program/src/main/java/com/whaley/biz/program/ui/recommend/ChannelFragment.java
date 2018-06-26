package com.whaley.biz.program.ui.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.recommend.presenter.ChannelPresenter;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.fragment.RefreshUIViewListFragment;

/**
 * Created by dell on 2017/9/14.
 */

@Route(path = ProgramRouterPath.CHANNEL_RECOMMEND)
public class ChannelFragment extends RefreshUIViewListFragment<ChannelPresenter> {

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText("全部频道");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
        getUiAdapter().setOnUIViewClickListener(this);
    }


    @Override
    public void onViewClick(ClickableUIViewHolder viewHolder) {
        getPresenter().onViewClick(viewHolder.getData());
    }

}
