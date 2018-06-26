package com.whaley.biz.user.model.response;

import com.whaley.biz.user.R;
import com.whaley.biz.user.model.CaptchaModel;
import com.whaley.core.appcontext.AppContextProvider;

/**
 * Author: qxw
 * Date:2017/8/2
 * Introduction:
 */

public class SendSMSResponse extends WhaleyResponse<CaptchaModel> {
    @Override
    public CaptchaModel getData() {
        CaptchaModel captchaModel = super.getData();
        if (captchaModel == null) {
            captchaModel = new CaptchaModel();
        }
        return captchaModel;
    }

    @Override
    public String getMsg() {
        if (getStatus() == 101) {
            return AppContextProvider.getInstance().getContext().getString(R.string.user_empty_smscode_error);
        } else if (getStatus() == 141) {
            return AppContextProvider.getInstance().getContext().getString(R.string.user_hint_validate);
        } else {
            return AppContextProvider.getInstance().getContext().getString(R.string.user_sms_code_err);
        }
    }
}
