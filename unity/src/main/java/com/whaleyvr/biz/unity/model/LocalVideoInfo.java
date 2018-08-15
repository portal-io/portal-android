package com.whaleyvr.biz.unity.model;

import java.util.List;

/**
 * Created by dell on 2017/9/21.
 */

public class LocalVideoInfo {

    private int operation; //0 query 1 insert 2 delete
    private List<LocalVideoModel> localVideoModelList;
    private boolean result;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public List<LocalVideoModel> getLocalVideoModelList() {
        return localVideoModelList;
    }

    public void setLocalVideoModelList(List<LocalVideoModel> localVideoModelList) {
        this.localVideoModelList = localVideoModelList;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
