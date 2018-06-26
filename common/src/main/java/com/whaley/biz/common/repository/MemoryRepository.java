package com.whaley.biz.common.repository;

import com.whaley.core.repository.IRepository;

/**
 * Created by YangZhi on 2017/7/25 17:23.
 */

public class MemoryRepository implements IRepository{

    @Override
    public <T> T obtainService(Class<T> aClass) {
        return (T)this;
    }
}
