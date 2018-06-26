package com.whaley.biz.user.ui.view;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Author: qxw
 * Date:2017/8/7
 * Introduction:
 */

public interface FindPasswordView extends BasePageView {
    void enableSmsButton();

    void disableSmsButton();

    void enableNextButton();

    void disableNextButton();


    void updateValidateCodeButton(long number);

    void resetSmsButton();

    void showNext();
}
