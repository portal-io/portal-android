package com.whaley.biz.common.widget;

import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: qxw
 * Date:2017/8/23
 * Introduction:
 */

public class BaseViewHolder {
    View rootView;
    private Unbinder unbinder;

    public BaseViewHolder(View view) {
        rootView = view;
        unbinder = ButterKnife.bind(this, view);
    }


    public void destory() {
        if (unbinder != null)
            unbinder.unbind();
    }

    public View getRootView() {
        return rootView;
    }
}
