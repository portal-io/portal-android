package com.whaley.biz.common.response;

import java.util.List;

/**
 * Author: qxw
 * Date: 2016/10/28
 */

public interface BaseListResponse<T, K> extends BaseResponse<T> {
    List<K> getList();

    boolean isLast();
}
