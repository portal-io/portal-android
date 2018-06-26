package com.whaley.biz.common.interactor;

import com.whaley.biz.common.response.BaseResponse;
import com.whaley.biz.common.response.CMSResponse;

/**
 * Created by dell on 2017/11/3.
 */

public class ResponseCMSFunction <R extends CMSResponse, M extends CMSResponse> extends CommonCMSFunction<R, M> {

    @Override
    protected M getData(BaseResponse response) {
        return (M) response;
    }
}
