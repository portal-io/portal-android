package com.whaley.biz.setting.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.whaley.biz.setting.ui.adapter.CurrencyAdapter;
import com.whaley.biz.setting.ui.presenter.CurrencyPresenter;
import com.whaley.biz.setting.ui.viewmodel.CurrencyViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.viewholder.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2017/10/12.
 */

@Route(path = SettingRouterPath.CURRENCY, extras = RouterConstants.EXTRA_LOGIN)
public class CurrencyFragment extends RecyclerLoaderFragment<CurrencyPresenter, CurrencyViewModel> implements CurrencyView {

    @BindView(R2.id.tv_currency)
    TextView tvCurrency;

    CurrencyAdapter adapter;
    ImageRequest.RequestManager requestManager;

    @BindView(R2.id.layout_currency)
    LinearLayout layoutCurrency;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_currency;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText("鲸币");
            getTitleBar().setRightText("明细");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener() {
                @Override
                public void onRightClick(View view) {
                    super.onRightClick(view);
                    getPresenter().onCurrencyDetailsClick();
                }
            });
        }
        getRefreshLayout().setEnabled(false);
    }

    @Override
    protected EmptyDisplayLayout onCreateEmptyLayout(View rootView) {
        EmptyDisplayLayout emptyLayout = (EmptyDisplayLayout) rootView.findViewById(R.id.emptyCurrencyLayout);
        return emptyLayout;
    }

    @Override
    protected ListAdapter onCreateAdapter() {
        adapter = new CurrencyAdapter(requestManager);
        adapter.setOnBtnClickListener(new CurrencyAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClick(int position) {
                getPresenter().onBtnClick(position);
            }
        });
        return adapter;
    }

    @Override
    protected boolean isShouldLoadMore() {
        return false;
    }

    @Override
    public void updateAmount(int amount) {
        tvCurrency.setText("" + amount);
    }

    @Override
    public void showCurencyContent() {
        layoutCurrency.setVisibility(View.VISIBLE);
    }

}
