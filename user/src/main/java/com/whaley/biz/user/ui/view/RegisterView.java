package com.whaley.biz.user.ui.view;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Author: qxw
 * Date:2017/8/2
 * Introduction:
 */

public interface RegisterView extends BasePageView {

    void disableSmsButton();

    void enableSmsButton();

    void enableNextButton();

    void disableNextButton();

    void showUserNameError(String error);

    void showSmsCodeError(String error);

    void showValidateCodeError(String error);

    void clearError();

    void updateValidateCodeButton(long number);

    void resetSmsButton();

    void showValidateInput();
}
