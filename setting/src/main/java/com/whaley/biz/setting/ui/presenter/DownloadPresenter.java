package com.whaley.biz.setting.ui.presenter;

import android.os.Bundle;
import android.view.View;

import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.biz.setting.constant.SettingConstants;
import com.whaley.biz.setting.constant.DownloadConstants;
import com.whaley.biz.setting.DownloadStatus;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.event.ChangeEditEvent;
import com.whaley.biz.setting.event.ExitEditEvent;
import com.whaley.biz.setting.event.MainBackEvent;
import com.whaley.biz.setting.model.download.DownloadBean;
import com.whaley.biz.setting.model.player.DataBuilder;
import com.whaley.biz.setting.model.player.PlayData;
import com.whaley.biz.setting.router.FormatPageModel;
import com.whaley.biz.setting.router.GoPageUtil;
import com.whaley.biz.setting.ui.repository.DownloadRepository;
import com.whaley.biz.setting.ui.view.DownloadView;
import com.whaley.biz.setting.ui.viewmodel.DownloadViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.NetworkUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.whaley.biz.setting.ui.presenter.LocalTabPresenter.STR_PARAM_TYPE;

/**
 * Created by dell on 2017/8/4.
 */

public class DownloadPresenter extends BasePagePresenter<DownloadView> {

    @Repository
    DownloadRepository repository;

    public DownloadPresenter(DownloadView view) {
        super(view);
    }

    public DownloadRepository getDownloadRepository() {
        return repository;
    }

    Executor downloadServiceManager;
    Executor client;

    private Disposable disposable;

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            getDownloadRepository().setType(arguments.getInt(STR_PARAM_TYPE));
        }
        EventController.regist(this);
    }

    @Override
    public void onAttached() {
        super.onAttached();
        registDownloadService();
    }

    @Override
    public void onDetached() {
        super.onDetached();
        unRegistDownloadService();
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
    public void onResume() {
        super.onResume();
        readDownloadBeanFromDB();
    }

    public void changeEdit() {
        getDownloadRepository().setShowEdit(!getDownloadRepository().isShowEdit());
        if (getDownloadRepository().isShowEdit()) {
            getDownloadRepository().setOnCheck(true);
            getDownloadRepository().unheckAll();
            if (getUIView() != null) {
                getUIView().showEdit();
            }
        } else {
            getDownloadRepository().setOnCheck(false);
            getDownloadRepository().unheckAll();
            if (getUIView() != null) {
                getUIView().cancelEdit();
            }
        }
    }

    private void exitEdit() {
        if (getDownloadRepository().isShowEdit()) {
            getDownloadRepository().setShowEdit(false);
            getDownloadRepository().setOnCheck(false);
            getDownloadRepository().unheckAll();
            if (getUIView() != null) {
                getUIView().cancelEdit();
            }
        }
    }

    public void onDeleteClick() {
        if (getDownloadRepository().getCheck() == null || getDownloadRepository().getCheck().size() <= 0) {
            if (getUIView() != null) {
                getUIView().showToast("未选中任何选项");
            }
        } else {
            showDeleteDialog();
        }
    }

    public void delete() {
        deleteCheckedDownloadItem();
        getDownloadRepository().setShowEdit(false);
        getDownloadRepository().setOnCheck(false);
        getDownloadRepository().unheckAll();
        if (getUIView() != null) {
            getUIView().cancelEdit();
        }
        readDownloadBeanFromDB();
    }

    public void showDeleteDialog() {
        DialogUtil.showDialog(getStater().getAttatchContext(), "确定要删除吗", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }

    public void check(int position) {
        DownloadViewModel downloadViewModel = getDownloadRepository().getItemDatas().get(position);
        getDownloadRepository().check(downloadViewModel, !downloadViewModel.isSelect);
        if (getUIView() != null) {
            getUIView().updateCheck(position);
            getUIView().updateCheck();
        }
    }

    private void readDownloadBeanFromDB() {
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = Observable.create(new ObservableOnSubscribe<List<DownloadBean>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<DownloadBean>> emitter) throws Exception {
                Router.getInstance().buildExecutor("/download/service/getDownloadListService").callback(new Executor.Callback<List<DownloadBean>>() {
                    @Override
                    public void onCall(List<DownloadBean> downloadBeanList) {
                        emitter.onNext(downloadBeanList);
                        emitter.onComplete();
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                    }
                }).excute();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<List<DownloadBean>>() {
            @Override
            public void onNext(@NonNull List<DownloadBean> downloadBeanList) {
                getDownloadRepository().setItemDatas(downloadBeanList);
                checkDownload();
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void checkDownload() {
        if (getUIView() != null) {
            List<DownloadViewModel> list = getDownloadRepository().getItemDatas();
            if (list != null && list.size() > 0) {
                getUIView().updateMainList();
            } else {
                getUIView().noList();
            }
        }
    }

    public long getTotalSize(int position) {
        if (getDownloadRepository().getItemDatas() == null || getDownloadRepository().getItemDatas().size() <= 0)
            return 0;
        DownloadBean data = getDownloadRepository().getItemDatas().get(position).itemData;
        return data.totalSize;
    }

    public String getDownloadText(int position) {
        if (getDownloadRepository().getItemDatas() == null || getDownloadRepository().getItemDatas().size() <= 0)
            return null;
        DownloadBean data = getDownloadRepository().getItemDatas().get(position).itemData;
        switch (data.status) {
            case DownloadStatus.DOWNLOAD_STATUS_PREPARED:
                return AppContextProvider.getInstance().getContext().getString(R.string.status_waiting);
            case DownloadStatus.DOWNLOAD_STATUS_DOWNLOADING:
                int progress = (int) (data.getProgress() * 100);
                return progress + "%";
            case DownloadStatus.DOWNLOAD_STATUS_COMPLETED:
                return AppContextProvider.getInstance().getContext().getString(R.string.status_done);
            case DownloadStatus.DOWNLOAD_STATUS_PAUSE:
            case DownloadStatus.DOWNLOAD_STATUS_ERROR:
                return AppContextProvider.getInstance().getContext().getString(R.string.status_continue);
            case DownloadStatus.DOWNLOAD_STATUS_NOTDOWNLOAD:
                return AppContextProvider.getInstance().getContext().getString(R.string.status_download);
            default:
                return "";
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeEditEvent event) {
        if (event.getType() == getDownloadRepository().getType()) {
            changeEdit();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ExitEditEvent event) {
        if (event.getType() == getDownloadRepository().getType()) {
            exitEdit();
            checkDownload();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case DownloadConstants.EVENT_DOWNLOAD:
                DownloadBean downloadBean = event.getObject(DownloadBean.class);
                if (downloadBean != null) {
                    DownloadViewModel downloadViewModel = new DownloadViewModel(downloadBean);
                    if (getDownloadRepository().getItemDatas() != null && getDownloadRepository().getItemDatas().contains(downloadViewModel)) {
                        int position = getDownloadRepository().getItemDatas().indexOf(downloadViewModel);
                        DownloadViewModel date = getDownloadRepository().getItemDatas().get(position);
                        date.itemData = downloadViewModel.itemData;
                        getDownloadRepository().updateItemDownloadStatus(position, date);
                        if (getUIView() != null) {
                            getUIView().updateDownloadText(position);
                        }
                    }
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainBackEvent event) {
        if (getDownloadRepository().isShowEdit()) {
            changeEdit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventController.unRegist(this);
        if (disposable != null) {
            disposable.dispose();
        }
    }


    public void deleteCheckedDownloadItem() {
        if (downloadServiceManager != null) {
            Map<String, Object> param = new HashMap<>();
            param.put("eventId", DownloadConstants.EVENT_REMOVE_DOWNLOAD);
            param.put("object", getDownloadRepository().getCheck());
            downloadServiceManager.excute(param, null);
        }
    }

    public void onItemClick(final int position, boolean isBtnClick) {
        final DownloadBean bean = getDownloadRepository().getItemDatas().get(position).itemData;
        if (!isBtnClick && bean.status != DownloadStatus.DOWNLOAD_STATUS_COMPLETED) {
            return;
        }
        switch (bean.status) {
            case DownloadStatus.DOWNLOAD_STATUS_PREPARED:
            case DownloadStatus.DOWNLOAD_STATUS_DOWNLOADING:
                if (downloadServiceManager != null) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("eventId", DownloadConstants.EVENT_PAUSE_DOWNLOAD);
                    param.put("object", bean.getItemid());
                    downloadServiceManager.excute(param, null);
                }
                if (getUIView() != null)
                    getUIView().updateDownloadText(position);
                break;
            case DownloadStatus.DOWNLOAD_STATUS_COMPLETED:
                File file = new File(bean.savePath);
                if (file.exists()) {
                    GoPageUtil.goPage(getStater(), FormatPageModel.getLocalPlayerPageModel(bean.getPlayData()));
                } else {
                    bean.setStatus(DownloadStatus.DOWNLOAD_STATUS_NOTDOWNLOAD);
                    bean.setCurrentSize(0);
                    bean.setProgress(0);
                    if (getUIView() != null) {
                        getUIView().updateDownloadText(position);
                        getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.text_video_not_exist));
                    }
                    Observable.create(new ObservableOnSubscribe<Boolean>() {
                        @Override
                        public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                            Router.getInstance().buildExecutor("/download/service/updateDownloadService").putObjParam(bean).excute();
                            emitter.onComplete();
                        }
                    }).subscribeOn(Schedulers.io()).subscribe();
                }
                break;
            case DownloadStatus.DOWNLOAD_STATUS_PAUSE:
            case DownloadStatus.DOWNLOAD_STATUS_ERROR:
            case DownloadStatus.DOWNLOAD_STATUS_NOTDOWNLOAD:
                download(bean, position);
                break;
            default:
                break;
        }
    }

    private void startPlay(DownloadBean bean) {
        List<PlayData> playDatas = new ArrayList<>();
        PlayData playData = DataBuilder.createBuilder(bean.savePath, SettingConstants.TYPE_LOCALVIDEO)
                .setId(bean.getItemid())
                .setTitle(bean.getName())
                .putCustomData("key_is_Can_change_render", false)
                .setMonocular(true)
                .build();
        playDatas.add(playData);
        Router.getInstance().buildNavigation("/program/ui/localplayer")
                .setStarter(getStater())
                .withInt(RouterConstants.KEY_ACTIVITY_TYPE, RouterConstants.CONTAINER_ACTIVITY)
                .withString("key_param_datas", GsonUtil.getGson().toJson(playDatas))
                .navigation();
    }

    private void download(final DownloadBean bean, final int position) {
        if (NetworkUtils.isNetworkAvailable()) {
            if (SharedPreferencesUtil.getWifiOnly() && !NetworkUtils.isWiFiActive()) {
                DialogUtil.showWifiDialog(getStater().getAttatchContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resumeDownload(bean, position);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {
                resumeDownload(bean, position);
            }
        }
    }

    private void resumeDownload(DownloadBean bean, int position) {
        if (downloadServiceManager != null) {
            if (!SettingUtil.checkAvailableSpace(bean, getUIView())) {
                return;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("eventId", DownloadConstants.EVENT_RESUME_DOWNLOAD);
            param.put("object", bean.getItemid());
            downloadServiceManager.excute(param, null);
        }
        if (getUIView() != null)
            getUIView().updateDownloadText(position);
    }

    public void onAllClick() {
        if (getDownloadRepository().getCheckNum() == getDownloadRepository().getTotalNum()) {
            getDownloadRepository().unheckAll();
        } else {
            getDownloadRepository().checkAll();
        }
        getUIView().updateCheck();
    }

    public int getCheckNum(){
        return getDownloadRepository().getCheckNum();
    }

    public int getTotalNum(){
        return getDownloadRepository().getTotalNum();
    }

}
