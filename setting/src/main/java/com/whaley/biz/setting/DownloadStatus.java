package com.whaley.biz.setting;

public interface DownloadStatus {

    int DOWNLOAD_STATUS_NOTDOWNLOAD = -1;
    int DOWNLOAD_STATUS_NOTSTARTED = 0;
    int DOWNLOAD_STATUS_PREPARED = 1;
    int DOWNLOAD_STATUS_DOWNLOADING = 2;
    int DOWNLOAD_STATUS_PAUSE = 3;
    int DOWNLOAD_STATUS_COMPLETED = 4;
    int DOWNLOAD_STATUS_CANCEL = 5;
    int DOWNLOAD_STATUS_ERROR = 6;

}
