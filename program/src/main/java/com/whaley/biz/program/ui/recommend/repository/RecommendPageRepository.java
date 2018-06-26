package com.whaley.biz.program.ui.recommend.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Created by YangZhi on 2017/8/14 18:01.
 */

public class RecommendPageRepository extends MemoryRepository{

    private String code;


    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
