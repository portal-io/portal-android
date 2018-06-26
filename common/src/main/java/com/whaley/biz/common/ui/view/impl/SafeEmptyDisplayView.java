package com.whaley.biz.common.ui.view.impl;

import com.whaley.core.uiframe.view.EmptyDisplayView;

/**
 * Created by YangZhi on 2017/7/31 20:10.
 */

public class SafeEmptyDisplayView implements EmptyDisplayView{

    public static final SafeEmptyDisplayView INSTANCE = new SafeEmptyDisplayView();

    private SafeEmptyDisplayView(){}

    @Override
    public void showEmpty() {

    }

    @Override
    public void showLoading(String s) {

    }

    @Override
    public void showError(Throwable throwable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void setOnRetryListener(OnRetryListener onRetryListener) {

    }
}
