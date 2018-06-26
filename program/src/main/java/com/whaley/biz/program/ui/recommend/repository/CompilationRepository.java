package com.whaley.biz.program.ui.recommend.repository;

import com.whaley.biz.program.uiview.repository.RecyclerUIViewRepository;

/**
 * Created by dell on 2017/8/15.
 */

public class CompilationRepository extends RecyclerUIViewRepository {

    private String code;
    private String bitCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBitCode() {
        return bitCode;
    }

    public void setBitCode(String bitCode) {
        this.bitCode = bitCode;
    }

}
