package com.whaley.biz.setting.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.ui.RecyclerLoaderFragment;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.model.RedemptionCodeModel;
import com.whaley.biz.setting.ui.adapter.RedemptionCodeAdapter;
import com.whaley.biz.setting.ui.presenter.RedemptionCodePresenter;
import com.whaley.biz.setting.ui.viewmodel.RedemptionCodeViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.ListAdapter;
import com.whaley.core.widget.viewholder.OnItemClickListener;

/**
 * Created by dell on 2017/8/3.
 */

@Route(path = SettingRouterPath.REDEMPTION, extras = RouterConstants.EXTRA_LOGIN)
public class RedemptionCodeFragment extends RecyclerLoaderFragment<RedemptionCodePresenter, RedemptionCodeViewModel> implements RedemptionCodeView {

    RedemptionCodeAdapter adapter;
    ImageRequest.RequestManager requestManager;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_redemption;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText("我的券/兑换码");
            getTitleBar().setRightText("明细");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener(){
                @Override
                public void onRightClick(View view) {
                    getPresenter().onPayDetailsClick();
                }
            });
        }
    }

    @Override
    protected ListAdapter onCreateAdapter() {
        adapter = new RedemptionCodeAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder iViewHolder, int position) {
                getPresenter().onItemDetailClick(iViewHolder, position);
            }
        });
        adapter.setConvertOnClickListener(new RedemptionCodeAdapter.ConvertOnClickListener() {
            @Override
            public void onConvertClick(String code) {
                getPresenter().onConvertClick(code);
            }
        });
        return adapter;
    }

    @Override
    protected boolean isShouldLoadMore() {
        return true;
    }

    @Override
    public void onSuccessView(RedemptionCodeModel redemptionCodeModel) {
        if(!redemptionCodeModel.isUnity()) {
            getPresenter().onRefresh();
        }
        RedemptionSuccessFragment.showDialog(redemptionCodeModel, getFragmentManager());
    }

}
