package com.whaleyvr.biz.hybrid.event;

/**
 * Created by dell on 2017/8/9.
 */

public class DownloadH5PageModel {

    private static int updateId=0;

    private int callbackId;
    private boolean shouldLoadUrl;
    private String url;
    private String savePath;

    public DownloadH5PageModel(String url, String savePath) {
        this.url = url;
        this.savePath = savePath;
        this.callbackId=updateId++;
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
