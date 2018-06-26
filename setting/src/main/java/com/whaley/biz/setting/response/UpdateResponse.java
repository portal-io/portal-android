package com.whaley.biz.setting.response;

import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.setting.model.UpdateModel;

/**
 * Author: qxw
 * Date:2017/8/28
 * Introduction:
 */

public class UpdateResponse extends CMSResponse<UpdateModel> {

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public boolean checkStatus() {
        return getStatus() == 1 || getStatus() == 200;
    }

}
