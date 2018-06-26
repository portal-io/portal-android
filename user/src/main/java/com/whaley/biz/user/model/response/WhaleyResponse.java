package com.whaley.biz.user.model.response;


import com.whaley.biz.common.response.Response;

/**
 * Created by YangZhi on 2016/8/26 0:22.
 */
public class WhaleyResponse<T> extends Response<T> {

    private String redirect;

    private T data;

    private int code;

    private boolean isProcess = false;

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public String getMsg() {
        return super.getMsg();
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getRedirect() {
        return redirect;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        setStatus(code);
    }


    @Override
    public int getStatus() {
        return getCode();
    }

    @Override
    public void setStatus(int status) {
        super.setStatus(status);
    }


    @Override
    public boolean checkStatus() {
        return getCode() == 0;
    }
}
