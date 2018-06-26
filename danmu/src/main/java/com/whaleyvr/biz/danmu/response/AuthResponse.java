package com.whaleyvr.biz.danmu.response;

import com.whaley.biz.common.response.BaseResponse;
import com.whaley.biz.common.response.Response;
import com.whaleyvr.biz.danmu.model.AuthModel;

/**
 * Created by dell on 2017/5/8.
 */

public class AuthResponse implements BaseResponse {

    private int status;
    private String msg;
    private AuthModel memberdata;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public boolean checkStatus() {
        return status == 1 || status == 200;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public void setCache(boolean cache) {

    }

    @Override
    public boolean isCache() {
        return false;
    }

    @Override
    public AuthModel getData() {
        return getMemberdata();
    }

    public AuthModel getMemberdata() {
        return memberdata;
    }

    public void setMemberdata(AuthModel memberdata) {
        this.memberdata = memberdata;
    }
}
