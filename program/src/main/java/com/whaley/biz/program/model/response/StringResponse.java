package com.whaley.biz.program.model.response;

import com.whaley.biz.common.response.CMSResponse;

/**
 * Author: qxw
 * Date:2017/8/26
 * Introduction:
 */

public class StringResponse extends CMSResponse<String> {
    @Override
    public String getData() {
        if (super.getData() == null) {
            return "";
        } else {
            return super.getData();
        }
    }
}
