package com.whaley.biz.program.ui.follow.presenter;


import android.os.Bundle;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.ChangeFollowStatus;
import com.whaley.biz.program.interactor.GetRecommendUid;
import com.whaley.biz.program.interactor.mapper.RecommendFollowMapper;
import com.whaley.biz.program.interactor.mapper.UIViewModelMapper;
import com.whaley.biz.program.ui.event.FollowEvent;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.uiview.presenter.LoadUIViewPresenter;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.HorziontalFullImgViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.PromulagtorDescriptionUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.PromulagtorUIViewModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;


/**
 * Author: qxw
 * Date:2017/8/14
 * Introduction:
 */

public class FollowRecommendPresenter extends LoadUIViewPresenter<LoadUIViewPresenter.RecyclerUIVIEW> {


    @UseCase
    ChangeFollowStatus changeFollowStatus;

    @UseCase
    GetRecommendUid getRecommendUid;

    @UseCase
    RecommendFollowMapper mapper;

    public FollowRecommendPresenter(RecyclerUIVIEW view) {
        super(view);
    }

    @Override
    public Observable<? extends BaseListResponse> onRefreshObservable() {
        if (StrUtil.isEmpty(getRecyclerRepository().getId())) {
            getRecyclerRepository().setId("tuijian_guanzhu");
        }
        return super.onRefreshObservable();
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

    @Override
    public UIViewModelMapper getMapper() {
        return mapper;
    }

    @Override
    public GetRecommendUid getUserCase() {
        return getRecommendUid;
    }

    public void onViewClick(ClickableUIViewModel data) {
        if (data.getType() == ViewTypeConstants.TYPE_IMG_HORZIONTAL_FULL) {
            onADClick((HorziontalFullImgViewUIViewModel) data);
            return;
        }
        if (data.getType() == ViewTypeConstants.TYPE_PROMULGATOR_CONTENT) {
            GoPageUtil.goPage(getStater(), data.getPageModel());
            return;
        }
    }


    public void onFollowClick(ClickableUIViewModel data, int position) {
        if (data.getType() == ViewTypeConstants.TYPE_PROMULGATOR) {
            PromulagtorUIViewModel promulagtorUIViewModel = (PromulagtorUIViewModel) data;
            changeFollowStatus(promulagtorUIViewModel);
            ClickableUIViewModel model = promulagtorUIViewModel.getRecyclerViewModel().getClickableUiViewModels().get(0);
            if (model.getType() == ViewTypeConstants.TYPE_PROMULGATOR_DESCRIPTION) {
                PromulagtorDescriptionUIViewModel promulagtorDescriptionUIViewModel = (PromulagtorDescriptionUIViewModel) model;
                int fansCount = promulagtorDescriptionUIViewModel.getFansCount();
                promulagtorDescriptionUIViewModel.setFansCount(promulagtorUIViewModel.isFollow() ? fansCount + 1 : fansCount - 1);
                promulagtorDescriptionUIViewModel.udataFollowNum();
                getUIView().updateItem(position);
            }
        }
    }

    private void changeFollowStatus(final PromulagtorUIViewModel promulagtorUIViewModel) {
        String follow = "0";
        if (promulagtorUIViewModel.isFollow()) {
            follow = "1";
        }
        ChangeFollowStatus.Param param = new ChangeFollowStatus.Param();
        param.setCode(promulagtorUIViewModel.getClickModel().getCode());
        param.setStatus(follow);
        changeFollowStatus.execute(new UpdateUIObserver<String>(getUIView(), false) {
            @Override
            public void onNext(@NonNull String s) {
                super.onNext(s);
                EventController.postEvent(new FollowEvent(promulagtorUIViewModel.getClickModel().getCode(), promulagtorUIViewModel.isFollow(), FollowEvent.FOLLOW_LIST));
            }
        }, param);
    }


    public void onPromulagtorClick(ClickableUIViewModel data) {
        if (data.getType() == ViewTypeConstants.TYPE_PROMULGATOR) {
            GoPageUtil.goPage(getStater(), data.getPageModel());
            return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FollowEvent event) {
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (ProgramConstants.EVENT_LOGIN_SUCCESS.equals(event.getEventType())) {
            onRefresh();
        }
    }


    private void onADClick(HorziontalFullImgViewUIViewModel data) {

    }
}
