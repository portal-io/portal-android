package com.whaley.biz.common.interactor.observer;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.core.widget.refresh.ILoadMoreView;
import com.whaley.core.widget.refresh.IRefreshView;

import io.reactivex.annotations.NonNull;

/**
 * Created by YangZhi on 2017/7/31 12:32.
 */

public class RefreshObserver<T extends LoaderUseCase.LoaderData> extends UpdateUIObserver<T> {

    private final LoaderView loaderView;

    private final IRefreshView iRefreshView;

    private final ILoadMoreView iLoadMoreView;

    public RefreshObserver(LoaderView loaderView) {
        this(loaderView, true);
    }

    public RefreshObserver(LoaderView loaderView, boolean isShowEmpty) {
        super(loaderView, true, isShowEmpty);
        this.loaderView = loaderView;
        this.iLoadMoreView = loaderView.getILoadMoreView();
        this.iRefreshView = loaderView.getIRefreshView();
    }

    @Override
    public void onNext(@NonNull T t) {
        super.onNext(t);
        loaderView.updateOnRefresh(t.getLoadListData().getViewDatas());
        iLoadMoreView.setHasMore(t.getLoadListData().isHasMore(), true);
    }

    @Override
    public void onError(@NonNull Throwable e) {;
        super.onError(e);
        iRefreshView.stopRefresh(false);
    }

    @Override
    public void onComplete() {
        super.onComplete();
        iRefreshView.stopRefresh(true);
    }

    @Override
    public void onNoDataError() {
        super.onNoDataError();
        loaderView.updateOnRefresh(null);
        iLoadMoreView.setHasMore(false, true);
    }

    @Override
    protected boolean isShowToast() {
        return false;
    }
}
