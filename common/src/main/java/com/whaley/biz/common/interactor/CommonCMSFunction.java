package com.whaley.biz.common.interactor;

import com.whaley.biz.common.exception.ResponseErrorException;
import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.common.response.Response;

/**
 * Created by dell on 2017/8/3.
 */

public class CommonCMSFunction <R extends CMSResponse, M> extends CommonFunction<R, M>  {

    @Override
    public M apply(final R response) throws Exception {
        if (response == null) {
            throw new ResponseErrorException();
        }
        if (!response.checkStatus()) {
            throw new StatusErrorThrowable(response.getStatus(), response.getMsg(), getData(response), response.getSubCode());
        }
        return getData(response);
    }

}
