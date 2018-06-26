package com.whaley.biz.program.interactor.mapper;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LineViewUIViewModel;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewmodel.CardVideoUIViewModel;
import com.whaley.core.repository.IRepositoryManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * Created by dell on 2017/8/15.
 */

@Route(path = "/program/mapper/reserve")
public class ReserveViewModelMapper extends UIViewModelMapper<List<LiveDetailsModel>> implements IProvider {

    public ReserveViewModelMapper() {

    }

    public ReserveViewModelMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    protected RecyclerViewModel convert(List<LiveDetailsModel> obj) {
        initDefaultViewModel();
        if (obj == null) {
            getRecyclerViewModel().setClickableUiViewModels(null);
            return getRecyclerViewModel();
        }
        List<ClickableUIViewModel> clickableUiViewModelList = new ArrayList<>();
        boolean isFrist = true;
        for (LiveDetailsModel model : obj) {
            if (isFrist) {
                isFrist = false;
            } else {
                LineViewUIViewModel lineViewUIViewModel = new LineViewUIViewModel();
                lineViewUIViewModel.setLineHeightType(LineViewUIViewModel.TYPE_LINE_1);
                clickableUiViewModelList.add(lineViewUIViewModel);
            }
            CardVideoUIViewModel cardVideoUIViewModel = new CardVideoUIViewModel();
            cardVideoUIViewModel.convert(model);
            clickableUiViewModelList.add(cardVideoUIViewModel);
        }
        getRecyclerViewModel().setClickableUiViewModels(clickableUiViewModelList);
        return getRecyclerViewModel();
    }

    @Override
    public void init(Context context) {

    }
}
