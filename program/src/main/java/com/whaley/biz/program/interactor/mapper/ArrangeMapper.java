package com.whaley.biz.program.interactor.mapper;


import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.common.model.base.ListModel;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.ArrangeModel;
import com.whaley.biz.program.uiview.viewmodel.ProgramUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/8/17
 * Introduction:
 */
@Route(path = ProgramRouterPath.MAPPER_ARRANGE)
public class ArrangeMapper extends UIViewModelMapper<ListModel<ArrangeModel>> implements IProvider {

    private int size;

    public ArrangeMapper() {
    }

    public ArrangeMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    protected void initDefaultViewModel() {
        super.initDefaultViewModel();
        getRecyclerViewModel().setGrid(true);
        getRecyclerViewModel().setColumnCount(size);
    }

    @Override
    protected RecyclerViewModel convert(ListModel<ArrangeModel> modelListModel) {
        setSize(modelListModel);
        initDefaultViewModel();
        if (modelListModel == null) {
            getRecyclerViewModel().setClickableUiViewModels(null);
            return getRecyclerViewModel();
        }
        List<ClickableUIViewModel> clickableUiViewModelList = new ArrayList<>();
        if (modelListModel.getNumber() == 0) {
            addViewModelList(modelListModel.getContent(), clickableUiViewModelList);
        } else {
            clickableUiViewModelList.addAll(getRecyclerViewModel().getClickableUiViewModels());
            addViewModelList(modelListModel.getContent(), clickableUiViewModelList);
        }
        getRecyclerViewModel().setClickableUiViewModels(clickableUiViewModelList);
        return getRecyclerViewModel();
    }

    private void setSize(ListModel<ArrangeModel> modelListModel) {
        if (modelListModel != null && modelListModel.getContent() != null && modelListModel.getContent().size() > 0) {
            ArrangeModel model = modelListModel.getContent().get(0);
            if (ProgramConstants.VIDEO_TYPE_VR.equals(model.getVideoType())) {
                size = 2;
            } else {
                size = 3;
            }
        }
    }

    private void addViewModelList(List<ArrangeModel> content, List<ClickableUIViewModel> clickableUiViewModelList) {
        if (size == 3) {
            int i = clickableUiViewModelList.size();
            for (ArrangeModel model : content) {
                int left = 0;
                int right = 0;
                if (i % 3 == 0) {
                    right = 1;
                } else if (i % 3 == 2) {
                    left = 1;
                }
                ProgramUIViewModel viewModel = new ProgramUIViewModel();
                viewModel.setSpanSize(1);
                viewModel.setCustomizeHeight(true);
                viewModel.setImgHeight(DisplayUtil.convertDIP2PX(160));
                viewModel.convert(model);
                viewModel.setOutLeft(left);
                viewModel.setOutRight(right);
                clickableUiViewModelList.add(viewModel);
                i++;
            }
        } else {
            int i = clickableUiViewModelList.size();
            for (ArrangeModel model : content) {
                ProgramUIViewModel viewModel = new ProgramUIViewModel();
                viewModel.convert(model);
                viewModel.setSpanSize(1);
                int left = 0;
                int right = 0;
                if (i % 2 == 0) {
                    right = 1;
                } else {
                    left = 1;
                }
                viewModel.setOutLeft(left);
                viewModel.setOutRight(right);
                clickableUiViewModelList.add(viewModel);
                i++;
            }
        }
    }

    @Override
    public void init(Context context) {

    }
}
