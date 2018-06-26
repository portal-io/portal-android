package com.whaley.biz.program.interactor.mapper;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.CpFollowInfoModel;
import com.whaley.biz.program.model.CpInfoModel;
import com.whaley.biz.program.model.FollowMyModel;
import com.whaley.biz.program.uiview.viewmodel.ButtonViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ExpandMoreViewModel;
import com.whaley.biz.program.uiview.viewmodel.GraphicIconHeadUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.GraphicLeftAndRightUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.IconRoundUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LineViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/8/14
 * Introduction:
 */
@Route(path = ProgramRouterPath.MAPPER_FOLLOW_MY)
public class FollowMyMapper extends UIViewModelMapper<FollowMyModel> implements IProvider {


    public FollowMyMapper() {

    }

    public FollowMyMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    protected RecyclerViewModel convert(FollowMyModel model) {
        initDefaultViewModel();
        if (model == null) {
            getRecyclerViewModel().setClickableUiViewModels(null);
            return getRecyclerViewModel();
        }
        List<ClickableUIViewModel> clickableUiViewModelList = new ArrayList<>();
        if (model.getPageNumber() == 0) {
            setRecyclerViewModel(model.getCpList(), clickableUiViewModelList);
            setCpProgramModel(model.getLatestUpdated(), clickableUiViewModelList);
        } else {
            clickableUiViewModelList.addAll(getRecyclerViewModel().getClickableUiViewModels());
            setCpProgramModel(model.getLatestUpdated(), clickableUiViewModelList);
        }
        getRecyclerViewModel().setClickableUiViewModels(clickableUiViewModelList);
        return getRecyclerViewModel();
    }

    private void setCpProgramModel(List<CpFollowInfoModel> latestUpdated, List<ClickableUIViewModel> clickableUiViewModelList) {
        String previousCP = "";
        if (latestUpdated != null && latestUpdated.size() > 0) {
            int pos = 0;
            List<ClickableUIViewModel> graphicLeftAndRightClickableUIViewModels = new ArrayList<>();
            for (CpFollowInfoModel cpProgramModel : latestUpdated) {
                if (!cpProgramModel.getCpCode().equals(previousCP)) {
                    setButtonViewUIViewModel(graphicLeftAndRightClickableUIViewModels, clickableUiViewModelList);
                    if (graphicLeftAndRightClickableUIViewModels.size() > 0)
                        graphicLeftAndRightClickableUIViewModels = new ArrayList<>();
                    pos = 0;
                    LineViewUIViewModel lineViewUIViewModel = new LineViewUIViewModel();
                    lineViewUIViewModel.setLineHeightType(LineViewUIViewModel.TYPE_LINE_1);
                    clickableUiViewModelList.add(lineViewUIViewModel);
                    GraphicIconHeadUIViewModel graphicIconHeadUIViewModel = new GraphicIconHeadUIViewModel();
                    graphicIconHeadUIViewModel.convert(cpProgramModel);
                    clickableUiViewModelList.add(graphicIconHeadUIViewModel);
                    previousCP = cpProgramModel.getCpCode();
                }
                if (pos < 2) {
                    GraphicLeftAndRightUIViewModel graphicLeftAndRightUIViewModel = new GraphicLeftAndRightUIViewModel();
                    graphicLeftAndRightUIViewModel.convert(cpProgramModel);
                    clickableUiViewModelList.add(graphicLeftAndRightUIViewModel);
                } else {
                    GraphicLeftAndRightUIViewModel graphicLeftAndRightUIViewModel = new GraphicLeftAndRightUIViewModel();
                    graphicLeftAndRightUIViewModel.convert(cpProgramModel);
                    graphicLeftAndRightClickableUIViewModels.add(graphicLeftAndRightUIViewModel);
                    if (pos > 9) {
                        previousCP = "";
                    }
                }
                pos++;
            }
            setButtonViewUIViewModel(graphicLeftAndRightClickableUIViewModels, clickableUiViewModelList);
        }
    }

    private void setButtonViewUIViewModel(List<ClickableUIViewModel> graphicLeftAndRightClickableUIViewModels, List<ClickableUIViewModel> clickableUiViewModelList) {
        if (graphicLeftAndRightClickableUIViewModels.size() > 0) {
            ButtonViewUIViewModel buttonViewUIViewModel = new ButtonViewUIViewModel();
            ExpandMoreViewModel expandMoreViewModel = new ExpandMoreViewModel();
            expandMoreViewModel.clickableUiViewModels = graphicLeftAndRightClickableUIViewModels;
            expandMoreViewModel.size = graphicLeftAndRightClickableUIViewModels.size();
            buttonViewUIViewModel.convert(expandMoreViewModel);
            buttonViewUIViewModel.setButtonType(ButtonViewUIViewModel.TYPE_EXPAND);
            clickableUiViewModelList.add(buttonViewUIViewModel);
        }
    }

    private void setRecyclerViewModel(List<CpInfoModel> cpList, List<ClickableUIViewModel> clickableUiViewModelList) {
        if (cpList != null && cpList.size() > 0) {
            RecyclerViewModel recyclerViewModel = new RecyclerViewModel();
            recyclerViewModel.setHorizontal(true);
            recyclerViewModel.setHight(DisplayUtil.convertDIP2PX(100));
            List<ClickableUIViewModel> clickableUiViewModels = new ArrayList<>();
            for (CpInfoModel cpInfo : cpList) {
                IconRoundUIViewModel channelIconRound = new IconRoundUIViewModel();
                channelIconRound.convert(cpInfo);
                clickableUiViewModels.add(channelIconRound);
            }
            recyclerViewModel.setClickableUiViewModels(clickableUiViewModels);
            clickableUiViewModelList.add(recyclerViewModel);
        }
    }

    @Override
    public void init(Context context) {

    }
}
