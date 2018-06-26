package com.whaley.biz.program.interactor.mapper;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.CpProgramListModel;
import com.whaley.biz.program.model.CpProgramModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewmodel.CardVideoUIViewModel;
import com.whaley.core.repository.IRepositoryManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/8/16
 * Introduction:
 */
@Route(path = ProgramRouterPath.MAPPER_CP_PROGRAM_LIST)
public class CpProgramMapper extends UIViewModelMapper<CpProgramListModel> implements IProvider {


    public CpProgramMapper() {
    }

    public CpProgramMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public void init(Context context) {

    }

    @Override
    protected RecyclerViewModel convert(CpProgramListModel model) {
        initDefaultViewModel();
        if (model == null) {
            getRecyclerViewModel().setClickableUiViewModels(null);
            return getRecyclerViewModel();
        }
        List<ClickableUIViewModel> clickableUiViewModelList = new ArrayList<>();
        if (model.getPageNumber() == 0) {
            addViewModelList(model.getPrograms(), clickableUiViewModelList);
        } else {
            clickableUiViewModelList.addAll(getRecyclerViewModel().getClickableUiViewModels());
            addViewModelList(model.getPrograms(), clickableUiViewModelList);
        }
        getRecyclerViewModel().setClickableUiViewModels(clickableUiViewModelList);
        return getRecyclerViewModel();
    }

    private void addViewModelList(List<CpProgramModel> programs, List<ClickableUIViewModel> clickableUiViewModelList) {
        for (CpProgramModel model : programs) {
            CardVideoUIViewModel viewModel = new CardVideoUIViewModel();
            viewModel.convert(model);
            clickableUiViewModelList.add(viewModel);
        }
    }
}
