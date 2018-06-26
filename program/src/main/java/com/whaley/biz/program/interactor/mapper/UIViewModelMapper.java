package com.whaley.biz.program.interactor.mapper;

import com.whaley.biz.common.interactor.Mapper;
import com.whaley.biz.program.model.RecommendAreasBean;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.core.repository.IRepositoryManager;

import java.util.List;

import io.reactivex.Scheduler;

/**
 * Created by YangZhi on 2017/8/14 19:28.
 */

public abstract class UIViewModelMapper<T> extends Mapper<RecyclerViewModel,T>{

    private RecyclerViewModel recyclerViewModel;

    public UIViewModelMapper(){

    }

    public UIViewModelMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    protected void initDefaultViewModel() {
        if (recyclerViewModel == null)
            recyclerViewModel = new RecyclerViewModel();
    }


    public RecyclerViewModel getRecyclerViewModel() {
        return recyclerViewModel;
    }

    public void setRecyclerViewModel(RecyclerViewModel recyclerViewModel) {
        this.recyclerViewModel = recyclerViewModel;
    }

    /**
     * 判断该推荐位是否有数据
     *
     * @param areasBean
     * @return
     */
    protected boolean isAreaBeanHasData(RecommendAreasBean areasBean) {
        return areasBean.getRecommendElements() != null && areasBean.getRecommendElements().size() > 0 && areasBean.getRecommendElements().get(0) != null;
    }
}
