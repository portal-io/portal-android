package com.whaley.biz.program.ui.follow.presenter;


import android.os.Bundle;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.interactor.observer.RefreshObserver;
import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.GetFollowMyList;
import com.whaley.biz.program.interactor.mapper.FollowMyMapper;
import com.whaley.biz.program.interactor.mapper.UIViewModelMapper;
import com.whaley.biz.program.ui.event.FollowEvent;
import com.whaley.biz.program.ui.follow.FollowMyView;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.uiview.presenter.LoadUIViewPresenter;
import com.whaley.biz.program.uiview.viewmodel.ButtonViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ExpandMoreViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.inject.annotation.UseCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Author: qxw
 * Date:2017/8/9
 * Introduction:
 */

public class FollowMyPresenter extends LoadUIViewPresenter<FollowMyView> {


    @UseCase
    GetFollowMyList getFollowMyList;

    @UseCase
    FollowMyMapper followMyMapper;

    boolean isFragmentVisible;


//    public void isFragmentVisible(boolean isFragmentVisible) {
//        if (isFragmentVisible == this.isFragmentVisible) {
//            return;
//        }
//        this.isFragmentVisible = isFragmentVisible;
//        if (this.isFragmentVisible && isVisible()) {
//            initData();
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FollowEvent event) {
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (ProgramConstants.EVENT_SIGN_OUT.equals(event.getEventType())) {
            setDataView(true);
            return;
        }
        if (ProgramConstants.EVENT_LOGIN_SUCCESS.equals(event.getEventType())) {
            onRefresh();
            return;
        }
        if (ProgramConstants.EVENT_FOLLOW_MY_CLICK.equals(event.getEventType())) {
            onRefresh();
            return;
        }
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        regist();
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        unRegist();
    }


    public FollowMyPresenter(FollowMyView view) {
        super(view);
    }

    @Override
    public Observable<? extends BaseListResponse> onLoadMoreObservable() {
        return getFollowMyList.buildUseCaseObservable(getLoaderRepository().getLoadListData().getPage() + 1);
    }

    @Override
    public Observable<? extends BaseListResponse> onRefreshObservable() {
        return getFollowMyList.buildUseCaseObservable(0);
    }

    @Override
    public RefreshObserver onRefreshObserver() {
        return new RefreshObserver(getUIView(), isShowEmpty()) {
            @Override
            public void onNext(@NonNull LoaderUseCase.LoaderData loaderData) {
                getRecyclerViewModel().setChanged(true);
                onSuccess(loaderData.getLoadListData().isHasMore(), false);
            }

            @Override
            public void onNoDataError() {
                getUIView().getILoadMoreView().setHasMore(false, true);
                setDataView(false);
            }

            @Override
            public void onNotLoggedInError() {
                setDataView(true);
            }
        };
    }

    private void setDataView(boolean isNoLogin) {
        if (getRecyclerViewModel() != null) {
            getRecyclerViewModel().setClickableUiViewModels(null);
            getRecyclerViewModel().setChanged(true);
            getUIView().bindViewModel(getRecyclerViewModel());
        }
        if (isNoLogin) {
            getUIView().showEmptyLogin();
        } else {
            getUIView().updataNoData();
        }
    }

    @Override
    public Object getMapperData(LoaderUseCase.LoaderData loaderData) {
        return loaderData.getResponse().getData();
    }

    @Override
    public UIViewModelMapper getMapper() {
        return followMyMapper;
    }

    @Override
    public GetFollowMyList getUserCase() {
        return getFollowMyList;
    }

    public void onFollowClick() {
        GoPageUtil.goPage(getStater(), FormatPageModel.goPagePageModelFollowRecommend());
    }

    public void onViewClick(ClickableUIViewModel data, int position) {
        switch (data.getType()) {
            case ViewTypeConstants.TYPE_GRAPHIC_ICON_HEAD:
            case ViewTypeConstants.TYPE_CHANNEL_ICON_ROUND:
            case ViewTypeConstants.TYPE_GRAPHIC_LEFT_AND_RIGHT:
                GoPageUtil.goPage(getStater(), data.getPageModel());
                break;
            case ViewTypeConstants.TYPE_BUTTON:
                ButtonViewUIViewModel buttonViewUIViewModel = (ButtonViewUIViewModel) data;
                ExpandMoreViewModel expandMoreViewModel = buttonViewUIViewModel.getSeverModel();
                if (ButtonViewUIViewModel.TYPE_EXPAND == buttonViewUIViewModel.getButtonType()) {
                    more(buttonViewUIViewModel, expandMoreViewModel, position);
                    return;
                }
                if (ButtonViewUIViewModel.TYPE_PUT_AWAY == buttonViewUIViewModel.getButtonType()) {
                    getRecyclerViewModel()
                            .getClickableUiViewModels()
                            .removeAll(expandMoreViewModel.clickableUiViewModels);
                    buttonViewUIViewModel.setButtonType(ButtonViewUIViewModel.TYPE_EXPAND);
                    getRecyclerViewModel().setChanged(true);
                    getUIView().bindViewModel(getRecyclerViewModel());
                    return;
                }
                break;
        }
    }

    private void more(ButtonViewUIViewModel buttonViewUIViewModel, ExpandMoreViewModel expandMoreViewModel, int position) {
        getRecyclerViewModel()
                .getClickableUiViewModels()
                .addAll(position, expandMoreViewModel.clickableUiViewModels);
        buttonViewUIViewModel.setButtonType(ButtonViewUIViewModel.TYPE_PUT_AWAY);
        getRecyclerViewModel().setChanged(true);
        getUIView().bindViewModel(getRecyclerViewModel());
    }

    public void onLoginClick() {
        TitleBarActivity.goPage(getStater(), 0, "/user/ui/login");
    }

    public void refreshData() {
        onRefresh();
    }
}
