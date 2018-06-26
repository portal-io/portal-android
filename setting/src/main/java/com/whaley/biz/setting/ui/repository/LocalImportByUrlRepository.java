package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Created by dell on 2017/8/7.
 */

public class LocalImportByUrlRepository extends MemoryRepository{

    private String url;
    private long totalMemory;
    private long leaveMemory;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public long getLeaveMemory() {
        return leaveMemory;
    }

    public void setLeaveMemory(long leaveMemory) {
        this.leaveMemory = leaveMemory;
    }
}
