package com.whaley.biz.common.ui.view;

import com.whaley.core.widget.refresh.ILoadMoreView;
import com.whaley.core.widget.refresh.IRefreshView;
import com.whaley.core.widget.refresh.OnLoadMoreListener;
import com.whaley.core.widget.refresh.OnRefreshListener;

/**
 * Created by YangZhi on 2017/7/25 17:44.
 */

public interface LoaderView<DATA> extends BasePageView,OnRefreshListener,OnLoadMoreListener{

    void updateOnRefresh(DATA refreshData);

    void updateOnLoadMore(DATA loadMoreData);

    IRefreshView getIRefreshView();

    ILoadMoreView getILoadMoreView();
}
