package com.whaley.biz.common.interactor;

import com.whaley.biz.common.exception.ResponseErrorException;
import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.response.BaseListResponse;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/8/10.
 */

public class ListTabFunction<R extends BaseListResponse, M> implements Function<R, List<M>> {

    @Override
    public List<M> apply(@NonNull R response) throws Exception {
        if (response == null) {
            throw new ResponseErrorException();
        }
        if (!response.checkStatus()) {
            throw new StatusErrorThrowable(response.getStatus(), response.getMsg(), getList(response));
        }
        return getList(response);
    }

    protected List<M> getList(R response) {
        return response.getList();
    }


}
