package com.whaley.biz.setting.ui.viewmodel;

import com.whaley.biz.setting.DownloadStatus;
import com.whaley.biz.setting.db.LocalVideoBean;

/**
 * Created by dell on 2017/8/4.
 */

public class LocalVideoViewModel {

    private boolean isOnCheck = false;

    private int status = DownloadStatus.DOWNLOAD_STATUS_PREPARED;

    private LocalVideoBean videoBean;

    public LocalVideoViewModel(LocalVideoBean videoBean){
        this.videoBean = videoBean;
    }

    public LocalVideoBean getVideoBean() {
        return videoBean;
    }

    public void setVideoBean(LocalVideoBean videoBean) {
        this.videoBean = videoBean;
    }

    public boolean isOnCheck() {
        return isOnCheck;
    }

    public void setOnCheck(boolean onCheck) {
        isOnCheck = onCheck;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CheckableVideoBean{" +
                "isOnCheck=" + isOnCheck +
                ", status=" + status +
                ", videoBean=" + videoBean.toString() +
                '}';
    }


}
