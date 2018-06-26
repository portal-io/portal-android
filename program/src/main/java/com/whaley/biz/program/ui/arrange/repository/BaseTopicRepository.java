package com.whaley.biz.program.ui.arrange.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;

/**
 * Created by dell on 2017/9/4.
 */

public class BaseTopicRepository extends MemoryRepository {

    private RecyclerViewModel recyclerViewModel;

    private boolean isTopicView;

    public RecyclerViewModel getRecyclerViewModel() {
        return recyclerViewModel;
    }

    public void setRecyclerViewModel(RecyclerViewModel recyclerViewModel) {
        this.recyclerViewModel = recyclerViewModel;
    }

    public boolean isShowEmpty() {
        return getRecyclerViewModel() == null || getRecyclerViewModel().getClickableUiViewModels() == null
        || getRecyclerViewModel().getClickableUiViewModels().isEmpty();
    }

    public boolean isTopicView() {
        return isTopicView;
    }

    public void setTopicView(boolean topicView) {
        isTopicView = topicView;
    }
}
