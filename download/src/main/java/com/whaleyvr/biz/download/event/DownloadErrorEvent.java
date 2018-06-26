package com.whaleyvr.biz.download.event;

import com.whaleyvr.biz.download.db.DownloadBean;

public class DownloadErrorEvent extends DownloadEvent {

    private Throwable throwable;

    public DownloadErrorEvent(DownloadBean downloadBean, Throwable throwable) {
        super(downloadBean);
        this.throwable=throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
