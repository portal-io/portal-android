package com.whaley.biz.program.ui.arrange;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.arrange.presenter.TopicPresenter;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.adapter.ActivityHeadUIAdapter;
import com.whaley.biz.program.uiview.adapter.RecyclerViewUIAdapter;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;

import butterknife.BindView;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */
@Route(path = ProgramRouterPath.TOPIC)
public class TopicFragment extends BaseTopicFragment<TopicPresenter> implements ActivityHeadUIAdapter.OnMyCardClickListener {

    @BindView(R2.id.vs_movable)
    ViewStub vsMovable;
    RecyclerView recyclerView;

    @Override
    public void updata(RecyclerViewModel recyclerViewModel) {
        if (getPresenter().getRepository().isMovable()) {
            pullableLayout.setVisibility(View.GONE);
            if (recyclerView == null) {
                uiAdapter.setOnUIViewClickListener(this);
                recyclerView = (RecyclerView) vsMovable.inflate();
                recyclerHolder=new RecyclerViewUIAdapter.RecyclerHolder(recyclerView);
                uiAdapter.setRecyclerHolder(recyclerHolder);
                uiAdapter.setRequestManager(requestManager);
                if (uiAdapter != null && recyclerHolder != null) {
                    uiAdapter.onBindView(recyclerHolder, recyclerViewModel, 0);
                }
            }
        } else {
            super.updata(recyclerViewModel);
        }

    }

    @Override
    public void onMyCardClick(ClickableUIViewHolder viewHolder) {
            getPresenter().onMyCardClick();
    }
}
