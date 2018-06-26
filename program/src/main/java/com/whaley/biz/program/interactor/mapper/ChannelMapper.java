package com.whaley.biz.program.interactor.mapper;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.program.model.RecommendAreasBean;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.viewmodel.ChannelIconViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.repository.IRepositoryManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * Created by dell on 2017/9/14.
 */

public class ChannelMapper extends UIViewModelMapper<List<RecommendAreasBean>> implements IProvider {

    static final int TYPE_ALL_CHANNEL = 5;

    public ChannelMapper() {

    }

    public ChannelMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    protected void initDefaultViewModel() {
        super.initDefaultViewModel();
        getRecyclerViewModel().setGrid(true);
        getRecyclerViewModel().setColumnCount(4);
    }

    @Override
    public RecyclerViewModel convert(List<RecommendAreasBean> model) {
        initDefaultViewModel();
        if (model == null) {
            getRecyclerViewModel().setClickableUiViewModels(null);
            return getRecyclerViewModel();
        }
        List<ClickableUIViewModel> uiViewModelList = null;
        if (model == null || model.size() == 0) {
            return getRecyclerViewModel();
        }
        for (RecommendAreasBean recommendAreasBean : model) {
            int type = -1;
            try {
                type = Integer.valueOf(recommendAreasBean.getType());
            } catch (NumberFormatException e) {
                Log.e(e, "");
            }
            switch (type) {
                case TYPE_ALL_CHANNEL:
                    List<RecommendModel> recommendModels = recommendAreasBean.getRecommendElements();
                    if (recommendModels == null || recommendModels.size() == 0) {
                        break;
                    }
                    uiViewModelList = new ArrayList<>();
                    for (RecommendModel recommendModel : recommendModels) {
                        ChannelIconViewUIViewModel channelIconViewModel = new ChannelIconViewUIViewModel();
                        channelIconViewModel.convert(recommendModel);
                        channelIconViewModel.setSpanSize(1);
                        uiViewModelList.add(channelIconViewModel);
                    }
                    break;
            }
        }
        getRecyclerViewModel().setClickableUiViewModels(uiViewModelList);
        return getRecyclerViewModel();
    }

    @Override
    public void init(Context context) {

    }
}
