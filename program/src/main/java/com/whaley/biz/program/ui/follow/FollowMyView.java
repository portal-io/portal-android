package com.whaley.biz.program.ui.follow;

import com.whaley.biz.program.uiview.presenter.LoadUIViewPresenter;

/**
 * Author: qxw
 * Date:2017/8/9
 * Introduction:
 */

public interface FollowMyView extends LoadUIViewPresenter.RecyclerUIVIEW {
    void showEmptyLogin();

    void updataNoData();
}
