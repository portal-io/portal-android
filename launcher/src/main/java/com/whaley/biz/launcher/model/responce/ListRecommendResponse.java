package com.whaley.biz.launcher.model.responce;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.launcher.model.ListRecommendModel;

import java.util.List;

/**
 * Created by dell on 2017/8/10.
 */

public class ListRecommendResponse<T> extends CMSResponse<ListRecommendModel<T>> implements BaseListResponse<ListRecommendModel<T>, T> {
    @Override
    public List<T> getList() {
        return getData().getRecommendAreas();
    }

    @Override
    public boolean isLast() {
        return true;
    }
}
