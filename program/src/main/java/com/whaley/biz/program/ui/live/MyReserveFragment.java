package com.whaley.biz.program.ui.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.ui.RecyclerLoaderFragment;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.live.adapter.ReserveAdapter;
import com.whaley.biz.program.ui.live.presenter.MyReservePresenter;
import com.whaley.biz.program.ui.uimodel.ReserveViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.ListAdapter;
import com.whaley.core.widget.viewholder.OnItemClickListener;

/**
 * Created by dell on 2017/8/21.
 */

@Route(path = ProgramRouterPath.MY_RESERVE, extras = RouterConstants.EXTRA_LOGIN)
public class MyReserveFragment extends RecyclerLoaderFragment<MyReservePresenter, ReserveViewModel> implements MyReserveView {

    ReserveAdapter adapter;
    ImageRequest.RequestManager requestManager;

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        if(getTitleBar()!=null){
            getTitleBar().setTitleText("我的预约");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_program_empty, null, false);
        ((TextView)emptyView.findViewById(R.id.tv_empty)).setText("当前无任何预约节目");
        emptyLayout.setEmptyView(emptyView);
    }

    @Override
    protected boolean isShouldLoadMore() {
        return false;
    }

    @Override
    protected ListAdapter onCreateAdapter() {
        adapter = new ReserveAdapter(requestManager);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder iViewHolder, int position) {
                getPresenter().onClick(position);
            }
        });
        return adapter;
    }

    @Override
    public void updatePayStatus() {
        adapter.notifyDataSetChanged();
    }

}

