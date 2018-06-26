package com.whaley.biz.program.ui.detail.component;

import android.Manifest;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.DownloadStatus;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.constants.ServerRenderType;
import com.whaley.biz.program.constants.VideoBitType;
import com.whaley.biz.program.interactor.CheckLogin;
import com.whaley.biz.program.model.MediaModel;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.model.download.DownloadBean;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.playersupport.model.VideoParserBean;
import com.whaley.biz.program.ui.detail.ProgramDetailView;
import com.whaley.biz.program.ui.detail.presenter.BaseProgramDetailPresenter;
import com.whaley.biz.program.ui.detail.repository.ProgramDetailRepository;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.NetworkUtils;
import com.whaley.core.utils.PermissionUtil;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ProgramDownloadController extends BaseController implements BIConstants{

    Disposable disposable, disposableLogin;

    Executor downloadServiceManager;
    Executor client;

    boolean isDownloaded;

    ProgramDetailView programDetailView;

    ProgramDetailRepository programRepository;

    CheckLogin checkLogin = new CheckLogin();

    private boolean isReady;

    public ProgramDownloadController(ProgramDetailView programDetailView) {
        this.programDetailView = programDetailView;
        programRepository = new ProgramDetailRepository();
    }


    public ProgramDetailView getUIView() {
        return programDetailView;
    }

    public ProgramDetailRepository getRepository() {
        return programRepository;
    }


    @Subscribe
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent) {
        PlayData playData = prepareStartPlayEvent.getPlayData();
        getRepository().setType(playData.getType());
        ProgramDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.PROGRAM_INFO);
        getRepository().setCode(programDetailModel.getCode());
        getRepository().setProgramDetailModel(programDetailModel);
        boolean isCanDownload = playData.getBooleanCustomData(PlayerDataConstants.IS_CAN_DOWNLOAD);
        if (isCanDownload) {
            MediaModel mediaModel = programDetailModel.getDownloadDtos().get(0);
            getRepository().setDownloadPath(mediaModel.getDownloadUrl());
            getRepository().setDownloadRenderType(mediaModel.getRenderType());
            updateDownloaded();
        }
        isReady = true;
    }

    @Subscribe
    public void onDownloadEvent(ModuleEvent moduleEvent) {
        if (moduleEvent.getEventName().equals(BaseProgramDetailPresenter.KEY_EVENT_DOWNLOAD)) {
            disposeLogin();
            disposableLogin = checkLogin.buildUseCaseObservable(null)
                    .subscribeWith(new DisposableObserver<UserModel>() {
                        @Override
                        public void onNext(@NonNull UserModel userModel) {
                            if (userModel.isLoginUser()) {
                                download();
                            } else {
                                DialogUtil.showDialog(getActivity(),
                                        AppContextProvider.getInstance().getContext().getString(R.string.dialog_video_caching), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                TitleBarActivity.goPage((Starter) getActivity(), 0, "/user/ui/login");
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    protected void onInit() {
        super.onInit();
        registDownloadService();
    }

    private void download() {
        if(!isReady){
            //
        } else if (isDownloaded) {
            getUIView().showToast("已经在缓存列表，请不要重复添加");
        } else if (getRepository().getType() != PlayerType.TYPE_PANO || StrUtil.isEmpty(getRepository().getDownloadPath())) {
            getUIView().showToast("版权原因，暂不提供缓存");
        } else if (!NetworkUtils.isNetworkAvailable()) {
            getUIView().showToast("网络异常，请检查网络");
        } else if (PermissionUtil.requestPermission(getUIView().getAttachActivity(), Manifest.permission.READ_EXTERNAL_STORAGE, BaseProgramDetailPresenter.REQUEST_CODE_DOWNLOAD)) {
            getUIView().showToast("未开启相应权限");
        } else {
            boolean isOnlyWifi = SharedPreferencesUtil.getWifiOnly() && !NetworkUtils.isWiFiActive();
            if (!isOnlyWifi) {
                startDownload();
            } else {
                DialogUtil.showWifiDialog(getContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startDownload();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                    }
                });
            }
        }
    }

    private void updateDownloaded() {
        Router.getInstance().buildExecutor("/download/service/getDownloadStatusService").putObjParam(getRepository().getCode())
                .callback(new Executor.Callback<Integer>() {
                    @Override
                    public void onCall(Integer status) {
                        if (status != DownloadStatus.DOWNLOAD_STATUS_NOTDOWNLOAD) {
                            isDownloaded = true;
                            getUIView().updateDownloaded(isDownloaded);
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                }).excute();
    }


    private void startDownload() {
        disposeDownload();
        disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Boolean> e) throws Exception {
                Router.getInstance().buildExecutor("/download/service/getDownloadStatusService").putObjParam(getRepository().getCode())
                        .callback(new Executor.Callback<Integer>() {
                            @Override
                            public void onCall(Integer status) {
                                if (e.isDisposed())
                                    return;
                                if (status == DownloadStatus.DOWNLOAD_STATUS_NOTDOWNLOAD) {
                                    e.onNext(true);
                                    e.onComplete();
                                } else {
                                    e.onError(new DownloadStartedException());
                                    e.onComplete();
                                }
                            }

                            @Override
                            public void onFail(Executor.ExecutionError executionError) {
                                if (e.isDisposed())
                                    return;
                                e.onError(executionError);
                                e.onComplete();
                            }
                        }).excute();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (getUIView() != null)
                            getUIView().showLoading("");
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<List<VideoParserBean>>>() {
                    @Override
                    public ObservableSource<List<VideoParserBean>> apply(@NonNull Boolean aBoolean) throws Exception {
                        return io.reactivex.Observable.create(new ObservableOnSubscribe<List<VideoParserBean>>() {
                            @Override
                            public void subscribe(@NonNull final ObservableEmitter<List<VideoParserBean>> e) throws Exception {
                                Router.getInstance().buildExecutor("/parser/service/parser").putObjParam(getRepository().getDownloadPath())
                                        .callback(new Executor.Callback<String>() {
                                            @Override
                                            public void onCall(String s) {
                                                List<VideoParserBean> videoParserBeanList = GsonUtil.getGson().fromJson(s, new TypeToken<List<VideoParserBean>>() {
                                                }.getType());
                                                if (e.isDisposed())
                                                    return;
                                                e.onNext(videoParserBeanList);
                                                e.onComplete();
                                            }

                                            @Override
                                            public void onFail(Executor.ExecutionError executionError) {
                                                if (e.isDisposed())
                                                    return;
                                                e.onError(new DownloadParserException());
                                                e.onComplete();
                                            }
                                        }).excute();
                            }
                        });
                    }
                })
                .doOnNext(new Consumer<List<VideoParserBean>>() {
                    @Override
                    public void accept(@NonNull List<VideoParserBean> videoParserBeanList) throws Exception {
                        Map<Integer, String> downloadMap = new HashMap<>();
                        for (VideoParserBean bean : videoParserBeanList) {
                            downloadMap.put(bean.videoBitType, bean.url);
                        }
                        if (downloadMap.containsKey(VideoBitType.TDA)) {
                            getRepository().setDownloadResolvedPath(downloadMap.get(VideoBitType.TDA));
                        } else if (downloadMap.containsKey(VideoBitType.TDB)) {
                            getRepository().setDownloadResolvedPath(downloadMap.get(VideoBitType.TDB));
                        } else if (downloadMap.containsKey(VideoBitType.SDA)) {
                            getRepository().setDownloadResolvedPath(downloadMap.get(VideoBitType.SDA));
                            getRepository().setDownloadRenderType(ServerRenderType.RENDER_TYPE_360_2D_OCTAHEDRAL);
                        } else if (downloadMap.containsKey(VideoBitType.SDB)) {
                            getRepository().setDownloadResolvedPath(downloadMap.get(VideoBitType.SDB));
                            getRepository().setDownloadRenderType(ServerRenderType.RENDER_TYPE_360_2D_OCTAHEDRAL);
                        } else if (downloadMap.containsKey(VideoBitType.ST)) {
                            getRepository().setDownloadResolvedPath(downloadMap.get(VideoBitType.ST));
                        } else if (downloadMap.containsKey(VideoBitType.SD)) {
                            getRepository().setDownloadResolvedPath(downloadMap.get(VideoBitType.SD));
                            getRepository().setDownloadRenderType(ServerRenderType.RENDER_TYPE_360_2D_OCTAHEDRAL);
                        }
                        if (getUIView() != null)
                            getUIView().removeLoading();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (getUIView() != null)
                            getUIView().removeLoading();
                    }
                }).subscribeWith(new DisposableObserver() {
                    @Override
                    public void onNext(@NonNull Object o) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof DownloadStartedException) {
                            if (getUIView() != null)
                                getUIView().showToast("已经在缓存列表，请不要重复添加");
                        } else if (e instanceof DownloadParserException) {
                            if (getUIView() != null)
                                getUIView().showToast("缓存地址解析失败");
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (downloadServiceManager != null) {
                            DownloadBean downloadBean = DownloadBean.create(getRepository().getCode(), getRepository().getProgramDetailModel(),
                                    getRepository().getDownloadResolvedPath(), getRepository().getDownloadRenderType());
                            Map<String, Object> param = new HashMap<>();
                            param.put("eventId", BaseProgramDetailPresenter.EVENT_ADD_DOWNLOAD);
                            param.put("object", GsonUtil.getGson().toJson(downloadBean));
                            downloadServiceManager.excute(param, null);
                            isDownloaded = true;
                            if (getUIView() != null) {
                                getUIView().updateDownloaded(isDownloaded);
                                getUIView().showToast("已加入缓存列表");
                            }
                        }
                    }
                });
        prevueClickDownload();
    }

    private void registDownloadService() {
        Router.getInstance().buildExecutor("/download/service/getDownloadClient").notTransCallbackData().callback(new Executor.Callback<Executor>() {
            @Override
            public void onCall(Executor executor) {
                client = executor;
                Map<String, Object> param = new HashMap<>();
                param.put("isRegist", true);
                param.put("activity", getUIView().getAttachActivity());
                client.excute(param, new Executor.Callback() {
                    @Override
                    public void onCall(Object o) {
                        downloadServiceManager = (Executor) o;
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                        //
                    }
                });
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
            }
        }).excute();
    }

    private void unRegistDownloadService() {
        if (client != null) {
            Map<String, Object> param = new HashMap<>();
            param.put("isRegist", false);
            param.put("activity", getUIView().getAttachActivity());
            client.excute(param, null);
        }
        downloadServiceManager = null;
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        disposeDownload();
        disposeLogin();
    }

    private void disposeDownload() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    private void disposeLogin() {
        if (disposableLogin != null) {
            disposableLogin.dispose();
            disposableLogin = null;
        }
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        unRegistDownloadService();
        programDetailView = null;
    }

    static class DownloadStartedException extends Exception {

    }

    static class DownloadParserException extends Exception {

    }

    //==================bi埋点=====================//

    private void prevueClickDownload(){
        ProgramDetailModel programDetailModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.PROGRAM_INFO);
        if(programDetailModel==null)
            return;
        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                .setEventId(PREVUE_CLICK_DOWNLOAD)
                .setCurrentPageId(ROOT_VIDEO_DETAILS)
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, programDetailModel.getCode())
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, programDetailModel.getDisplayName())
                .setNextPageId(ROOT_VIDEO_DETAILS);
        if (builder != null) {
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

}