package com.whaley.biz.program.interactor.mapper;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.model.response.RecommendListResponse;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.CompilationUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.utils.StringUtil;
import com.whaley.core.repository.IRepositoryManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * Created by dell on 2017/8/15.
 */

@Route(path = ProgramRouterPath.MAPPER_COMPILATION)
public class CompilationMapper extends UIViewModelMapper<RecommendListResponse> implements IProvider, ProgramConstants {

    public CompilationMapper() {

    }

    public CompilationMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    protected RecyclerViewModel convert(RecommendListResponse recommendListResponse) {
        initDefaultViewModel();
        if (recommendListResponse == null && recommendListResponse.getData() == null) {
            getRecyclerViewModel().setClickableUiViewModels(null);
            return getRecyclerViewModel();
        }
        List<ClickableUIViewModel> clickableUiViewModelList = new ArrayList<>();
        if (recommendListResponse.getData().getNumber() == 0) {
            addViewModel(clickableUiViewModelList, recommendListResponse.getList());
        } else {
            clickableUiViewModelList.addAll(getRecyclerViewModel().getClickableUiViewModels());
            addViewModel(clickableUiViewModelList, recommendListResponse.getList());
        }
        getRecyclerViewModel().setClickableUiViewModels(clickableUiViewModelList);
        return getRecyclerViewModel();
    }

    private void addViewModel(List<ClickableUIViewModel> clickableUiViewModelList, List<RecommendModel> recommendModelList) {
        for (RecommendModel recommend : recommendModelList) {
            CompilationUIViewModel compilationUIViewModel = new CompilationUIViewModel();
            compilationUIViewModel.setImgUrl(recommend.getNewPicUrl());
            compilationUIViewModel.setName(recommend.getName());
            String videoCount = "";
            if (LINKARRANGETYPE_PROGRAM.equals(recommend.getLinkArrangeType())
                    || LINKARRANGETYPE_MOREMOVIEPROGRAM.equals(recommend.getLinkArrangeType())
                    || LINKARRANGETYPE_MORETVPROGRAM.equals(recommend.getLinkArrangeType())) {
                videoCount = StringUtil.getFormatTime(recommend.getProgramPlayTime());
                if (recommend.getStatQueryDto() != null) {
                    videoCount = videoCount + "  |  " + StringUtil.getCuttingCount(recommend.getStatQueryDto().getPlayCount()) + "人播放";
                }
            } else if (LINKARRANGETYPE_LIVE.equals(recommend.getLinkArrangeType())) {
                if (recommend.getStatQueryDto() != null) {
                    videoCount = StringUtil.getCuttingCount(recommend.getStatQueryDto().getPlayCount()) + "人播放";
                }
            } else if (LINKARRANGETYPE_CONTENT_PACKAGE.equals(recommend.getLinkArrangeType())) {
                videoCount = "共" + recommend.getContentPackageQueryDtos().getTotalCount() + "部视频";
            } else if (LINKARRANGETYPE_MANUAL_ARRANGE.equals(recommend.getLinkArrangeType())) {
                videoCount = "共" + recommend.getDetailCount() + "个内容";
            }
            compilationUIViewModel.setSubtitle(videoCount);
            compilationUIViewModel.convert(recommend);
            clickableUiViewModelList.add(compilationUIViewModel);
        }
    }

    @Override
    public void init(Context context) {

    }
}
