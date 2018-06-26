package com.whaley.biz.setting.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.ui.RecyclerLoaderFragment;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.common.widget.emptylayout.EmptyDisplayLayout;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.ui.adapter.GiftAdapter;
import com.whaley.biz.setting.ui.presenter.GiftPresenter;
import com.whaley.biz.setting.ui.viewmodel.GiftViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.viewholder.ListAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/1.
 */

@Route(path = SettingRouterPath.GIFT, extras = RouterConstants.EXTRA_LOGIN)
public class GiftFragment extends RecyclerLoaderFragment<GiftPresenter, GiftViewModel> implements GiftView {

    GiftAdapter adapter;
    ImageRequest.RequestManager requestManager;
    @BindView(R2.id.tv_address)
    EditText tvAddress;
    @BindView(R2.id.btn_address)
    TextView btnAddress;
    @BindView(R2.id.layout_address)
    LinearLayout layoutAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_gift;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText("我的奖品");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
    }

    @Override
    protected EmptyDisplayLayout onCreateEmptyLayout(View rootView) {
        EmptyDisplayLayout emptyLayout = (EmptyDisplayLayout)rootView.findViewById(R.id.emptyGiftLayout);
        emptyLayout.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_gift, null, false));
        return emptyLayout;
    }

    @Override
    protected ListAdapter onCreateAdapter() {
        adapter = new GiftAdapter(requestManager);
        adapter.setOnRecyclerViewListener(new GiftAdapter.OnRecyclerViewListener() {
            @Override
            public void onBtnClick(int position) {
                getPresenter().onItemClick(position);
            }
        });
        return adapter;
    }

    @Override
    protected boolean isShouldLoadMore() {
        return false;
    }

    @OnClick(R2.id.btn_address)
    public void onClick() {
        getPresenter().onModifyAddress();
    }

    @Override
    public void updateAddress() {
        layoutAddress.setVisibility(View.VISIBLE);
        tvAddress.setText(getPresenter().getGiftRepository().getAddress());
        btnAddress.setText(getPresenter().getGiftRepository().getBtnText());
    }

}
