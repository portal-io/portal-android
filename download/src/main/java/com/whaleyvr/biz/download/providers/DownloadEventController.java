package com.whaleyvr.biz.download.providers;

import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.core.appcontext.AppFileStorage;
import com.whaley.core.debug.logger.Log;
import com.whaleyvr.biz.download.DownloadHelper;
import com.whaleyvr.biz.download.DownloadObserver;
import com.whaleyvr.biz.download.DownloadTask;
import com.whaleyvr.biz.download.IDownloadTask;
import com.whaleyvr.biz.download.db.DownloadBean;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dell on 2017/8/9.
 */

public class DownloadEventController  {

    private volatile static DownloadEventController instance;

    private DownloadEventController() {

    }

    public static DownloadEventController getInstance() {
        if (instance == null) {
            synchronized (DownloadEventController.class) {
                if (instance == null) {
                    instance = new DownloadEventController();
                }
            }
        }
        return instance;
    }

    public void downloadSplashImage(final String url) {
        if (downloadTasks.get(url) != null)
            return;
        IDownloadTask task = new DownloadTask(){
            @Override
            public void execute() {
                final String savePath = AppFileStorage.getImagePath() + File.separator + System.currentTimeMillis() + ".jpg";
                DownloadObserver<ResponseBody> downloadObserver = new DownloadObserver<ResponseBody>
                        (DownloadBean.create(url, savePath), false) {
                    @Override
                    protected void onSuccess(ResponseBody responseBody, DownloadBean downloadBean) {
                        super.onSuccess(responseBody, downloadBean);
                        Log.d("downloadSplashImage success");
                        try {
//                            SharedPreferencesUtil.setSplashLastUpdate(url, savePath);
                        } catch (Exception e) {
                            Log.e(e, "downloadImage");
                        }
                    }
                };
                //将原文件删除重新下载
                Observable<ResponseBody> downloadObservable = DownloadHelper.getDownloadObservable(downloadObserver, false);
                DownloadHelper.executeDownload(downloadObservable, downloadObserver);
            }
        };
        task.execute();
        downloadTasks.put(url, task);
    }

    private ConcurrentHashMap<String, IDownloadTask> downloadTasks = new ConcurrentHashMap<>();

    public void downloadH5Page(final DownloadH5PageModel model, final downloadH5PageCallback downloadH5PageCallback) {
        if (downloadTasks.get(model.getUrl()) != null)
            return;
        IDownloadTask task = new DownloadTask(){
            @Override
            public void execute() {
                DownloadObserver<ResponseBody> downloadObserver = new DownloadObserver<ResponseBody>
                        (DownloadBean.create(model.getUrl(), model.getSavePath()), false) {
                    @Override
                    protected void onSuccess(ResponseBody responseBody, DownloadBean downloadBean) {
                        super.onSuccess(responseBody, downloadBean);
                        Log.e("download h5page onSuccess");
                        DownloadH5PageResultModel resultEvent = new DownloadH5PageResultModel(true);
                        resultEvent.setCallbackId(model.getCallbackId());
                        resultEvent.setShouldLoadUrl(model.isShouldLoadUrl());
                        resultEvent.setFilePath(downloadBean.getSavePath());
                        downloadTasks.remove(model.getUrl());
                        if(downloadH5PageCallback!=null){
                            downloadH5PageCallback.onResult(resultEvent);
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        if (e != null)
                            Log.e("download h5page onFailure : " + e.getMessage());
                        DownloadH5PageResultModel resultEvent = new DownloadH5PageResultModel(false);
                        resultEvent.setCallbackId(model.getCallbackId());
                        resultEvent.setThrowable(e);
                        downloadTasks.remove(model.getUrl());
                        if(downloadH5PageCallback!=null){
                            downloadH5PageCallback.onResult(resultEvent);
                        }
                    }
                };
                //将原文件删除重新下载
                Observable<ResponseBody> downloadObservable = DownloadHelper.getDownloadObservable(downloadObserver, false);
                DownloadHelper.executeDownload(downloadObservable, downloadObserver);
            }
        };
        task.execute();
        downloadTasks.put(model.getUrl(), task);
    }

    interface downloadH5PageCallback{
        void onResult(DownloadH5PageResultModel model);
    }


}
