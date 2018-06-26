package com.whaley.biz.program.ui.follow.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.program.ui.uimodel.PublisherDetailViewModel;

/**
 * Author: qxw
 * Date:2017/8/16
 * Introduction:
 */

public class PublisherDetailRepository extends MemoryRepository {
    private String mCpCode;
    private PublisherDetailViewModel model;

    public String getmCpCode() {
        return mCpCode;
    }

    public void setmCpCode(String mCpCode) {
        this.mCpCode = mCpCode;
    }


    public PublisherDetailViewModel getModel() {
        return model;
    }

    public void setModel(PublisherDetailViewModel model) {
        this.model = model;
    }
}
