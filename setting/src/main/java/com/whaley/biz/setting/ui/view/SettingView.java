package com.whaley.biz.setting.ui.view;

import android.app.Activity;

import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.widget.titlebar.ITitleBar;

/**
 * Author: qxw
 * Date:2017/7/26
 * Introduction:
 */

public interface SettingView extends BasePageView {
    void updateList();

    void updateList(int pos);
}
