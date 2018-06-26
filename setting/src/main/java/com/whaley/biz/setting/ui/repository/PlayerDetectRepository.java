package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Created by dell on 2017/9/4.
 */

public class PlayerDetectRepository extends MemoryRepository {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
