package com.whaley.biz.user.model.response;

/**
 * Author: qxw
 * Date:2017/8/3
 * Introduction:
 */

public class WhaleyStringResponse extends WhaleyResponse<String> {
    @Override
    public String getData() {
        if (super.getData() == null) {
            return "";
        }
        return super.getData();
    }
}
