package com.whaley.biz.program.ui.arrange;

import android.support.v4.app.FragmentManager;

import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;

/**
 * Author: qxw
 * Date:2017/8/10
 * Introduction:
 */

public interface TopicView extends BasePageView {
    void updata(RecyclerViewModel recyclerViewModel);

    FragmentManager getChildFragmentManager();

    void onError();
}
