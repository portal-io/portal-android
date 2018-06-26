package com.whaley.biz.program.ui.follow.repository;

import com.whaley.biz.program.uiview.repository.RecyclerUIViewRepository;

/**
 * Author: qxw
 * Date:2017/8/16
 * Introduction:
 */

public class CpProgramRepository extends RecyclerUIViewRepository {
    private String cpCode;
    private String mSortOrder;


    public String getmSortOrder() {
        return mSortOrder;
    }

    public void setmSortOrder(String mSortOrder) {
        this.mSortOrder = mSortOrder;
    }

    public String getCpCode() {
        return cpCode;
    }

    public void setCpCode(String cpCode) {
        this.cpCode = cpCode;
    }
}
