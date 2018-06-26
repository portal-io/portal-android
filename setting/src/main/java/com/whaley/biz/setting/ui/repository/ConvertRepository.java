package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Created by dell on 2017/8/4.
 */

public class ConvertRepository extends MemoryRepository{

    private String code;

    private boolean isUnity;

    private boolean convertResult;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isUnity() {
        return isUnity;
    }

    public void setUnity(boolean unity) {
        isUnity = unity;
    }

    public boolean isConvertResult() {
        return convertResult;
    }

    public void setConvertResult(boolean convertResult) {
        this.convertResult = convertResult;
    }
}
