package com.whaley.biz.program.interactor.mapper;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.RecommendAreasBean;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.viewmodel.AreaHeadViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.BannerImgLoopViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.BannerPlayerSingerUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ButtonTagUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ButtonViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ChannelIconViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.HorziontalFullImgViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.LineViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ProgramUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * Created by YangZhi on 2017/8/14 19:34.
 */

@Route(path = ProgramRouterPath.MAPPER_RECOMMENT)
public class RecommendMapper extends UIViewModelMapper<List<RecommendAreasBean>> implements ProgramConstants, IProvider {

    static final int SPAN_SIZE = 60;

    public RecommendMapper() {

    }

    public RecommendMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    protected void initDefaultViewModel() {
        super.initDefaultViewModel();
        getRecyclerViewModel().setGrid(true);
        getRecyclerViewModel().setColumnCount(SPAN_SIZE);
    }

    @Override
    public RecyclerViewModel convert(List<RecommendAreasBean> datas) {
        initDefaultViewModel();
        if (datas == null) {
            getRecyclerViewModel().setClickableUiViewModels(null);
            return getRecyclerViewModel();
        }
        List<ClickableUIViewModel> clickableUiViewModelList = new ArrayList<>();
        for (RecommendAreasBean areasBean : datas) {
            if (!StrUtil.isNumber(areasBean.getType())) {
                continue;
            }
            int type = Integer.valueOf(areasBean.getType());
            if (!isAreaBeanHasData(areasBean))
                continue;
            switch (type) {
                case TYPE_DISCOVERY_BANNER:
                    addBannerData(areasBean, clickableUiViewModelList);
                    break;
                case TYPE_DISCOVERY_SINGLE_PLAY:
                    addBannerPlayData(areasBean, clickableUiViewModelList);
                    break;
                case TYPE_DISCOVERY_AD:
                    addADData(areasBean, clickableUiViewModelList);
                    break;
                case TYPE_DISCOVERY_HOT:
                    addHotData(areasBean, clickableUiViewModelList);
                    break;
                case TYPE_DISCOVERY_TITLE:
                    addTitleData(areasBean, clickableUiViewModelList);
                    break;
                case TYPE_DISCOVERY_NORMAL:
                case TYPE_DISCOVERY_BRAND:
                case TYPE_DISCOVERY_SOCIAL:
                case TYPE_DISCOVERY_TVPLAY:
                case TYPE_DISCOVERY_MOVIE:
                    addData(areasBean, clickableUiViewModelList, type);
                    break;
            }
        }
        getRecyclerViewModel().setClickableUiViewModels(clickableUiViewModelList);
        return getRecyclerViewModel();
    }

    /***
     *
     * @param areasBean
     * @param clickableUiViewModelList
     */
    private void addDataMovie(RecommendAreasBean areasBean, List<ClickableUIViewModel> clickableUiViewModelList) {
    }

    /**
     * 单个视频播放
     *
     * @param areasBean
     * @param clickableUiViewModelList
     * @author qxw
     * @time 2017/3/20 15:28
     */
    private void addBannerPlayData(RecommendAreasBean areasBean, List<ClickableUIViewModel> clickableUiViewModelList) {
        RecommendModel bean = areasBean.getRecommendElements().get(0);
        BannerPlayerSingerUIViewModel bannerPlayerUIModel = new BannerPlayerSingerUIViewModel();
        bannerPlayerUIModel.convert(bean);
        bannerPlayerUIModel.setSpanSize(SPAN_SIZE);
        clickableUiViewModelList.add(bannerPlayerUIModel);
    }


    /**
     * 添加标题数据
     *
     * @param areasBean
     */
    private void addTitleData(RecommendAreasBean areasBean, List<ClickableUIViewModel> clickableUiViewModelList) {
        int j = 4;
        if (areasBean.getRecommendElements().size() < 4) {
            j = areasBean.getRecommendElements().size();
        }
        for (int n = 0; n < j; n++) {
            RecommendModel bean = areasBean.getRecommendElements().get(n);
            ButtonTagUIViewModel buttonTagUIViewModel = new ButtonTagUIViewModel();
            buttonTagUIViewModel.convert(bean);
            if (n == 0) {
                buttonTagUIViewModel.setOutLeft(DisplayUtil.convertDIP2PX(15));
            } else if (n == 1) {
                buttonTagUIViewModel.setOutLeft(DisplayUtil.convertDIP2PX(10));
                buttonTagUIViewModel.setOutRight(DisplayUtil.convertDIP2PX(5));
            } else if (n == 2) {
                buttonTagUIViewModel.setOutLeft(DisplayUtil.convertDIP2PX(5));
                buttonTagUIViewModel.setOutRight(DisplayUtil.convertDIP2PX(10));
            } else {
                buttonTagUIViewModel.setOutRight(DisplayUtil.convertDIP2PX(15));
            }
            buttonTagUIViewModel.setSpanSize(SPAN_SIZE / j);
            clickableUiViewModelList.add(buttonTagUIViewModel);
        }
    }

    /**
     * @param areasBean
     * @param clickableUiViewModelList
     * @author qxw
     * @time 2017/3/17 13:47
     */
    private void addData(RecommendAreasBean areasBean, List<ClickableUIViewModel> clickableUiViewModelList, int type) {
        List<ClickableUIViewModel> remainDatas = new ArrayList<>();
        int itemSize = 0;
        List<PlayData> playModels = new ArrayList<>();
        for (int m = 0; m < areasBean.getRecommendElements().size(); m++) {
            RecommendModel bean = areasBean.getRecommendElements().get(m);
            if (isAddTitle(clickableUiViewModelList, areasBean, bean, m)) {
                if (m == (areasBean.getRecommendElements().size() - 1)) {
                    addButtonData(clickableUiViewModelList, areasBean, remainDatas, bean);
                }
                continue;
            }
            int left = 0;
            int right = 0;
            if (itemSize % 2 == 0) {
                right = 1;
            } else {
                left = 1;
            }
            ProgramUIViewModel programViewModel = addOtherTypeData(areasBean, bean, type, left, right, m);
            remainDatas.add(programViewModel);
            if (itemSize < 4) {
                clickableUiViewModelList.add(programViewModel);
            }
            PlayData playModel = bean.getPlayData();
            if (playModel != null) {
                playModels.add(playModel);
            }
            itemSize++;
            if (m == (areasBean.getRecommendElements().size() - 1) && areasBean.getRecommendElements().size() > 8) {
                RecommendModel beanButton = new RecommendModel();
                beanButton.setRecommendAreaType(areasBean.getType());
                addButtonData(clickableUiViewModelList, areasBean, remainDatas, beanButton);
            }
        }
    }


    /**
     * 添加其他Type类型数据（节目Item）
     *
     * @param areasBean
     * @param bean
     * @param type
     */
    private ProgramUIViewModel addOtherTypeData(RecommendAreasBean areasBean, RecommendModel bean, int type, int left, int right, int pos) {
        ProgramUIViewModel programViewModel = new ProgramUIViewModel();
        programViewModel.convert(bean);
        programViewModel.setSpanSize(SPAN_SIZE / 2);
        programViewModel.setOutLeft(left);
        programViewModel.setOutRight(right);
        programViewModel.setPos(pos);
        return programViewModel;
    }

    /**
     * 检查是不是有头部Title
     *
     * @param areasBean
     * @param bean
     * @param m
     * @return
     * @author qxw
     * @time 2017/3/17 13:52
     */
    private boolean isAddTitle(List<ClickableUIViewModel> clickableUiViewModelList, RecommendAreasBean areasBean, RecommendModel bean, int m) {
        int titleType = Integer.valueOf(bean.getType());
        if (titleType == TYPE_IS_TITLE) {
            if (m == 0) {
                LineViewUIViewModel lineViewModel = new LineViewUIViewModel();
                lineViewModel.setSpanSize(SPAN_SIZE);
                if (clickableUiViewModelList.size() > 0) {
                    lineViewModel.setLineHeightType(LineViewUIViewModel.TYPE_LINE_2);
                } else {
                    lineViewModel.setLineHeight(LineViewUIViewModel.TYPE_LINE_COLOR_1);
                }
                clickableUiViewModelList.add(lineViewModel);
                bean.setRecommendAreaType(areasBean.getType());
                AreaHeadViewUIViewModel areaHeadViewModel = new AreaHeadViewUIViewModel();
                areaHeadViewModel.convert(bean);
                areaHeadViewModel.setSpanSize(SPAN_SIZE);
                clickableUiViewModelList.add(areaHeadViewModel);
            }
            return true;
        }
        return false;
    }

    /**
     * 添加Button按钮数据
     *
     * @param areasBean
     * @param remainDatas
     * @param bean
     */
    private void addButtonData(List<ClickableUIViewModel> clickableUiViewModelList, RecommendAreasBean areasBean, List<ClickableUIViewModel> remainDatas, RecommendModel bean) {
        boolean isEnter = false;
        boolean isBatch = false;
        if (!StrUtil.isEmpty(bean.getLinkArrangeValue())) {
            isEnter = true;
        }
        if (remainDatas.size() >= 8) {
            isBatch = true;
        }
        if (isEnter && isBatch) {
            ButtonViewUIViewModel buttonViewModel1 = new ButtonViewUIViewModel();
            bean.setRecommendAreaType(areasBean.getType());
            buttonViewModel1.convert(bean);
            buttonViewModel1.setButtonType(ButtonViewUIViewModel.TYPE_ENTER);
            buttonViewModel1.setSpanSize(SPAN_SIZE / 2);
            ButtonViewUIViewModel buttonViewModel2 = new ButtonViewUIViewModel();
            ButtonViewUIViewModel.BatchViewModel batchViewModel = new ButtonViewUIViewModel.BatchViewModel();
            batchViewModel.differential = 1;
            batchViewModel.clickableUiViewModels = remainDatas;
            buttonViewModel2.convert(batchViewModel);
            buttonViewModel2.setButtonType(ButtonViewUIViewModel.TYPE_BATCH);
            buttonViewModel2.setSpanSize(SPAN_SIZE / 2);
            clickableUiViewModelList.add(buttonViewModel1);
            clickableUiViewModelList.add(buttonViewModel2);
            return;
        }
        if (isEnter) {
            ButtonViewUIViewModel buttonViewModel1 = new ButtonViewUIViewModel();
            bean.setRecommendAreaType(areasBean.getType());
            buttonViewModel1.convert(bean);
            buttonViewModel1.setButtonType(ButtonViewUIViewModel.TYPE_ENTER);
            buttonViewModel1.setSpanSize(SPAN_SIZE);
            clickableUiViewModelList.add(buttonViewModel1);
            return;
        }
        if (isBatch) {
            ButtonViewUIViewModel buttonViewModel2 = new ButtonViewUIViewModel();
            buttonViewModel2.convert(remainDatas);
            buttonViewModel2.setButtonType(ButtonViewUIViewModel.TYPE_BATCH);
            buttonViewModel2.setSpanSize(SPAN_SIZE);
            ButtonViewUIViewModel.BatchViewModel batchViewModel = new ButtonViewUIViewModel.BatchViewModel();
            batchViewModel.clickableUiViewModels = remainDatas;
            buttonViewModel2.convert(batchViewModel);
            clickableUiViewModelList.add(buttonViewModel2);
            return;
        }
    }

    /**
     * 添加热门数据
     *
     * @param areasBean
     */
    private void addHotData(RecommendAreasBean areasBean, List<ClickableUIViewModel> clickableUiViewModelList) {
        int j = 5;
        if (areasBean.getRecommendElements().size() < 5) {
            j = areasBean.getRecommendElements().size();
        }
        for (int i = 0; i < j; i++) {
            RecommendModel recommendModel = areasBean.getRecommendElements().get(i);
            final ChannelIconViewUIViewModel channelIconViewModel = new ChannelIconViewUIViewModel();
            channelIconViewModel.convert(recommendModel);
            channelIconViewModel.setSpanSize(SPAN_SIZE / 5);
            clickableUiViewModelList.add(channelIconViewModel);
            Router.getInstance().buildExecutor("/launch/service/getfestival")
                    .notTransCallbackData()
                    .notTransParam()
                    .callback(new Executor.Callback<Boolean>() {
                        @Override
                        public void onCall(Boolean aBoolean) {
                            channelIconViewModel.setActivity(aBoolean);
                        }

                        @Override
                        public void onFail(Executor.ExecutionError executionError) {

                        }
                    })
                    .excute();
        }
    }

    /**
     * 添加banner
     *
     * @param areasBean
     * @param clickableUiViewModelList
     */
    private void addBannerData(RecommendAreasBean areasBean, List<ClickableUIViewModel> clickableUiViewModelList) {
        final BannerImgLoopViewUIViewModel bannerImgLoop = new BannerImgLoopViewUIViewModel();
        bannerImgLoop.setHeight(DisplayUtil.convertDIP2PX(187));
        bannerImgLoop.convert(areasBean);
        bannerImgLoop.setSpanSize(SPAN_SIZE);
        clickableUiViewModelList.add(bannerImgLoop);
//        Router.getInstance().buildExecutor("/launch/service/getfestival")
//                .notTransCallbackData()
//                .notTransParam()
//                .callback(new Executor.Callback<Boolean>() {
//                    @Override
//                    public void onCall(Boolean aBoolean) {
//                        bannerImgLoop.setActivity(aBoolean);
//                    }
//
//                    @Override
//                    public void onFail(Executor.ExecutionError executionError) {
//
//                    }
//                })
//                .excute();

    }


    /**
     * 添加广告数据
     *
     * @param areasBean
     */
    private void addADData(RecommendAreasBean areasBean, List<ClickableUIViewModel> clickableUiViewModelList) {
        for (int n = 0; n < areasBean.getRecommendElements().size(); n++) {
            if (clickableUiViewModelList.size() > 0) {
                LineViewUIViewModel lineViewModel = new LineViewUIViewModel();
                lineViewModel.setSpanSize(SPAN_SIZE);
                lineViewModel.setLineHeightType(LineViewUIViewModel.TYPE_LINE_2);
                clickableUiViewModelList.add(lineViewModel);
            }
            RecommendModel bean = areasBean.getRecommendElements().get(n);
            HorziontalFullImgViewUIViewModel horziontalFullImgViewModel = new HorziontalFullImgViewUIViewModel();
            horziontalFullImgViewModel.convert(bean);
            horziontalFullImgViewModel.setSpanSize(SPAN_SIZE);
            horziontalFullImgViewModel.setImgHeightType(HorziontalFullImgViewUIViewModel.TYPE_AD);
            clickableUiViewModelList.add(horziontalFullImgViewModel);
        }
    }


    @Override
    public void init(Context context) {

    }
}
