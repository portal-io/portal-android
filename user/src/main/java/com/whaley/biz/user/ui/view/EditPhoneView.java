package com.whaley.biz.user.ui.view;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Author: qxw
 * Date:2017/8/4
 * Introduction:
 */

public interface EditPhoneView extends BasePageView {
    void showPhone(String phone);

    void setSmsButtonEnable(boolean b);

    void updateValidateCodeButton(long time);

    void resetSmsButton();

    void showValidateInput();

    void showReLoginDialog();
}
