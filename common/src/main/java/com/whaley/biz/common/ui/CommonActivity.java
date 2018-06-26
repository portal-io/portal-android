package com.whaley.biz.common.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.biz.common.ui.view.SystemBarView;
import com.whaley.biz.common.utils.LeakCanaryUtil;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.uiframe.BaseFragment;
import com.whaley.core.uiframe.ContainerActivity;
import com.whaley.core.utils.StrUtil;
import com.whaley.core.widget.titlebar.ITitleBar;


/**
 * Author: qxw
 * Date:2017/8/4
 * Introduction:
 */

public class CommonActivity extends ContainerActivity implements BasePageView {

    ProgressDialog progressDialog;
    SystemBarTintManager systemBarManager;

    public static Intent createIntent(Starter starter, String fragmentClazzName, String fragmentClassPath) {
        Intent intent = new Intent(starter.getAttatchContext(), CommonActivity.class);
        intent.putExtra(STR_FRAGMENT_CLASS_NAME, fragmentClazzName);
        intent.putExtra(STR_FRAGMENT_PATH, fragmentClassPath);
        return intent;
    }

    @Override
    public void showLoading(String text) {
        if (StrUtil.isEmpty(text)) {
            text = "请稍候...";
        }
        removeLoading();
        progressDialog = ProgressDialog.show(this, null, text, true, true);
    }

    @Override
    public void removeLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        removeLoading();
        super.onDestroy();
    }

    public SystemBarTintManager getSystemBarManager() {
        if (systemBarManager == null)
            systemBarManager = new SystemBarTintManager(this);
        return systemBarManager;
    }

    @Override
    public ITitleBar getTitleBar() {
        return null;
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public void finish() {
        if (baseFragment != null) {
            BaseFragment fragment = baseFragment;
            baseFragment = null;
            fragment.finish();
            return;
        }
        super.finish();
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
