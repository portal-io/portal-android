package com.whaley.biz.program.uiview.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;

/**
 * Created by YangZhi on 2017/8/14 20:59.
 */

public class RecyclerUIViewRepository extends MemoryRepository {

    private String getDataUseCasePath;

    private String mapperPath;

    private String id;

    private String title;

    private RecyclerViewModel recyclerViewModel;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setGetDataUseCasePath(String getDataUseCasePath) {
        this.getDataUseCasePath = getDataUseCasePath;
    }

    public String getGetDataUseCasePath() {
        return getDataUseCasePath;
    }

    public void setMapperPath(String mapperPath) {
        this.mapperPath = mapperPath;
    }

    public String getMapperPath() {
        return mapperPath;
    }

    public RecyclerViewModel getRecyclerViewModel() {
        return recyclerViewModel;
    }

    public void setRecyclerViewModel(RecyclerViewModel recyclerViewModel) {
        this.recyclerViewModel = recyclerViewModel;
    }
}
