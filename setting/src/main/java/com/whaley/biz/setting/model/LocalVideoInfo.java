package com.whaley.biz.setting.model;

import com.whaley.biz.setting.db.LocalVideoBean;

import java.util.List;

/**
 * Created by dell on 2017/9/21.
 */

public class LocalVideoInfo {

    private int operation; //0 query 1 insert 2 delete
    private List<LocalVideoBean> localVideoModelList;
    private boolean result;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public List<LocalVideoBean> getLocalVideoModelList() {
        return localVideoModelList;
    }

    public void setLocalVideoModelList(List<LocalVideoBean> localVideoModelList) {
        this.localVideoModelList = localVideoModelList;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
