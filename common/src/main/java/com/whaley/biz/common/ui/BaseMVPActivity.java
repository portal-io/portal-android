package com.whaley.biz.common.ui;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.core.uiframe.MVPActivity;
import com.whaley.core.widget.titlebar.ITitleBar;

/**
 * Created by YangZhi on 2017/7/25 17:22.
 */

public abstract class BaseMVPActivity<PRESENTER extends BasePagePresenter> extends MVPActivity<PRESENTER> implements BasePageView {

    @Override
    public ITitleBar getTitleBar() {
        return null;
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public SystemBarTintManager getSystemBarManager() {
        return null;
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        try {
            Configuration config = new Configuration();
            config.setToDefaults();
            res.updateConfiguration(config, res.getDisplayMetrics());
        } catch (Exception e) {
        }
        return res;
    }
}
