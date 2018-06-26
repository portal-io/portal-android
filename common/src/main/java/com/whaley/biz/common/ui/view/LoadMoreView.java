package com.whaley.biz.common.ui.view;

/**
 * Created by yangzhi on 17/1/19.
 */

public interface LoadMoreView<DATA>{

    void stopLoadMore(boolean isSuccess);

    void setHasMore(boolean hasMore, boolean isRefresh);

    void updateOnLoadMore(DATA data);
}
