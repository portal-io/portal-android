package com.whaley.biz.program.ui.arrange.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Author: qxw
 * Date:2017/8/17
 * Introduction:
 */

public class ArrangeRopository extends MemoryRepository {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
