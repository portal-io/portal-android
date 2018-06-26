package com.whaley.biz.program.ui.detail.component;

import android.view.View;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.ChangeFollowStatus;
import com.whaley.biz.program.interactor.GetCpDetail;
import com.whaley.biz.program.model.CpDetailModel;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.ui.detail.ProgramDetailView;
import com.whaley.biz.program.ui.detail.presenter.BaseProgramDetailPresenter;
import com.whaley.biz.program.ui.event.FollowEvent;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.repository.RepositoryManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProgramFollowController extends BaseController {

    ProgramDetailView programDetailView;
    Disposable disposable, disposableDetail;
    ChangeFollowStatus changeFollowStatus;
    GetCpDetail getCpDetail;
    protected String code;
    protected boolean isFollow;

    public ProgramDetailView getUIView() {
        return programDetailView;
    }

    public ProgramFollowController(ProgramDetailView programDetailView) {
        this.programDetailView = programDetailView;
    }

    @Subscribe(priority = PlayerConstants.PREPARE_START_PLAY_PRIORITY_PROGRAM)
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent) {
        if (prepareStartPlayEvent.getMaxPriority() < PlayerConstants.PREPARE_START_PLAY_PRIORITY_PROGRAM)
            return;
        PlayData playData = prepareStartPlayEvent.getPlayData();
        initFollow(playData);
    }

    protected void initFollow( PlayData playData){
        ProgramDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.PROGRAM_INFO);
        code = programDetailModel.getCpCode();
        isFollow = (programDetailModel.getIsFollow() == 1);
    }

    @Override
    public void registEvents() {
        super.registEvents();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void unRegistEvents() {
        super.unRegistEvents();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void followEvent(FollowEvent followEvent) {
        if (followEvent.getCode().equals(code) && FollowEvent.DETAIL != followEvent.getPage()) {
            isFollow = followEvent.isFollowed();
            getUIView().updateFollow(true, isFollow);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (ProgramConstants.EVENT_LOGIN_SUCCESS.equals(event.getEventType())) {
            getPublisherDetail();
        }
    }

    private void getPublisherDetail() {
        disposeDetail();
        if (getCpDetail == null) {
            getCpDetail = new GetCpDetail(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        }
        disposableDetail = getCpDetail.buildUseCaseObservable(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<CpDetailModel>(null) {
                    @Override
                    public void onNext(@NonNull CpDetailModel cpDetailModel) {
                        if (cpDetailModel.getFollow() == 1) {
                            isFollow = true;
                            getUIView().updateFollow(false, isFollow);
                        }
                    }

                    @Override
                    protected boolean isShowToast() {
                        return false;
                    }
                });
    }

    @Subscribe
    public void onFollowEvent(ModuleEvent event) {
        if (BaseProgramDetailPresenter.KEY_EVENT_FOLLOW.equals(event.getEventName())) {
            follow();
            return;
        }
        if (BaseProgramDetailPresenter.KEY_EVENT_POSTER.equals(event.getEventName())) {
            GoPageUtil.goPage((Starter) getUIView().getAttachActivity(), FormatPageModel.goPageModelPublisher(code));
            return;
        }
    }


    private void follow() {
        dispose();
        if (changeFollowStatus == null) {
            changeFollowStatus = new ChangeFollowStatus(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        }
        ChangeFollowStatus.Param param = new ChangeFollowStatus.Param();
        param.setCode(code);
        param.setStatus(isFollow ? "0" : "1");
        disposable = changeFollowStatus.buildUseCaseObservable(param).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<String>(getUIView(), false) {
                    @Override
                    public void onNext(@NonNull String s) {
                        super.onNext(s);
                        isFollow = !isFollow;
                        getUIView().updateFollow(true, isFollow);
                        EventController.postEvent(new FollowEvent(code, isFollow, FollowEvent.DETAIL));
                    }

                    @Override
                    public void onNotLoggedInError() {
                        DialogUtil.showDialog(getUIView().getAttachActivity(),
                                getUIView().getAttachActivity().getString(R.string.dialog_follow), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        TitleBarActivity.goPage((Starter) getUIView().getAttachActivity(), 0, "/user/ui/login");
                                    }
                                });
                    }
                });
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        dispose();
        disposeDetail();
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void disposeDetail() {
        if (disposableDetail != null) {
            disposableDetail.dispose();
        }
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        dispose();
        disposeDetail();
        programDetailView = null;
    }

}