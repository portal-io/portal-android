package com.whaley.biz.common.ui.viewmodel;

import com.whaley.core.debug.logger.Log;

import java.util.List;

/**
 * Created by YangZhi on 2017/7/25 19:52.
 */

public class LoadListData<T,H>{

    private List<T> listDatas;

    private List<H> viewDatas;

    private int page;

    private boolean isHasMore = false;

    public List<T> getListData() {
        return listDatas;
    }

    public void setListData(List<T> listData) {
        this.listDatas = listData;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isHasMore() {
        return isHasMore;
    }

    public void setHasMore(boolean hasMore) {
        isHasMore = hasMore;
    }

    public List<H> getViewDatas() {
        return viewDatas;
    }

    public void setViewDatas(List<H> viewDatas) {
        this.viewDatas = viewDatas;
    }
}
