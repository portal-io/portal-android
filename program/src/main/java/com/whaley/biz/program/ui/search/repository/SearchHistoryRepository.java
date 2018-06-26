package com.whaley.biz.program.ui.search.repository;

import com.whaley.biz.common.repository.MemoryRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/24.
 */

public class SearchHistoryRepository extends MemoryRepository{

    private List<String> historyList = new ArrayList<String>();

    public List<String> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<String> historyList) {
        this.historyList = historyList;
    }
}
