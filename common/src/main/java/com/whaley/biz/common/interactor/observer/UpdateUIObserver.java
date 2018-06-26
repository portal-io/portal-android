package com.whaley.biz.common.interactor.observer;

import com.whaley.biz.common.exception.NoNetworkErrorException;
import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.biz.common.ui.view.impl.SafeEmptyDisplayView;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.utils.StrUtil;

import io.reactivex.annotations.NonNull;

/**
 * Created by YangZhi on 2017/7/31 20:06.
 */

public class UpdateUIObserver<T> extends ErrorHandleObserver<T> {

    private final BasePageView pageView;

    private final EmptyDisplayView emptyDisplayView;

    private final boolean isShowLoading;

    private final boolean isShowEmpty;

    public UpdateUIObserver(BasePageView pageView) {
        this(pageView, false);
    }

    public UpdateUIObserver(BasePageView pageView, boolean isShowLoading) {
        this(pageView, isShowLoading, true);
    }

    public UpdateUIObserver(BasePageView pageView, boolean isShowLoading, boolean isShowEmpty) {
        this.pageView = pageView;
        if (pageView != null && pageView.getEmptyDisplayView() != null) {
            this.emptyDisplayView = pageView.getEmptyDisplayView();
        } else {
            this.emptyDisplayView = SafeEmptyDisplayView.INSTANCE;
        }
        this.isShowLoading = isShowLoading;
        this.isShowEmpty = isShowEmpty;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isShowLoading && isShowEmpty) {
            showLoading();
        }
    }

    @Override
    public void onFinalError(Throwable e) {
        removeLoading();
        if (isShowToast() && getPageView() != null && e != null && !StrUtil.isEmpty(e.getMessage())) {
            getPageView().showToast(e.getMessage());
        }
        if(e instanceof NoNetworkErrorException) {
            if(isShowEmpty)
                getEmptyDisplayView().showError(e);
            else if(!isShowToast() && getPageView() != null && e != null && !StrUtil.isEmpty(e.getMessage()))
                getPageView().showToast(e.getMessage());
        }else{
            getEmptyDisplayView().showError(e);
        }
    }

    @Override
    public void onStatusError(int status, String message) {
        removeLoading();
        if (isShowToast()) {
            getPageView().showToast(message);
        }
        getEmptyDisplayView().showError(new StatusErrorThrowable(status, message));
    }

    @Override
    public void onNoDataError() {
        getEmptyDisplayView().showEmpty();
    }

    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onComplete() {
        removeLoading();
        getEmptyDisplayView().showContent();
    }

    protected void showLoading() {
        if (getPageView() != null && getPageView().getEmptyDisplayView() != null) {
            getEmptyDisplayView().showLoading(null);
        } else if (getPageView() != null) {
            getPageView().showLoading(null);
        }
    }

    protected void removeLoading() {
        if (getPageView() != null) {
            getPageView().removeLoading();
        }
    }

    public BasePageView getPageView() {
        return pageView;
    }

    public EmptyDisplayView getEmptyDisplayView() {
        return emptyDisplayView;
    }

    protected boolean checkEmpty() {
        return true;
    }

    protected boolean isShowToast() {
        return true;
    }

}
