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
import com.whaley.biz.program.uiview.viewmodel.BannerGalleryUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.BannerImgLoopViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.BannerItemUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LineViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LiveHeaderUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LiveProgramUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LiveTopicUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LiveUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * Created by dell on 2017/8/17.
 */

@Route(path = ProgramRouterPath.MAPPER_LIVE_RECOMMEND)
public class LiveRecommendMapper extends UIViewModelMapper<List<RecommendAreasBean>> implements IProvider, ProgramConstants, LiveRecommendType, LiveConstants {

    private static final String STR_LINE = " | ";
    private static final String STR_PLAY_COUNT = "人播放";
    private static final String STR_FORMAT = "%.1f万";
    private static final String STR_SUBTITLE_APPEND = "人正在观看";
    private static final String STR_SUBTITLE_RESERVE_APPEND = "人已预约";

    public LiveRecommendMapper() {

    }

    public LiveRecommendMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
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
                        BannerGalleryUIViewModel bannerGalleryUIViewModel = new BannerGalleryUIViewModel();
                        bannerGalleryUIViewModel.convert(recommendAreasBean);
                        List<BannerItemUIViewModel> bannerItemUIViewModels = new ArrayList<>();
                        List<RecommendModel> recommendModels = recommendAreasBean.getRecommendElements();
                        if (recommendModels != null && !recommendModels.isEmpty()) {
                            for (RecommendModel recommendModel : recommendModels) {
                                if (recommendModel == null)
                                    continue;
                                BannerItemUIViewModel bannerItemViewData = new BannerItemUIViewModel();
                                bannerItemViewData.convert(recommendModel);
                                bannerItemUIViewModels.add(bannerItemViewData);
                            }
                            bannerGalleryUIViewModel.setBannerItemUIViewModels(bannerItemUIViewModels);
                            clickableUiViewModelList.add(bannerGalleryUIViewModel);
                        }
                        break;
                    }
                    case TYPE_SERVER_HOT_LIVE:
                    case TYPE_SERVER_PROGRAM:
                        List<RecommendModel> recommendModels = recommendAreasBean.getRecommendElements();
                        int i = 0;
                        if (recommendModels == null || recommendModels.size() <= 1) {
                            continue;
                        }
                        for (RecommendModel recommendModel : recommendModels) {
                            if (i == 0) {
                                LineViewUIViewModel lineViewUIViewModel = new LineViewUIViewModel();
                                lineViewUIViewModel.setLineHeightType(LineViewUIViewModel.TYPE_LINE_1);
                                clickableUiViewModelList.add(lineViewUIViewModel);
                                LiveHeaderUIViewModel liveHeaderUIViewModel = new LiveHeaderUIViewModel();
                                liveHeaderUIViewModel.setTitle(recommendModel.getName());
                                liveHeaderUIViewModel.convert(recommendModel);
                                clickableUiViewModelList.add(liveHeaderUIViewModel);
                                i++;
                                continue;
                            }
                            if (i > 1) {
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
                                        LiveProgramUIViewModel liveProgramUIViewModel = new LiveProgramUIViewModel();
                                        liveProgramUIViewModel.convert(recommendModel);
                                        clickableUiViewModelList.add(liveProgramUIViewModel);
                                        break;
                                }
                                i++;
                                continue;
                            }
                            LiveUIViewModel liveUIViewModel = new LiveUIViewModel();
                            liveUIViewModel.convert(recommendModel);
                            clickableUiViewModelList.add(liveUIViewModel);
                            i++;
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
