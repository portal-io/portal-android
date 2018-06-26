package com.whaley.biz.setting.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.ui.presenter.WhaleyVrPresenter;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.utils.VersionUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/9.
 */

@Route(path = SettingRouterPath.ABOUT)
public class WhaleyVrFragment extends BaseMVPFragment<WhaleyVrPresenter> implements WhaleyVrView {

    @BindView(R2.id.vr_manager_version)
    TextView vrManagerVersion;

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText(getString(R.string.about_whaley));
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
            vrManagerVersion.setText(String.format(getString(R.string.app_version), VersionUtil.getVersionName()));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_whaley_vr;
    }

    @OnClick(R2.id.user_agreement)
    void onAgreementClick() {
        getPresenter().onAgreementClick();
    }

    @OnClick(R2.id.tv_enter_qq)
    void onEnterQQClick() {
        getPresenter().onEnterQQClick();
    }

    @OnClick(R2.id.tv_trade)
    void onTrade() {
        getPresenter().onTrade();
    }

}
