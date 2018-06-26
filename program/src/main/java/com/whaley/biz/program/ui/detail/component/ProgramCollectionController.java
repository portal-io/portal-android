package com.whaley.biz.program.ui.detail.component;

import android.view.View;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.DeleteCollection;
import com.whaley.biz.program.interactor.GetCollection;
import com.whaley.biz.program.interactor.SaveCollection;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.ui.detail.ProgramDetailView;
import com.whaley.biz.program.ui.detail.presenter.BaseProgramDetailPresenter;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Router;
import com.whaley.core.utils.NetworkUtils;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ProgramCollectionController extends BaseController implements BIConstants {
    ProgramDetailView programDetailView;
    Disposable delDisposable, getDisposable, saveDisposable;
    GetCollection getCollection;
    SaveCollection saveCollection;
    DeleteCollection deleteCollection;
    private boolean isCollection;
    protected SaveCollection.Param param;

    public ProgramDetailView getUIView() {
        return programDetailView;
    }

    public ProgramCollectionController(ProgramDetailView programDetailView) {
        this.programDetailView = programDetailView;
        getCollection = new GetCollection(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        saveCollection = new SaveCollection(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        deleteCollection = new DeleteCollection(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (ProgramConstants.EVENT_LOGIN_SUCCESS.equals(event.getEventType())) {
            getCollection(param.getCode());
        }
    }

    @Subscribe
    public void onCollectionEvent(ModuleEvent event) {
        if (BaseProgramDetailPresenter.KEY_EVENT_GET_COLLECTION.equals(event.getEventName())) {
            getCollectionEvent(event);
            return;
        }
        if (BaseProgramDetailPresenter.KEY_EVENT_COLLECTION.equals(event.getEventName())) {
            collection();
            return;
        }
    }

    protected void getCollectionEvent(ModuleEvent event) {
        ProgramDetailModel programDetailModel = (ProgramDetailModel) event.getParam();
        String code = programDetailModel.getCode();
        if (ProgramConstants.VIDEO_TYPE_MORETV_TV.equals(programDetailModel.getVideoType())) {
            code = programDetailModel.getParentCode();
        }
        param = new SaveCollection.Param(
                code,
                getVideoName(programDetailModel),
                programDetailModel.getVideoType(),
                programDetailModel.getDuration(),
                getImg(programDetailModel));
        getCollection(param.getCode());
    }

    private void collection() {
        if (!NetworkUtils.isNetworkAvailable()) {
            getUIView().showToast("网络异常，请检查网络");
            return;
        }
        if (!isCollection) {
            saveCollection();
        } else {
            deleCollection();
        }
    }

    private void deleCollection() {
        dispose();
        delDisposable = deleteCollection.buildUseCaseObservable(param.getCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull String s) {
                        isCollection = false;
                        getUIView().updateCollection(isCollection);
                        getUIView().showToast("移除播单成功");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof NotLoggedInErrorException) {
                            DialogUtil.showDialog(getActivity(),
                                    getActivity().getString(R.string.dialog_collection), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            TitleBarActivity.goPage((Starter) getActivity(), 0, "/user/ui/login");
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void saveCollection() {
        dispose();
        saveDisposable = saveCollection.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull String s) {
                        prevueClickCollection();
                        isCollection = true;
                        getUIView().updateCollection(isCollection);
                        getUIView().showToast("加入播单成功");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof NotLoggedInErrorException) {
                            DialogUtil.showDialog(getActivity(),
                                    getActivity().getString(R.string.dialog_collection), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            TitleBarActivity.goPage((Starter) getActivity(), 0, "/user/ui/login");
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    protected void getCollection(String code) {
        dispose();
        getDisposable = getCollection.buildUseCaseObservable(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        isCollection = aBoolean;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        getUIView().updateCollection(isCollection);
                    }
                });
    }

    public String getImg(ProgramDetailModel bean) {
        if (ProgramConstants.VIDEO_TYPE_MORETV_2D.equals(bean.getVideoType())) {
            if (StrUtil.isEmpty(bean.getSmallPic()))
                return bean.getLunboPic();
            return bean.getSmallPic();
        }
        if (ProgramConstants.VIDEO_TYPE_MORETV_TV.equals(bean.getVideoType())) {
            return bean.getLunboPic();
        }
        if (StrUtil.isEmpty(bean.getBigPic()))
            return bean.getLunboPic();
        return bean.getBigPic();

    }

    /**
     * 获取标题 主要由于电视剧标题特殊
     *
     * @return
     */
    private String getVideoName(ProgramDetailModel programDetailModel) {
        String displayName = programDetailModel.getDisplayName();
        if (StrUtil.isEmpty(displayName)) {
            return displayName;
        }
        if (!ProgramConstants.VIDEO_TYPE_MORETV_TV.equals(programDetailModel.getVideoType())) {
            return displayName;
        }
        int index = displayName.indexOf("[");
        if (index > 0) {
            displayName = displayName.substring(0, index);
        }
        return displayName;
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        dispose();
    }

    private void dispose() {
        if (delDisposable != null) {
            delDisposable.dispose();
        }
        if (getDisposable != null) {
            getDisposable.dispose();
        }
        if (saveDisposable != null) {
            saveDisposable.dispose();
        }
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        dispose();
        programDetailView = null;
    }

    //==================bi埋点=====================//

    protected void prevueClickCollection() {
        ProgramDetailModel programDetailModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.PROGRAM_INFO);
        if (programDetailModel == null)
            return;
        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                .setEventId(PREVUE_CLICK_COLLECTION)
                .setCurrentPageId(ROOT_VIDEO_DETAILS)
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, programDetailModel.getCode())
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, programDetailModel.getDisplayName())
                .setNextPageId(ROOT_VIDEO_DETAILS);
        if (builder != null) {
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

}