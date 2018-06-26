package com.whaleyvr.biz.download;

import com.whaley.core.debug.logger.Log;
import com.whaleyvr.biz.download.db.DownloadBean;
import com.whaleyvr.biz.download.event.DownLoadSuccessEvent;
import com.whaleyvr.biz.download.event.DownloadErrorEvent;
import com.whaleyvr.biz.download.event.DownloadEvent;
import com.whaleyvr.biz.download.event.DownloadTaskFinishEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class DownloadTask implements IDownloadTask,DownloadStatus {

    private DownloadBean downloadBean;

    DownloadObserver<ResponseBody> downloadObserver;

    Observable<ResponseBody> downloadObservable;

    private Disposable disposable;

    public DownloadTask(){

    }

    public DownloadTask(DownloadBean downloadBean){
        this.downloadBean=downloadBean;
    }

    @Override
    public void execute(){
        downloadObserver=new DownloadObserver<ResponseBody>(downloadBean){
            @Override
            protected void onSuccess(ResponseBody responseBody, DownloadBean downloadBean) {
                super.onSuccess(responseBody, downloadBean);
                DownloadTask.this.downloadBean=downloadBean;
                onFinish();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);
                onDownloadError(e);
            }

            @Override
            protected void onDownloadBeanChanged(DownloadBean downloadBean) {
                super.onDownloadBeanChanged(downloadBean);
                DownloadTask.this.downloadBean=downloadBean;
                updateDataBaseBean();
            }

            @Override
            protected void onProgressChanged(float current, float total, float progressPercent) {
                super.onProgressChanged(current, total, progressPercent);
                DownloadTask.this.onProgressChanged();
            }
        };
        downloadObservable = DownloadHelper.getDownloadObservable(downloadObserver);
        if(disposable!=null){
            disposable.dispose();
        }
        disposable = downloadObservable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribeWith(downloadObserver);
    }

    @Override
    public void cancel() {
        if(disposable!=null){
            disposable.dispose();
        }
        if(downloadObserver !=null){
            downloadObserver.cancel();
        }
    }

    private void onDownloadError(Throwable throwable) {
        int status = DownloadDataManager.getInstance().getStatus(downloadBean.getItemid());
        if(status != DOWNLOAD_STATUS_NOTDOWNLOAD) {
            setStatus(DOWNLOAD_STATUS_ERROR);
            EventBus.getDefault().post(new DownloadErrorEvent(downloadBean, throwable));
        }
        EventBus.getDefault().post(new DownloadTaskFinishEvent());
    }

    private void onProgressChanged() {
        if(DownloadDataManager.getInstance().getStatus(downloadBean.getItemid()) != DOWNLOAD_STATUS_NOTDOWNLOAD) {
            EventBus.getDefault().post(new DownloadEvent(downloadBean));
        }
    }

    private void onFinish() {
        if(downloadBean.getStatus() != DOWNLOAD_STATUS_ERROR) {
            EventBus.getDefault().post(new DownloadTaskFinishEvent());
            if(DownloadDataManager.getInstance().getStatus(downloadBean.getItemid()) != DOWNLOAD_STATUS_NOTDOWNLOAD) {
                setStatus(DOWNLOAD_STATUS_COMPLETED);
                EventBus.getDefault().post(new DownLoadSuccessEvent(downloadBean));
            }
        }
    }

    private void setStatus(int status){
        downloadBean.setStatus(status);
        updateDataBaseBean();
    }

    private void updateDataBaseBean(){
        if(DownloadDataManager.getInstance().getStatus(downloadBean.getItemid()) != DOWNLOAD_STATUS_NOTDOWNLOAD) {
            DownloadDataManager.getInstance().updateNewDownload(downloadBean);
            EventBus.getDefault().post(new DownloadEvent(downloadBean));
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof String){
            String itemId=(String) o;
            return itemId.equals(downloadBean.getItemid());
        }else if(o instanceof DownloadBean){
            DownloadBean bean=(DownloadBean)o;
            return bean.getItemid().equals(downloadBean.getItemid());
        }
        return super.equals(o);
    }

}
