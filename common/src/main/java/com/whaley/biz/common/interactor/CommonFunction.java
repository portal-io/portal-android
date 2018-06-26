package com.whaley.biz.common.interactor;

import com.whaley.biz.common.exception.ResponseErrorException;
import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.response.BaseResponse;
import com.whaley.biz.common.response.Response;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Author: qxw
 * Date: 2017/7/14
 */

public class CommonFunction<R extends BaseResponse, M> implements Function<R, M> {

    @Override
    public M apply(final R response) throws Exception {
        if (response == null) {
            throw new ResponseErrorException();
        }
        if (!response.checkStatus()) {
            throw new StatusErrorThrowable(response.getStatus(), response.getMsg(), getData(response));
        }
        return getData(response);
    }


    protected M getData(BaseResponse response) {
        return (M) response.getData();
    }


}
