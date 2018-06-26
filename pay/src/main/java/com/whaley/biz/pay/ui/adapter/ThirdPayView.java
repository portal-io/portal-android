package com.whaley.biz.pay.ui.adapter;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Created by dell on 2017/8/21.
 */

public interface ThirdPayView extends BasePageView {
    void onPayfinish();
    void updataView();
    void updateContentViewVisible(boolean visible);
}
