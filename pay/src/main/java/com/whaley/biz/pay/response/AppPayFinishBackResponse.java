package com.whaley.biz.pay.response;


import com.whaley.biz.common.response.Response;

/**
 * Created by mafei on 2017/4/5.
 */

public class AppPayFinishBackResponse extends Response {

    /**
     * code : 200
     * subCode : 000
     * msg : 成功
     */

    private String code;
    private String subCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

}
