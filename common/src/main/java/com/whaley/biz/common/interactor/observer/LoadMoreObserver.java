package com.whaley.biz.common.interactor.observer;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.common.ui.viewmodel.LoadListData;
import com.whaley.core.widget.refresh.ILoadMoreView;
import com.whaley.core.widget.refresh.IRefreshView;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by YangZhi on 2017/7/31 12:32.
 */

public class LoadMoreObserver<T extends LoaderUseCase.LoaderData> extends DisposableObserver<T>{

    private final LoaderView loaderView;

    private final IRefreshView irefreshView;

    private final ILoadMoreView iloadMoreView;

    public LoadMoreObserver(LoaderView loaderView){
        this.loaderView = loaderView;
        this.iloadMoreView = loaderView.getILoadMoreView();
        this.irefreshView = loaderView.getIRefreshView();
    }

    @Override
    public void onNext(@NonNull T t) {
        loaderView.updateOnLoadMore(t.getLoadListData().getViewDatas());
        loaderView.getILoadMoreView().setHasMore(t.getLoadListData().isHasMore(),false);
        loaderView.getILoadMoreView().stopLoadMore(true);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        loaderView.getILoadMoreView().stopLoadMore(false);
    }

    @Override
    public void onComplete() {

    }
}
