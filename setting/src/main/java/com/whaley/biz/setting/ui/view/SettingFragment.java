package com.whaley.biz.setting.ui.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.biz.setting.ui.adapter.SettingAdapter;
import com.whaley.biz.setting.ui.presenter.SettingPresenter;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.widget.adapter.ViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: qxw
 * Date:2017/7/26
 * Introduction:
 */

@Route(path = SettingRouterPath.SETTING)
public class SettingFragment extends BaseMVPFragment<SettingPresenter> implements SettingView {

    @BindView(R2.id.view_list)
    RecyclerView viewList;

    private SettingAdapter adapter;

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
            getTitleBar().setTitleText("设置");
        }
        adapter = new SettingAdapter();
        viewList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        viewList.setAdapter(adapter);
        adapter.setOnSettingItemClickListener(new SettingAdapter.OnSettingItemClickListener() {
            @Override
            public void onItemClick(ViewHolder viewHolder, int position) {
                getPresenter().onItemClick(position);
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, int position) {
                getPresenter().onCheckedChanged(isChecked, position);
            }

            @Override
            public void onSegmentCheckedChanged(int index, int position) {
                getPresenter().onSegmentCheckedChanged(index, position);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void updateList() {
        adapter.setData(getPresenter().getSettingRepository().getSettingViewModels());
    }

    @Override
    public void updateList(int pos) {
        adapter.notifyItemChanged(pos);
    }

}
