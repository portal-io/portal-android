package com.whaley.biz.program.ui.arrange.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewmodel.TopicHeadViewModel;
import com.whaley.core.share.model.ShareParam;


/**
 * Author: qxw
 * Date:2017/8/10
 * Introduction:
 */

public class TopicRepository extends MemoryRepository implements TopicService {

    private boolean isMovable;

    private String getDataUseCasePath;

    private String mapperPath;

    private String id;

    private TopicHeadViewModel topicHead;

    private RecyclerViewModel recyclerViewModel;

    private ShareParam.Builder builder;

    private boolean isPlayerButton;

    private PageModel pageModel;

    public TopicHeadViewModel getTopicHead() {
        return topicHead;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public String getGetDataUseCasePath() {
        return getDataUseCasePath;
    }

    public void setGetDataUseCasePath(String getDataUseCasePath) {
        this.getDataUseCasePath = getDataUseCasePath;
    }

    public String getMapperPath() {
        return mapperPath;
    }

    public void setMapperPath(String mapperPath) {
        this.mapperPath = mapperPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public RecyclerViewModel getRecyclerViewModel() {
        return recyclerViewModel;
    }

    public void setRecyclerViewModel(RecyclerViewModel recyclerViewModel) {
        this.recyclerViewModel = recyclerViewModel;
    }

    @Override
    public void setTopicHead(TopicHeadViewModel topicHeadData) {
        this.topicHead = topicHeadData;
    }

    @Override
    public void setPlayerButton(boolean isPlayerButton) {
        this.isPlayerButton = isPlayerButton;
    }

    public boolean isPlayerButton() {
        return isPlayerButton;
    }

    @Override
    public void setBuilder(ShareParam.Builder builder) {
        this.builder = builder;
    }


    public ShareParam.Builder getBuilder() {
        return builder;
    }

    @Override
    public void setPageModel(PageModel pageModel) {
        this.pageModel = pageModel;
    }

    @Override
    public void setActivity(boolean isActivity) {
        isMovable = isActivity;
    }

    public PageModel getPageModel() {
        return pageModel;
    }

}
