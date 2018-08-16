package com.whaley.biz.launcher.view;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Author: qxw
 * Date:2017/8/9
 * Introduction:
 */

public interface MainView extends BasePageView {
    void showNotice(String numNotice);

    void hideNotice();

    void showEventPoster(String path);

    void showRecommendedContent(String path, boolean isStart);

    void showSpringFestival();

    void hideSpringFestival();

    void clickPortal(boolean isFromLogin);
}
