package com.whaley.biz.setting.ui.viewmodel;

import com.whaley.biz.setting.model.download.DownloadBean;

/**
 * Created by dell on 2017/8/8.
 */

public class DownloadViewModel {

    public DownloadBean itemData;
    public boolean isSelect = false;

    public DownloadViewModel(DownloadBean itemData) {
        this.itemData = itemData;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof DownloadViewModel) {
            DownloadViewModel downloadViewModel = (DownloadViewModel) obj;
            if (downloadViewModel.itemData != null && downloadViewModel.itemData.getItemid().equals(this.itemData.getItemid())) {
                return true;
            }
            return false;
        }
        return false;
    }

}
