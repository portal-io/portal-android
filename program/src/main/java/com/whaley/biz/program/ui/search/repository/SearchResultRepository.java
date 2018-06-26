package com.whaley.biz.program.ui.search.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Created by dell on 2017/8/25.
 */

public class SearchResultRepository extends MemoryRepository {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
