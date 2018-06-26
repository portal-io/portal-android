package com.whaleyvr.biz.download;

import com.whaley.core.debug.logger.Log;
import com.whaleyvr.biz.download.db.DownloadBean;
import com.whaleyvr.biz.download.error.DownloadException;
import com.whaleyvr.biz.download.util.DownloadUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * Created by dell on 2017/8/9.
 */

public class DownloadObserver<T extends ResponseBody> extends DisposableObserver<T> {

    private final static int UPDATE_SIZE = 50 * 1024;

    private boolean isCanceled=false;

    private DownloadBean downloadBean;

    private boolean isShouldRange = true;

    public DownloadObserver(DownloadBean downloadBean){
        this.downloadBean=downloadBean;
    }

    public DownloadObserver(DownloadBean downloadBean, boolean isShouldRange){
        this.downloadBean=downloadBean;
        this.isShouldRange = isShouldRange;
    }

    public DownloadBean getDownloadBean() {
        return downloadBean;
    }

    @Override
    public void onNext(@NonNull T t) {
        if(isCanceled())
            return;
        InputStream inputStream = null;
        BufferedInputStream bis = null;
        RandomAccessFile file=null;
        try {
            if (t!=null) {
                    int i=downloadBean.getSavePath().lastIndexOf(File.separator);
                    if(i>-1){
                        String dirPath=downloadBean.getSavePath().substring(0,i);
                        File dirFile=new File(dirPath);
                        if (!dirFile.exists()){
                            dirFile.mkdirs();
                        }
                    }
                    file = new RandomAccessFile(downloadBean.getSavePath()+".temp", "rwd");
                    if(file.length()<=0){
                        downloadBean.setCurrentSize(0);
                        onDownloadBeanChanged(downloadBean);
                    }else if(file.length()>=downloadBean.getTotalSize() && downloadBean.getTotalSize() > 0&&isShouldRange){
                        downloadBean.setProgress(1f);
                        downloadBean.setStatus(DownloadStatus.DOWNLOAD_STATUS_COMPLETED);
                        onDownloadBeanChanged(downloadBean);
                        onProgressChanged(downloadBean.getTotalSize(),downloadBean.getTotalSize(),downloadBean.getProgress());
                        onSuccess(t, downloadBean);
                        return;
                    }
                    if(downloadBean.getTotalSize()<=0){
                        downloadBean.setTotalSize(t.contentLength());
                        onDownloadBeanChanged(downloadBean);
                    }
                    if(!DownloadUtil.checkAvailableSpace(downloadBean)){
                        downloadBean.setStatus(DownloadStatus.DOWNLOAD_STATUS_ERROR);
                        onError(new DownloadException("剩余空间不足.."));
                        return;
                    }
                    if(!isShouldRange){
                        //返回的没有Content-Range 不支持断点下载 需要重新下载
                        File tempFile = new File(downloadBean.getSavePath()+".temp");
                        if(tempFile.exists()){
                            tempFile.delete();
                        }

                        File successFile = new File(downloadBean.getSavePath());
                        if(successFile.exists()){
                            successFile.delete();
                        }

                        file = new RandomAccessFile(downloadBean.getSavePath()+".temp", "rwd");
                        downloadBean.setCurrentSize(0);
                        onDownloadBeanChanged(downloadBean);
                    }
                    file.seek(downloadBean.getCurrentSize());
                    inputStream = t.byteStream();
                    bis = new BufferedInputStream(inputStream);
                    byte[] buffer = new byte[2 * 1024];
                    int length = 0;
                    int buffOffset = 0;
                    onDownloadBeanChanged(downloadBean);
                    while ((length = bis.read(buffer)) > 0 && !isCanceled()) {
                        file.write(buffer, 0, length);
                        downloadBean.setCurrentSize(downloadBean.getCurrentSize()+length);
                        buffOffset += length;
                        if (buffOffset >= UPDATE_SIZE) {
                            // Update download database
                            buffOffset = 0;
                            float progress = 1f*downloadBean.getCurrentSize() / downloadBean.getTotalSize();
                            downloadBean.setProgress(progress);
                            downloadBean.setStatus(DownloadStatus.DOWNLOAD_STATUS_DOWNLOADING);
                            onDownloadBeanChanged(downloadBean);
                            onProgressChanged(downloadBean.getCurrentSize(),downloadBean.getTotalSize(),downloadBean.getProgress());
                        }
                    }
                    if(!isCanceled()) {
                        downloadBean.setProgress(1f);
                        onDownloadBeanChanged(downloadBean);
                        onProgressChanged(downloadBean.getCurrentSize(),downloadBean.getTotalSize(),downloadBean.getProgress());

                        File tempFile = new File(downloadBean.getSavePath()+".temp");
                        File successFile=new File(downloadBean.getSavePath());
                        boolean isSuceess=tempFile.renameTo(successFile);
                        if(isSuceess) {
                            onSuccess(t, downloadBean);
                        }else {
                            onError(new DownloadException("修改文件名失败.."));
                        }
                    }else {
                        onError(new InterruptedIOException("Download is Cancel"));
                    }

                }else {
                    onError(new DownloadException("Response body is Null"));
                }
        }catch (Exception e){
            onError(e);
        }finally {
            if (bis != null) try {
                bis.close();
            } catch (IOException e) {
            }
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException e) {
            }
            if (file != null) try {
                file.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    protected void onSuccess(T t, DownloadBean downloadBean){

    }


    protected void onProgressChanged(float current,float total,float progressPercent){

    }


    protected void onDownloadBeanChanged(DownloadBean downloadBean){

    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void cancel() {
        isCanceled = true;
        if(!isDisposed()){
            dispose();
        }
    }

}
