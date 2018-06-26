package com.whaley.biz.common.response;

/**
 * Created by dell on 2017/8/14.
 */

import java.util.List;

public class ListNoTabResponse<T> extends CMSResponse<List<T>> implements BaseListResponse<List<T>, T> {

    @Override
    public List<T> getList() {
        return getData();
    }

    @Override
    public boolean isLast() {
        return true;
    }
}
