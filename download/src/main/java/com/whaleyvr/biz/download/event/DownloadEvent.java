package com.whaleyvr.biz.download.event;

import com.whaley.biz.common.event.BaseEvent;
import com.whaleyvr.biz.download.db.DownloadBean;
import com.whaleyvr.biz.download.util.DownloadConstants;

public class DownloadEvent extends BaseEvent {

    private DownloadBean downloadBean;

    public DownloadEvent(DownloadBean downloadBean) {
        super(DownloadConstants.EVENT_DOWNLOAD, downloadBean);
        this.downloadBean = downloadBean;
    }

    public DownloadBean getDownloadBean(){
        return downloadBean;
    }

}
