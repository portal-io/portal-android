package com.whaleyvr.biz.download.providers;

/**
 * Created by dell on 2017/8/9.
 */

public class DownloadH5PageModel {

    private int callbackId;
    private boolean shouldLoadUrl;
    private String url;
    private String savePath;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

}
