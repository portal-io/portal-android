package com.whaley.biz.common.ui.view;

import com.whaley.core.uiframe.view.PageView;

/**
 * Created by YangZhi on 2017/7/25 17:19.
 */

public interface BasePageView extends PageView, TitleBarView ,SystemBarView{
    void finishView();
}
