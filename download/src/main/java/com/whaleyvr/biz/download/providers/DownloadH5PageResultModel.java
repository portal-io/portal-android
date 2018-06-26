package com.whaleyvr.biz.download.providers;

/**
 * Created by dell on 2017/8/9.
 */

public class DownloadH5PageResultModel {

    private int callbackId;
    private boolean shouldLoadUrl;
    private boolean isSuccess;
    private String filePath;
    private Throwable throwable;

    public DownloadH5PageResultModel(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(int callbackId) {
        this.callbackId = callbackId;
    }

    public boolean isShouldLoadUrl() {
        return shouldLoadUrl;
    }

    public void setShouldLoadUrl(boolean shouldLoadUrl) {
        this.shouldLoadUrl = shouldLoadUrl;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
