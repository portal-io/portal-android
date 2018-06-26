package com.whaley.biz.common.response;


import com.whaley.biz.common.model.base.ListModel;

import java.util.List;


/**
 * Author: qxw
 * Date: 2016/10/12
 * 分页数据返回模型
 */

public class ListTabResponse<T> extends CMSResponse<ListModel<T>> implements BaseListResponse<ListModel<T>, T> {

    @Override
    public List<T> getList() {
        if (getData() != null)
            return getData().getContent();
        else {
            return null;
        }
    }

    @Override
    public boolean isLast() {
        if (getData() != null)
            return getData().isLast();
        else
            return true;
    }
}
