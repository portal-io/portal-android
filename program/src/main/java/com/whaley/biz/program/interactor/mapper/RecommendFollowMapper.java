package com.whaley.biz.program.interactor.mapper;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.RecommendAreasBean;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.HorziontalFullImgViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LineViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.PromulagtorUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * 推荐关注Concerter
 * Author: qxw
 * Date: 2017/3/20
 */
@Route(path = ProgramRouterPath.MAPPER_FOLLOW_RECOMMENT)
public class RecommendFollowMapper extends UIViewModelMapper<List<RecommendAreasBean>> implements IProvider, ProgramConstants {


    public RecommendFollowMapper() {
    }

    public RecommendFollowMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public RecyclerViewModel convert(List<RecommendAreasBean> model) {
        initDefaultViewModel();
        if (model == null) {
            getRecyclerViewModel().setClickableUiViewModels(null);
            return getRecyclerViewModel();
        }
        List<ClickableUIViewModel> clickableUiViewModelList = new ArrayList<>();
        for (RecommendAreasBean areasBean : model) {
            if (!StrUtil.isNumber(areasBean.getType())) {
                continue;
            }
            int type = Integer.valueOf(areasBean.getType());
            if (!isAreaBeanHasData(areasBean))
                continue;
            switch (type) {
                case TYPE_DISCOVERY_AD:
                    addADData(areasBean, clickableUiViewModelList);
                    break;
                case TYPE_DISCOVERY_PUBLISHER:
                    addPublisher(areasBean, clickableUiViewModelList);
                    break;
            }
        }
        getRecyclerViewModel().setClickableUiViewModels(clickableUiViewModelList);
        return getRecyclerViewModel();
    }

    /**
     * 发布者
     *
     * @param areasBean
     * @param clickableUiViewModelList
     * @author qxw
     * @time 2017/3/20 15:42
     */
    private void addPublisher(RecommendAreasBean areasBean, List<ClickableUIViewModel> clickableUiViewModelList) {
        for (RecommendModel recommendModel : areasBean.getRecommendElements()) {
            if (recommendModel != null && recommendModel.getCpInfo() != null && recommendModel.getCpProgramDtos() != null && recommendModel.getCpProgramDtos().size() > 0) {
                PromulagtorUIViewModel promulagtorUIViewModel = new PromulagtorUIViewModel();
                promulagtorUIViewModel.convert(recommendModel);
                clickableUiViewModelList.add(promulagtorUIViewModel);
                LineViewUIViewModel lineViewUIViewModel = new LineViewUIViewModel();
                lineViewUIViewModel.setLineHeightType(LineViewUIViewModel.TYPE_LINE_2);
                clickableUiViewModelList.add(lineViewUIViewModel);
            }
        }

    }

    /**
     * 添加广告数据
     *
     * @param areasBean
     */
    private void addADData(RecommendAreasBean areasBean, List<ClickableUIViewModel> clickableUiViewModelList) {
        RecommendModel bean = areasBean.getRecommendElements().get(0);
        HorziontalFullImgViewUIViewModel horziontalFullImgViewModel = new HorziontalFullImgViewUIViewModel();
        horziontalFullImgViewModel.convert(bean);
        horziontalFullImgViewModel.setImgHeightType(HorziontalFullImgViewUIViewModel.TYPE_AD);
        clickableUiViewModelList.add(horziontalFullImgViewModel);
    }

    @Override
    public void init(Context context) {

    }
}
