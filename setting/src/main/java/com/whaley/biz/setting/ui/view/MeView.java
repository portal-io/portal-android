package com.whaley.biz.setting.ui.view;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Created by dell on 2017/7/27.
 */

public interface MeView extends BasePageView {

    void updateLoginStatus();
    void updateList();
    void updateCardList();
    void hideStatusBar();

}
