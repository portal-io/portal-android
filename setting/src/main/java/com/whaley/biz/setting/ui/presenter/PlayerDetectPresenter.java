package com.whaley.biz.setting.ui.presenter;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.orhanobut.logger.Logger;
import com.whaley.biz.common.response.Response;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.interactor.PlayerDetect;
import com.whaley.biz.setting.model.DectectInfo;
import com.whaley.biz.setting.ui.repository.PlayerDetectRepository;
import com.whaley.biz.setting.ui.view.PlayerDetectView;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.NetworkUtils;
import com.whaley.core.utils.StrUtil;
import com.whaley.core.utils.VersionUtil;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/9/4.
 */

public class PlayerDetectPresenter extends BasePagePresenter<PlayerDetectView> {

    @Repository
    PlayerDetectRepository repository;

    @UseCase
    PlayerDetect playerDetect;

    private Disposable disposable;

    public PlayerDetectPresenter(PlayerDetectView view) {
        super(view);
    }

    public PlayerDetectRepository getRepository(){
        return repository;
    }

    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    private DownloadChangeObserver downloadObserver = null;
    private long myDownloadReference;
    private DownloadManager manager;
    private final String filename = "test4K.mkv";
    private String pathRelated = "/whaleyvr/detect";
    private boolean isDownload = false;
    private boolean isDetect = false;
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        downloadObserver = new DownloadChangeObserver(null);
        getUIView().getAttachActivity().getContentResolver().registerContentObserver(CONTENT_URI, true, downloadObserver);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (myDownloadReference == reference) {
                    String path = checkUpdatePath() + "/" + filename;
                    if (android.os.Build.VERSION.SDK_INT < 23) {
                        Uri downloadFileUri = manager.getUriForDownloadedFile(myDownloadReference);
                        if (downloadFileUri != null && "file".equals(downloadFileUri.getScheme())) {
                            path = downloadFileUri.getPath();
                        }
                    }
                    if (isDownload) {
                        isDetect = true;
                        getRepository().setPath(path);
                        if (getUIView() != null)
                            getUIView().detect(path);
                    }
                    isDownload = false;
                }
            }
        };
        AppContextProvider.getInstance().getContext().registerReceiver(receiver, filter);
    }

    public void onSurfaceTextureAvailable() {
        if (!StrUtil.isEmpty(getRepository().getPath())) {
            if (getUIView() != null)
                getUIView().detect(getRepository().getPath());
        }
    }

    public void onCautionDialogConfirm() {
        downloadVideo("http://store-media.snailvr.com/mobile_check.mp4");
    }

    public void downloadVideo(String downloadUrl) {
        if (isDownload) {
            return;
        }
        if (ContextCompat.checkSelfPermission(getUIView().getAttachActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getUIView().getAttachActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
        if (!SettingUtil.isDownloadManagerAvailable(AppContextProvider.getInstance().getContext())) {
            return;
        }
        getUIView().showProgress(true);
        String path = checkUpdatePath() + "/" + filename;
        final File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        manager = ((android.app.DownloadManager) AppContextProvider.getInstance().getContext().getSystemService(Service.DOWNLOAD_SERVICE));
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setDestinationInExternalPublicDir(pathRelated, filename);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setMimeType("application/vnd.android.package-archive");
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        myDownloadReference = manager.enqueue(request);
        isDownload = true;
    }

    private String checkUpdatePath() {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + pathRelated;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private void queryDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(myDownloadReference);
        Cursor c = manager.query(query);
        if (c != null && c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            int reasonIdx = c.getColumnIndex(DownloadManager.COLUMN_REASON);
            int titleIdx = c.getColumnIndex(DownloadManager.COLUMN_TITLE);
            int fileSizeIdx =
                    c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int bytesDLIdx =
                    c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            String title = c.getString(titleIdx);
            int fileSize = c.getInt(fileSizeIdx);
            int bytesDL = c.getInt(bytesDLIdx);
            // Translate the pause reason to friendly text.
            int reason = c.getInt(reasonIdx);
            StringBuilder sb = new StringBuilder();
            sb.append(title).append("\n");
            sb.append("Downloaded ").append(bytesDL).append(" / ").append(fileSize);
            int progress = (int) (bytesDL * 100.f / fileSize);
            if (getUIView() != null)
                getUIView().showProgress(progress);
            // Display the status
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                case DownloadManager.STATUS_PENDING:
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    //完成
//                  dowanloadmanager.remove(lastDownloadId);
                    break;
                case DownloadManager.STATUS_FAILED:
                    //清除已下载的内容，重新下载
                    manager.remove(myDownloadReference);
                    break;
            }
        }
    }

    class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            queryDownloadStatus();
        }
    }

    public void onDetectCompleted(int frame) {
        isDetect = false;
        String path = checkUpdatePath() + "/" + filename;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        int versionCode = 0;
        String versionName = "";
        try {
            versionCode = VersionUtil.getVersionCode();
            versionName = VersionUtil.getVersionName();
        } catch (Exception e) {
            Logger.e(e, "");
        }
        int ok = frame >= 20 ? 1 : 0;
        DectectInfo info = new DectectInfo();
        info.setFrames(frame);
        info.setSystemname("android");
        info.setSystemcode(String.valueOf(Build.VERSION.SDK_INT));
        info.setAppname(versionName);
        info.setAppcode(String.valueOf(versionCode));
        info.setModel(String.valueOf(android.os.Build.MODEL));
        info.setDetection(ok);
        if(disposable!=null){
            disposable.dispose();
        }
        disposable = playerDetect.buildUseCaseObservable(info)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        int level = frame >= 20 ? SharedPreferencesUtil.DEFINITION_LEVEL_SD :
                SharedPreferencesUtil.DEFINITION_LEVEL_ST;
        if(getUIView() != null) {
            getUIView().showDetectCompleteDialog(level);
        }
    }

    public void onclickStartButton() {
        if (isDownload || isDetect) {
            getUIView().showToast("正在检测中，请耐心等待...");
            return;
        }
        String path = checkUpdatePath() + "/" + filename;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        if (NetworkUtils.getNetworkState() == NetworkUtils.NONE) {
            getUIView().showToast("无网络连接，请检查网络");
        } else if (NetworkUtils.getNetworkState() == NetworkUtils.MOBILE) {
            getUIView().showCautionFlowDialog();
        } else if (NetworkUtils.getNetworkState() == NetworkUtils.WIFI) {
            downloadVideo("http://store-media.snailvr.com/mobile_check.mp4");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isDetect = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            AppContextProvider.getInstance().getContext().unregisterReceiver(receiver);
        }
        if(downloadObserver!=null){
            getUIView().getAttachActivity().getContentResolver().unregisterContentObserver(downloadObserver);
        }
        if (manager != null) {
            manager.remove(myDownloadReference);
        }
        if(disposable!=null){
            disposable.dispose();
        }
    }

}
