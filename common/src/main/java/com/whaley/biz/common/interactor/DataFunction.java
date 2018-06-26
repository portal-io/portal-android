package com.whaley.biz.common.interactor;

import com.whaley.biz.common.response.Response;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Author: qxw
 * Date:2017/8/2
 * Introduction:
 */

public class DataFunction<R extends Response, M> implements Function<R, M> {
    @Override
    public M apply(@NonNull R response) throws Exception {
        return getData(response);
    }

    protected M getData(Response response) {
        return (M) response.getData();
    }
}
