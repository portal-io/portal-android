package com.whaley.biz.setting.ui.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.biz.common.utils.SpaceUtil;
import com.whaley.biz.setting.util.CodeUtils;
import com.whaley.biz.setting.constant.SettingConstants;
import com.whaley.biz.setting.constant.DownloadConstants;
import com.whaley.biz.setting.DownloadStatus;
import com.whaley.biz.setting.util.QRcodeUtil;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.db.LocalVideoBean;
import com.whaley.biz.setting.db.LocalVideoDatabseManager;
import com.whaley.biz.setting.event.ChangeEditEvent;
import com.whaley.biz.setting.event.ExitEditEvent;
import com.whaley.biz.setting.event.ImportVideoEvent;
import com.whaley.biz.setting.event.MainBackEvent;
import com.whaley.biz.setting.event.PermissionGrantedEvent;
import com.whaley.biz.setting.model.download.DownloadBean;
import com.whaley.biz.setting.router.FormatPageModel;
import com.whaley.biz.setting.router.GoPageUtil;
import com.whaley.biz.setting.ui.repository.LocalVideoRepository;
import com.whaley.biz.setting.ui.view.LocalImportByUrlFragment;
import com.whaley.biz.setting.ui.view.LocalImportByVideoFragment;
import com.whaley.biz.setting.ui.view.LocalVideoView;
import com.whaley.biz.setting.ui.view.QRcodeActivity;
import com.whaley.biz.setting.ui.viewmodel.LocalVideoViewModel;
import com.whaley.core.appcontext.AppFileStorage;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.NetworkUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

/**
 * Created by dell on 2017/8/4.
 */

public class LocalVideoPresenter extends BasePagePresenter<LocalVideoView> {

    @Repository
    LocalVideoRepository repository;

    public LocalVideoPresenter(LocalVideoView view) {
        super(view);
    }

    public LocalVideoRepository getLocalVideoRepository() {
        return repository;
    }

    private String videoDownloadPath = "/whaleyvr/video_import";
    Executor downloadServiceManager;
    Executor client;
    private ConnectionChangeReceiver netReceiver;

    private Disposable getVideoDisposable, fetchImageDisposable;

    public boolean isCheck() {
        return getLocalVideoRepository().isOnCheck();
    }


    class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mobNetInfo != null && wifiNetInfo != null &&
                    !mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                // 网络不可以用
            } else {
                if (downloadServiceManager != null) {
                    for (LocalVideoViewModel bean : getLocalVideoRepository().getLocalVideoBeanList()) {
                        if (bean.getVideoBean().isDowloading) {
                            Map<String, Object> param = new HashMap<>();
                            param.put("eventId", DownloadConstants.EVENT_RESUME_DOWNLOAD);
                            param.put("object", bean.getVideoBean().id);
                            downloadServiceManager.excute(param, null);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            getLocalVideoRepository().setType(arguments.getInt(LocalTabPresenter.STR_PARAM_TYPE));
        }
        regist();
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        netReceiver = new ConnectionChangeReceiver();
        getStater().getAttatchContext().registerReceiver(netReceiver, filter);
    }

    private String checkUpdatePath(Context mContext) {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + videoDownloadPath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public DownloadBean downloadVideo(String downloadUrl) {
        final boolean[] flag = {false};
        final DownloadBean bean = DownloadBean.create(downloadUrl);
        if (NetworkUtils.isNetworkAvailable()) {
            if (SharedPreferencesUtil.getWifiOnly() && !NetworkUtils.isWiFiActive()) {
                DialogUtil.showWifiDialog(getStater().getAttatchContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!SettingUtil.checkAvailableSpace(bean, getUIView())) {
                            return;
                        }
                        if (downloadServiceManager != null) {
                            Map<String, Object> param = new HashMap<>();
                            param.put("eventId", DownloadConstants.EVENT_ADD_DOWNLOAD);
                            param.put("object", GsonUtil.getGson().toJson(bean));
                            downloadServiceManager.excute(param, null);
                        }
                        flag[0] = true;
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {
                if (!SettingUtil.checkAvailableSpace(bean, getUIView())) {
                    return null;
                }
                if (downloadServiceManager != null) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("eventId", DownloadConstants.EVENT_ADD_DOWNLOAD);
                    param.put("object", GsonUtil.getGson().toJson(bean));
                    downloadServiceManager.excute(param, null);
                }
                flag[0] = true;
            }
        } else {
            if (getUIView() != null) {
                getUIView().showToast("网络异常，请检查后重试");
            }
            return null;
        }
        if (flag[0]) {
            return bean;
        } else {
            return null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionGrantedEvent event) {
        if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(event.getType())) {
            getLocalVideo();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImportVideoEvent event) {
        getLocalVideo();
    }

    private void getLocalVideo() {
        if (getVideoDisposable != null) {
            getVideoDisposable.dispose();
        }
        getVideoDisposable = Observable.create(new ObservableOnSubscribe<List<LocalVideoViewModel>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<LocalVideoViewModel>> e) throws Exception {
                readDownloadBeanFromDB();
                for (LocalVideoViewModel bean : getLocalVideoRepository().getLocalVideoBeanList()) {
                    if (bean.getVideoBean().isDowloading) {
                        File file = new File(bean.getVideoBean().getPath());
                        if (file.exists() && file.length() >= bean.getVideoBean().getSize() && bean.getVideoBean().getSize() > 0) {
                            bean.getVideoBean().isDowloading = false;
                            bean.setStatus(DownloadStatus.DOWNLOAD_STATUS_COMPLETED);
                            LocalVideoDatabseManager.getInstance().insertOrReplace(bean.getVideoBean());
                        } else if (downloadServiceManager != null) {
                            Map<String, Object> param = new HashMap<>();
                            param.put("eventId", DownloadConstants.EVENT_RESUME_DOWNLOAD);
                            param.put("object", bean.getVideoBean().id);
                            downloadServiceManager.excute(param, null);
                        }
                    }
                }
                e.onNext(getLocalVideoRepository().getLocalVideoBeanList());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<LocalVideoViewModel>>() {
                    @Override
                    public void onNext(@NonNull List<LocalVideoViewModel> localVideoViewModels) {
                        checkEmpty();
                        if (getUIView() != null) {
                            if (getLocalVideoRepository().getLocalVideoBeanList() != null
                                    && getLocalVideoRepository().getLocalVideoBeanList().size() > 0) {
                                getUIView().updateData();
                                FetchImage();
                            } else {
                                getUIView().noData();
                            }
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

    private void check() {
        if (getUIView() != null) {
            if (getLocalVideoRepository().getLocalVideoBeanList() != null
                    && getLocalVideoRepository().getLocalVideoBeanList().size() > 0) {
                getUIView().updateData();
            } else {
                getUIView().noData();
            }
        }
    }

    private void FetchImage() {
        if (fetchImageDisposable != null) {
            fetchImageDisposable.dispose();
        }
        fetchImageDisposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                List<LocalVideoViewModel> alist = new ArrayList<>();
                alist.addAll(getLocalVideoRepository().getLocalVideoBeanList());
                for (LocalVideoViewModel item : alist) {
                    String picPath = retrieveVideoThumbnail(item.getVideoBean());
                    if (!TextUtils.isEmpty(picPath)) {
                        e.onNext(alist.indexOf(item));
                    }
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(@NonNull Integer position) {
                        if (getUIView() != null) {
                            getUIView().updatePosition(position);
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

    private String retrieveVideoThumbnail(LocalVideoBean info) {
        if (info.isDowloading) {
            return "";
        }
        Activity activity = getUIView().getAttachActivity();
        if (activity == null)
            return null;
        File img = new File(AppFileStorage.getImagePath() + File.separator + info.name
                + ".png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        int scale = (options.outHeight / DisplayUtil.convertDIP2PX(68)) >
                (options.outWidth / DisplayUtil.convertDIP2PX(120))
                ? (options.outWidth / DisplayUtil.convertDIP2PX(68))
                : (options.outHeight / DisplayUtil.convertDIP2PX(120));
        options.inSampleSize = scale;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = null;
        if (img.isFile() && img.exists()) {
            bm = BitmapFactory.decodeFile(AppFileStorage.getImagePath() + File.separator
                    + info.name + ".png", options);
            info.picPath = img.getAbsolutePath();
        }
        if (null == bm && activity != null) {
            try {
                bm = MediaStore.Video.Thumbnails.getThumbnail(activity
                                .getContentResolver(), Long.valueOf(info.id),
                        MediaStore.Images.Thumbnails.MINI_KIND, options);
            } catch (Exception e) {
                //
            }
            if (null != bm) {
                info.picPath = saveBitmap(bm, info.name + ".png");
            } else {
                bm = ThumbnailUtils.createVideoThumbnail(info.path, MediaStore.Images.Thumbnails.MINI_KIND);
                if (null != bm) {
                    info.picPath = saveBitmap(bm, info.name + ".png");
                }
            }
        }
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
        return info.picPath;
    }

    private String saveBitmap(Bitmap bm, String videoName) {
        File f = new File(AppFileStorage.getImagePath() + File.separator + videoName);
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            return null;
        }
    }

    public void onPlayerClick(int position) {
        LocalVideoBean bean = getLocalVideoRepository().getLocalVideoBeanList().get(position).getVideoBean();
        File file = new File(bean.getPath());
        if (bean.isDowloading == false && !file.exists()) {
            getUIView().showToast("文件已经不存在");
            LocalVideoDatabseManager.getInstance().delete(bean);
            getLocalVideo();
            return;
        }
        if (bean.isDowloading) {
            return;
        }
        GoPageUtil.goPage(getStater(), FormatPageModel.getLocalPlayerPageModel(bean.getPlayData()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getVideoDisposable != null) {
            getVideoDisposable.dispose();
        }
        if (fetchImageDisposable != null) {
            fetchImageDisposable.dispose();
        }
        unRegist();
        getStater().getAttatchContext().unregisterReceiver(netReceiver);
        downloadServiceManager = null;
    }

    public void onClickQrcode() {
        exitEdit();
        if (!QRcodeUtil.setCameraParams() ||
                ContextCompat.checkSelfPermission(getUIView().getAttachActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getUIView().getAttachActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getUIView().getAttachActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    SettingConstants.REQUEST_CAMERA);
            getUIView().showToast("未开启相机权限，如有必要，请到权限管理中设置");
            return;
        }
        QRcodeActivity.goPage((Starter) getUIView().getFragment());
    }

    public void onClickLink(Fragment context) {
        exitEdit();
        LocalImportByUrlFragment.goPage(context, SettingConstants.CODE_RESULT_FOR_URL);
    }

    public void onClickGallery() {
        exitEdit();
        if (ContextCompat.checkSelfPermission(getUIView().getAttachActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getUIView().getAttachActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
            return;
        }
        LocalImportByVideoFragment.goPage((Starter) getUIView().getFragment());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == SettingConstants.CODE_RESULT_FOR_URL) {
            if (!NetworkUtils.isNetworkAvailable()) {
                getUIView().showToast("当前网络无连接，请连接后重试");
                return;
            }
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == QRcodeUtil.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    try {
                        URL url = new URL(result);
                    } catch (MalformedURLException e) {
                        getUIView().showToast("请输入正确的网址格式");
                        return;
                    }
                    DownloadBean beanDownload = downloadVideo(result);
                    if (beanDownload != null) {
                        LocalVideoBean bean = new LocalVideoBean();
                        bean.duration = 0;
                        bean.id = beanDownload.getItemid();
                        bean.isDowloading = true;
                        bean.lastUpdateTime = System.currentTimeMillis();
                        bean.name = beanDownload.name;
                        bean.path = beanDownload.savePath;
                        bean.progress = 0;
                        bean.progressSize = 0;
                        bean.size = 0;
                        bean.picPath = "";
                        getLocalVideoRepository().getLocalVideoBeanList().add(0, new LocalVideoViewModel(bean));
                        getUIView().updateData();
                        getUIView().showEditButton();
                        LocalVideoDatabseManager.getInstance().insertOrReplace(bean);
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == QRcodeUtil.RESULT_FAILED) {
                    if (getUIView() != null) {
                        getUIView().showToast("二维码解析失败");
                    }
                }
            }
        }
    }


    public LocalVideoViewModel findBean(String id) {
        LocalVideoViewModel bean = null;
        List<LocalVideoViewModel> beans = getLocalVideoRepository().getLocalVideoBeanList();
        for (LocalVideoViewModel beanItem : beans) {
            if (beanItem.getVideoBean().id.equals(id)) {
                bean = beanItem;
                break;
            }
        }
        return bean;
    }

    public synchronized int getCheckNum() {
        int checkNum = 0;
        for (LocalVideoViewModel bean : getLocalVideoRepository().getLocalVideoBeanList()) {
            if (bean.isOnCheck()) {
                checkNum++;
                if (!getLocalVideoRepository().getCheckedIDs().contains(bean.getVideoBean().getId())) {
                    getLocalVideoRepository().getCheckedIDs().add(bean.getVideoBean().getId());
                }
            } else {
                if (getLocalVideoRepository().getCheckedIDs().contains(bean.getVideoBean().getId())) {
                    getLocalVideoRepository().getCheckedIDs().remove(bean.getVideoBean().getId());
                }
            }
        }
        return checkNum;
    }

    public int getTotalNum() {
        return getLocalVideoRepository().getLocalVideoBeanList().size();
    }

    public void onDeleteClick() {
        boolean notCheck = true;
        for (LocalVideoViewModel bean : getLocalVideoRepository().getLocalVideoBeanList()) {
            if (bean.isOnCheck()) notCheck = false;
        }
        if (notCheck) {
            if (getUIView() != null) {
                getUIView().showToast("未选中任何选项");
            }
        } else {
            showDeleteDialog();
        }
    }

    public void showDeleteDialog() {
        DialogUtil.showDialogCustomConfirm(getUIView().getAttachActivity(),
                "继续操作将会\n从列表里移除选中的视频，\n确定继续吗？",
                null, "继续", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete();
                    }
                }, null);
    }

    public void delete() {
        deleteCheckedDownloadItem();
        getLocalVideoRepository().setShowEdit(false);
        getLocalVideoRepository().setOnCheck(false);
        getLocalVideoRepository().unCheckAll();
        if (getUIView() != null) {
            getUIView().cancelEdit();
        }
        getLocalVideo();
    }

    public void deleteCheckedDownloadItem() {
        if (downloadServiceManager != null) {
            Map<String, Object> param = new HashMap<>();
            param.put("eventId", DownloadConstants.EVENT_REMOVE_DOWNLOAD);
            param.put("object", getLocalVideoRepository().getCheckedIDs());
            downloadServiceManager.excute(param, null);
        }
        LocalVideoDatabseManager.getInstance().deleteListByIds(getLocalVideoRepository().getCheckedIDs());
    }

    public void checkEmpty() {
        if (getUIView() == null) {
            return;
        }
        if (getLocalVideoRepository().getLocalVideoBeanList() != null
                && getLocalVideoRepository().getLocalVideoBeanList().size() > 0) {
            getUIView().showEditButton();
        } else {
            getUIView().hideEditButton();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case DownloadConstants.EVENT_DOWNLOAD:
                DownloadBean downloadBean = event.getObject(DownloadBean.class);
                if (downloadBean != null) {
                    LocalVideoViewModel checkableVideoBean = findBean(downloadBean.getItemid());
                    if (checkableVideoBean == null) {
                        return;
                    }
                    final LocalVideoBean bean = checkableVideoBean.getVideoBean();
                    if (bean != null && getUIView() != null) {
                        final int position = getLocalVideoRepository().getLocalVideoBeanList().indexOf(checkableVideoBean);
                        if (bean.getSize() >= SpaceUtil.getAvailableSize()) {
                            List<String> cancelID = new ArrayList<>();
                            cancelID.add(bean.getId());
                            if (downloadServiceManager != null) {
                                Map<String, Object> param = new HashMap<>();
                                param.put("eventId", DownloadConstants.EVENT_REMOVE_DOWNLOAD);
                                param.put("object", cancelID);
                                downloadServiceManager.excute(param, null);
                            }
                            LocalVideoDatabseManager.getInstance().delete(bean);
                            if (getUIView() != null && getUIView().getAttachActivity() != null) {
                                getUIView().getAttachActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getUIView().updateData();
                                        getUIView().showMemoryFullDialog();
                                    }
                                });
                            }
                            return;
                        }
                        if (downloadBean.status == DownloadStatus.DOWNLOAD_STATUS_COMPLETED ||
                                downloadBean.status == DownloadStatus.DOWNLOAD_STATUS_CANCEL) {
                            Bitmap bm = ThumbnailUtils.createVideoThumbnail(bean.path, MediaStore.Images.Thumbnails.MINI_KIND);
                            if (null != bm) {
                                bean.picPath = saveBitmap(bm, bean.name + ".png");
                            }
                            bean.isDowloading = false;
                        }
                        checkableVideoBean.setStatus(downloadBean.status);
                        bean.size = downloadBean.getTotalSize();
                        long duration = System.currentTimeMillis() - bean.lastUpdateTime;
                        if (duration > 1000) {
                            bean.speed = (long) ((downloadBean.getCurrentSize() - bean.progressSize) * 1000.0f / duration);
                            bean.lastUpdateTime = System.currentTimeMillis();
                        }
                        bean.progressSize = downloadBean.getCurrentSize();
                        int progress = (int) (downloadBean.getCurrentSize() * 100.f / downloadBean.getTotalSize());
                        bean.progress = progress;
                        LocalVideoDatabseManager.getInstance().insertOrReplace(bean);
                        if (getUIView() != null && getUIView().getAttachActivity() != null) {
                            getUIView().getAttachActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getUIView().updatePosition(position);
                                }
                            });
                        }
                    }
                }
                break;
        }
    }

    private void readDownloadBeanFromDB() {
        List<LocalVideoBean> beans = LocalVideoDatabseManager.getInstance().queryAllList();
        List<LocalVideoViewModel> checkableVideoBeens = new ArrayList<>();
        for (LocalVideoBean bean : beans) {
            checkableVideoBeens.add(new LocalVideoViewModel(bean));
        }
        getLocalVideoRepository().setLocalVideoBeanList(checkableVideoBeens);
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
                        if (ContextCompat.checkSelfPermission(getUIView().getAttachActivity(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getUIView().getAttachActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    SettingConstants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        } else {
                            getLocalVideo();
                        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeEditEvent event) {
        if (event.getType() == getLocalVideoRepository().getType()) {
            changeEdit();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ExitEditEvent event) {
        if (event.getType() == getLocalVideoRepository().getType()) {
            exitEdit();
            check();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainBackEvent event) {
        if (getLocalVideoRepository().isShowEdit()) {
            changeEdit();
        }
    }

    public void changeEdit() {
        getLocalVideoRepository().setShowEdit(!getLocalVideoRepository().isShowEdit());
        if (getLocalVideoRepository().isShowEdit()) {
            getLocalVideoRepository().setOnCheck(true);
            if (getUIView() != null) {
                getUIView().showEdit();
            }
        } else {
            getLocalVideoRepository().setOnCheck(false);
            getLocalVideoRepository().unCheckAll();
            if (getUIView() != null) {
                getUIView().cancelEdit();
            }
        }
    }

    private void exitEdit() {
        if (getLocalVideoRepository().isShowEdit()) {
            getLocalVideoRepository().setShowEdit(false);
            getLocalVideoRepository().setOnCheck(false);
            getLocalVideoRepository().unCheckAll();
            if (getUIView() != null) {
                getUIView().cancelEdit();
            }
        }
    }

    public void check(String itemid, boolean isChecked) {
        getLocalVideoRepository().check(itemid, isChecked);
        if (getUIView() != null) {
            getUIView().updateCheck();
        }
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        if (getVideoDisposable != null) {
            getVideoDisposable.dispose();
        }
        if (fetchImageDisposable != null) {
            fetchImageDisposable.dispose();
        }
    }

    public void onAllClick() {
        if (getLocalVideoRepository().getLocalVideoBeanList().size() == getLocalVideoRepository().getCheckedIDs().size()) {
            getLocalVideoRepository().unCheckAll();
        } else {
            getLocalVideoRepository().checkAll();
        }
        getUIView().updateCheck();
    }

}
