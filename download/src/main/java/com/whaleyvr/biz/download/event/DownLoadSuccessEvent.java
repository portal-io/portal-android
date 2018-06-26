package com.whaleyvr.biz.download.event;

import com.whaleyvr.biz.download.db.DownloadBean;

public class DownLoadSuccessEvent extends DownloadEvent {
    public DownLoadSuccessEvent(DownloadBean downloadBean) {
        super(downloadBean);
    }
}
