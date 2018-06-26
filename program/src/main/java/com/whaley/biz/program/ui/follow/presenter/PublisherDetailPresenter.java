package com.whaley.biz.program.ui.follow.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.ui.presenter.TabIndicatorPresenter;
import com.whaley.biz.common.ui.view.TabIndicatorView;
import com.whaley.biz.common.ui.viewmodel.TabItemViewModel;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.interactor.ChangeFollowStatus;
import com.whaley.biz.program.interactor.GetCpDetail;
import com.whaley.biz.program.interactor.mapper.PublisheretailMapper;
import com.whaley.biz.program.model.CpDetailModel;
import com.whaley.biz.program.ui.event.FollowEvent;
import com.whaley.biz.program.ui.follow.CpProgramListFragment;
import com.whaley.biz.program.ui.follow.PublisherView;
import com.whaley.biz.program.ui.follow.repository.PublisherDetailRepository;
import com.whaley.biz.program.ui.uimodel.PublisherDetailViewModel;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/16
 * Introduction:
 */

public class PublisherDetailPresenter extends TabIndicatorPresenter<PublisherView> implements ProgramConstants {
    public static final String EXTRA_CP_CODE = "EXTRA_CP_CODE";
    public static final String TAB_TITLE_MOST_RECENT = "最新发布";
    public static final String TAB_TITLE_MOST_POPULAR = "最多播放";
    @Repository
    PublisherDetailRepository publisherDetailRepository;

    @UseCase
    GetCpDetail getCpDetail;

    @UseCase
    ChangeFollowStatus changeFollowStatus;

    @UseCase
    PublisheretailMapper publisheretailMapper;

    private Disposable detailDisposable, followDisposable;

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        if (arguments != null) {
            publisherDetailRepository.setmCpCode(arguments.getString(EXTRA_CP_CODE));
        }
        super.onViewCreated(arguments, saveInstanceState);
        regist();
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        unRegist();
    }

    @Subscribe
    public void followEvent(FollowEvent followEvent) {
        if (followEvent.getCode().equals(publisherDetailRepository.getmCpCode()) && FollowEvent.PUBLISHER != followEvent.getPage()) {
            getViewModel().setFollowed(followEvent.isFollowed());
            getUIView().updateFollowStatus(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (ProgramConstants.EVENT_LOGIN_SUCCESS.equals(event.getEventType())) {
            getData(false);
        }
    }

    public PublisherDetailViewModel getViewModel() {
        return publisherDetailRepository.getModel();
    }

    public PublisherDetailPresenter(PublisherView view) {
        super(view);
    }

    @Override
    protected void getTitles() {
        getData(true);
    }

    private void getData(final boolean isGetTitles) {
        if (detailDisposable != null) {
            detailDisposable.dispose();
        }
        detailDisposable = getCpDetail.buildUseCaseObservable(publisherDetailRepository.getmCpCode())
                .map(publisheretailMapper.<CpDetailModel>buildFunction())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<PublisherDetailViewModel>(getUIView()) {
                    @Override
                    public void onNext(@NonNull PublisherDetailViewModel publisherDetailViewModel) {
                        super.onNext(publisherDetailViewModel);
                        if (isGetTitles) {
                            publisherDetailRepository.setModel(publisherDetailViewModel);
                            getTabIndicatorRepository().setTitles(publisherDetailViewModel.getTitles());
                            getTabIndicatorRepository().setCount(getTabIndicatorRepository().getTitles().size());
                            getUIView().updateTabs();
                        } else {
                            publisherDetailRepository.getModel().setFollowed(publisherDetailViewModel.isFollowed());
                            getUIView().updateFollowStatus(false);
                        }
                    }
                });
    }


    @Override
    public Fragment getItem(int position) {
        TabItemViewModel tabItemViewData = publisherDetailRepository.getModel().getTitles().get(position);
        String title = tabItemViewData.getTitle();
        Fragment fragment = new CpProgramListFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ProgramConstants.KEY_PARAM_GET_DATA_USECASE_PATH, ProgramRouterPath.USECASE_CP_PROGRAM_LIST);
        arguments.putString(ProgramConstants.KEY_PARAM_MAPPER_PATH, ProgramRouterPath.MAPPER_CP_PROGRAM_LIST);
        arguments.putString(ProgramConstants.KEY_PARAM_ID, publisherDetailRepository.getmCpCode());
        if (TAB_TITLE_MOST_RECENT.equals(title)) {
            arguments.putString(ProgramConstants.KEY_PARAM_TYPE, CpProgramListPresenter.SORT_ORDER_TIME);
        } else if (TAB_TITLE_MOST_POPULAR.equals(title)) {
            arguments.putString(ProgramConstants.KEY_PARAM_TYPE, CpProgramListPresenter.SORT_ORDER_COUNT);
        }
        fragment.setArguments(arguments);
        return fragment;
    }

    public void onFollowClick() {
        if(getViewModel()==null)
            return;
        ChangeFollowStatus.Param param = new ChangeFollowStatus.Param();
        param.setCode(publisherDetailRepository.getmCpCode());
        param.setStatus(getViewModel().isFollowed() ? "0" : "1");
        if (followDisposable != null) {
            followDisposable.dispose();
        }
        followDisposable = changeFollowStatus.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<String>(getUIView(), false) {
                    @Override
                    public void onNext(@NonNull String s) {
                        super.onNext(s);
                        getViewModel().setFollowed(!getViewModel().isFollowed());
                        getUIView().updateFollowStatus(true);
                        EventController.postEvent(new FollowEvent(publisherDetailRepository.getmCpCode(), getViewModel().isFollowed(), FollowEvent.PUBLISHER));
                    }

                    @Override
                    public void onNotLoggedInError() {
                        DialogUtil.showDialog(getUIView().getAttachActivity(),
                                getUIView().getAttachActivity().getString(R.string.dialog_follow), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        TitleBarActivity.goPage(getStater(), 0, "/user/ui/login");
                                    }
                                });
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (detailDisposable != null) {
            detailDisposable.dispose();
        }
        if (followDisposable != null) {
            followDisposable.dispose();
        }
    }

}
