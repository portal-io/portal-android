package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.setting.util.QRcodeUtil;

/**
 * Created by dell on 2017/8/7.
 */

public class QRcodeRepository extends MemoryRepository {

    private int scanResult = QRcodeUtil.RESULT_CANCEL;

    private String callbackId;

    public int getScanResult() {
        return scanResult;
    }

    public void setScanResult(int scanResult) {
        this.scanResult = scanResult;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }
}
