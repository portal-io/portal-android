package com.whaley.biz.program.ui.detail.viewholder;

import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewHolder {
    View rootView;
    private Unbinder unbinder;

    public ViewHolder(View view) {
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
