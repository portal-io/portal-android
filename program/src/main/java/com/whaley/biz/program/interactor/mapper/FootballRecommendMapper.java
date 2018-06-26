package com.whaley.biz.program.interactor.mapper;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.LiveConstants;
import com.whaley.biz.program.constants.LiveRecommendType;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.RecommendAreasBean;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.viewmodel.BannerImgLoopViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LiveUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LiveProgramUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LineViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LiveHeaderUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LiveTopicUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * Created by dell on 2017/8/16.
 */

@Route(path = ProgramRouterPath.MAPPER_FOOTBALL_RECOMMEND)
public class FootballRecommendMapper extends UIViewModelMapper<List<RecommendAreasBean>> implements IProvider, ProgramConstants, LiveRecommendType, LiveConstants {

    private static final String STR_LINE = " | ";
    private static final String STR_PLAY_COUNT = "人播放";
    private static final String STR_FORMAT = "%.1f万";
    private static final String STR_SUBTITLE_APPEND = "人正在观看";
    private static final String STR_SUBTITLE_RESERVE_APPEND = "人已预约";

    public FootballRecommendMapper() {

    }

    public FootballRecommendMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
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
        for (RecommendAreasBean recommendAreasBean : model) {
            String typeStr = recommendAreasBean.getType();
            try {
                int type = Integer.valueOf(typeStr);
                switch (type) {
                    case TYPE_SERVER_BANNER: {
                        List<RecommendModel> recommendModels = recommendAreasBean.getRecommendElements();
                        if (recommendModels == null || recommendModels.size() <= 0) {
                            continue;
                        }
                        addBannerData(recommendAreasBean, clickableUiViewModelList);
                        break;
                    }
                    case TYPE_SERVER_HOT_LIVE:
                    case TYPE_SERVER_PROGRAM:
                        List<RecommendModel> recommendModels = recommendAreasBean.getRecommendElements();
                        if (recommendModels == null || recommendModels.size() <= 0) {
                            continue;
                        }
                        for (RecommendModel recommendModel : recommendModels) {
                            if (TYPE_TEXT.equals(recommendModel.getType())) {
                                LineViewUIViewModel lineViewUIViewModel = new LineViewUIViewModel();
                                lineViewUIViewModel.setLineHeightType(LineViewUIViewModel.TYPE_LINE_1);
                                clickableUiViewModelList.add(lineViewUIViewModel);
                                LiveHeaderUIViewModel liveHeaderUIViewModel = new LiveHeaderUIViewModel();
                                liveHeaderUIViewModel.setTitle(recommendModel.getName());
                                liveHeaderUIViewModel.convert(recommendModel);
                                clickableUiViewModelList.add(liveHeaderUIViewModel);
                                continue;
                            } else {
                                LineViewUIViewModel lineViewUIViewModel = new LineViewUIViewModel();
                                lineViewUIViewModel.setLineHeightType(LineViewUIViewModel.TYPE_LINE_1);
                                clickableUiViewModelList.add(lineViewUIViewModel);
                            }
                            if (type == TYPE_SERVER_PROGRAM) {
                                switch (recommendModel.getLinkArrangeType()) {
                                    case LINKARRANGETYPE_MANUAL_ARRANGE:
                                        LiveTopicUIViewModel liveTopicUIViewModel = new LiveTopicUIViewModel();
                                        liveTopicUIViewModel.convert(recommendModel);
                                        clickableUiViewModelList.add(liveTopicUIViewModel);
                                        break;
                                    case LINKARRANGETYPE_PROGRAM:
                                    case LINKARRANGETYPE_LIVE:
                                    case LINKARRANGETYPE_FOOTBALL_LIST:
                                    case LINKARRANGETYPE_FOOTBALL_HOMEPAGE:
                                    case LINKARRANGETYPE_FOOTBALL_RECOMMENDPAGE:
                                        LiveProgramUIViewModel liveProgramUIViewModel = new LiveProgramUIViewModel();
                                        liveProgramUIViewModel.convert(recommendModel);
                                        liveProgramUIViewModel.setSoccer(true);
                                        clickableUiViewModelList.add(liveProgramUIViewModel);
                                        break;
                                }
                                continue;
                            }
                            LiveUIViewModel liveUIViewModel = new LiveUIViewModel();
                            liveUIViewModel.convert(recommendModel);
                            liveUIViewModel.setSoccer(true);
                            clickableUiViewModelList.add(liveUIViewModel);
                        }
                        break;
                    default:
                        break;
                }
            } catch (NumberFormatException e) {
                Log.e(e, "FootballRecommendViewData convert");
            }
        }
        getRecyclerViewModel().setClickableUiViewModels(clickableUiViewModelList);
        return getRecyclerViewModel();
    }

//    private String convertReserveCountToSubTitle(RecommendModel recommendModel) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(DateUtils.foramteToYYYYMMDDHHMM(recommendModel.getLiveBeginTime()));
//        stringBuilder.append(STR_LINE);
//        stringBuilder.append(StringUtil.getCuttingCount(recommendModel.getLiveOrderCount()));
//        stringBuilder.append(STR_SUBTITLE_RESERVE_APPEND);
//        return stringBuilder.toString();
//    }
//
//    private String convertPlayCoutToSubTitle(int playCount) {
//        StringBuilder sb = new StringBuilder();
//        if (playCount > 10000) {
//            double playCountD = 1.0 * playCount / 10000;
//            String playCountStr = String.format(STR_FORMAT, playCountD);
//            sb.append(playCountStr);
//        } else {
//            sb.append(playCount);
//        }
//        sb.append(STR_SUBTITLE_APPEND);
//        return sb.toString();
//    }

    private void addBannerData(RecommendAreasBean areasBean, List<ClickableUIViewModel> clickableUiViewModelList) {
        BannerImgLoopViewUIViewModel bannerImgLoop = new BannerImgLoopViewUIViewModel();
        bannerImgLoop.setHeight(DisplayUtil.convertDIP2PX(100f));
        bannerImgLoop.setIndicator_focused(R.drawable.page_indicator_focused);
        bannerImgLoop.setIndicator_unfocused(R.drawable.page_indicator_unfocused);
        bannerImgLoop.setAlign(BannerImgLoopViewUIViewModel.CENTER_HORIZONTAL);
        bannerImgLoop.convert(areasBean);
        clickableUiViewModelList.add(bannerImgLoop);
    }

    @Override
    public void init(Context context) {

    }

}
