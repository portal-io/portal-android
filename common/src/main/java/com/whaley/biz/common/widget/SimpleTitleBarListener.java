package com.whaley.biz.common.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import com.whaley.core.widget.titlebar.TitleBarListener;

/**
 * Author: qxw
 * Date:2017/8/1
 * Introduction:
 */

public class SimpleTitleBarListener implements TitleBarListener {

    @Override
    public void onLeftClick(View view) {
        Context context = view.getContext();
        if (context instanceof Activity) {
            ((Activity) context).onBackPressed();
        } else if (context instanceof ContextWrapper) {
            Context context2 = ((ContextWrapper) context).getBaseContext();
            if (context2 instanceof Activity) {
                ((Activity) context2).onBackPressed();
            }
        }
    }

    @Override
    public void onTitleClick(View view) {

    }

    @Override
    public void onRightClick(View view) {

    }
}
