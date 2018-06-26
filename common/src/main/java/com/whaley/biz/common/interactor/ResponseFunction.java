package com.whaley.biz.common.interactor;

import com.whaley.biz.common.response.BaseResponse;
import com.whaley.biz.common.response.Response;

/**
 * Created by YangZhi on 2017/7/31 10:14.
 */

public class ResponseFunction<R extends BaseResponse, M extends BaseResponse> extends CommonFunction<R, M> {

    @Override
    protected M getData(BaseResponse response) {
        return (M) response;
    }
}
