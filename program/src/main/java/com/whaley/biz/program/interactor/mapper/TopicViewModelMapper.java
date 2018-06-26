package com.whaley.biz.program.interactor.mapper;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.TopicListModel;
import com.whaley.biz.program.model.TopicModel;
import com.whaley.biz.program.playersupport.component.normalplayer.springfestival.SpringFestivalComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.springfestivalresult.SpringFestivalResultComponent;
import com.whaley.biz.program.ui.arrange.repository.TopicService;
import com.whaley.biz.program.uiview.viewmodel.CardTopicUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewmodel.ShareBottomUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.TopicHeadViewModel;
import com.whaley.biz.program.uiview.viewmodel.CardVideoUIViewModel;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.model.ShareParam;
import com.whaley.core.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/8/10
 * Introduction:
 */

@Route(path = ProgramRouterPath.MAPPER_TOPIC)
public class TopicViewModelMapper extends UIViewModelMapper<Object> implements IProvider {

    public TopicViewModelMapper() {
    }

    public TopicViewModelMapper(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    protected RecyclerViewModel convert(Object model) {
        initDefaultViewModel();
        if (model == null) {
            getRecyclerViewModel().setClickableUiViewModels(null);
            return getRecyclerViewModel();
        }
        if (model instanceof TopicListModel) {
            final TopicListModel data = (TopicListModel) model;
            if (!StrUtil.isEmpty(data.getBizType()) && data.getBizType().equals("ACTIVITY")) {
                Router.getInstance().buildExecutor("/launch/service/getfestival")
                        .notTransCallbackData()
                        .notTransParam()
                        .callback(new Executor.Callback<Boolean>() {
                            @Override
                            public void onCall(Boolean aBoolean) {
                                if(aBoolean){
                                    siteActivity(data);
                                }else {
                                    siteTopic(data);
                                }
                            }

                            @Override
                            public void onFail(Executor.ExecutionError executionError) {
                                siteTopic(data);
                            }
                        })
                        .excute();
            } else {
                siteTopic(data);
            }
        }
        return getRecyclerViewModel();
    }

    /**
     * 普通专题ui模型
     *
     * @param data
     */
    private void siteTopic(TopicListModel data) {
        List<ClickableUIViewModel> clickableUiViewModelList = new ArrayList<>();
        TopicHeadViewModel topicHeadData = getHeadModel(data);
        boolean isOnlyTopic = false;
        List<TopicModel> topicDetailBeanList = data.getArrangeTreeElements();
        if (topicDetailBeanList != null && topicDetailBeanList.size() > 0) {
            clickableUiViewModelList.add(topicHeadData);
            int numVideo = 0;
            int numTopic = 0;
            List<PlayData> list = new ArrayList<>();
            for (TopicModel topicModel : topicDetailBeanList) {
                List<PlayData> oneList = new ArrayList<>();
                if (ProgramConstants.TYPE_ARRANGE.equals(topicModel.getProgramType())) {
                    CardTopicUIViewModel cardTopicUIViewModel = new CardTopicUIViewModel();
                    cardTopicUIViewModel.convert(topicModel);
                    clickableUiViewModelList.add(cardTopicUIViewModel);
                    numTopic++;
                } else if (ProgramConstants.TYPE_DYNAMIC.equals(topicModel.getProgramType())) {
                    CardVideoUIViewModel cardVideoUIViewModel = new CardVideoUIViewModel();
                    cardVideoUIViewModel.convert(topicModel);
                    cardVideoUIViewModel.setCornerResureId(R.mipmap.corner_drama);
                    clickableUiViewModelList.add(cardVideoUIViewModel);
                    oneList.add(topicModel.getPlayData());
                    cardVideoUIViewModel.setPageModel(FormatPageModel.getPayerListPagModel(oneList, 0, true));
                    numVideo++;
                } else if (ProgramConstants.TYPE_RECORDED.equals(topicModel.getProgramType())
                        || ProgramConstants.VIDEO_TYPE_MORETV_2D.equals(topicModel.getProgramType())
                        || ProgramConstants.VIDEO_TYPE_MORETV_TV.equals(topicModel.getProgramType())) {
                    list.add(topicModel.getPlayData());
                    CardVideoUIViewModel cardVideoUIViewModel = new CardVideoUIViewModel();
                    cardVideoUIViewModel.convert(topicModel);
                    clickableUiViewModelList.add(cardVideoUIViewModel);
                    oneList.add(topicModel.getPlayData());
                    cardVideoUIViewModel.setPageModel(FormatPageModel.getPayerListPagModel(oneList, 0));
                    numVideo++;
                } else {
                    continue;
                }
            }
            String numVideoString = "";
            if (numVideo > 0 && numTopic > 0) {
                numVideoString = numVideo + "个视频 | " + numTopic + "个子专题";
            }
            if (numVideo > 0 && numTopic == 0) {
                numVideoString = numVideo + "个视频";
            }
            if (numVideo == 0 && numTopic > 0) {
                numVideoString = numTopic + "个子专题";
                isOnlyTopic = true;
            }
            topicHeadData.setNumVideo(numVideoString);
            ShareBottomUIViewModel shareBottomUIViewModel = new ShareBottomUIViewModel();
            clickableUiViewModelList.add(shareBottomUIViewModel);
            getRecyclerViewModel().setClickableUiViewModels(clickableUiViewModelList);
            final TopicService topicRepository = getRepositoryManager().obtainMemoryService(TopicService.class);
            topicRepository.setTopicHead(topicHeadData);
            if (!isOnlyTopic && list != null && list.size() > 0) {
                topicRepository.setPlayerButton(true);
                topicRepository.setPageModel(FormatPageModel.getPayerListPagModel(list, 0, true, false));
            }
            ShareModel shareModel = ShareModel.createBuilder()
                    .setCode(topicHeadData.getCode())
                    .setDes(topicHeadData.getIntroduction())
                    .setTitle(topicHeadData.getName())
                    .setImgUrl(topicHeadData.getBigImageUrl())
                    .setShareType(ShareTypeConstants.TYPE_SHARE_TOPIC)
                    .setType(ShareConstants.TYPE_ALL)
                    .build();
            setShareModel(shareModel, topicRepository);
        }
    }

    private void siteActivity(TopicListModel data) {
        final TopicService topicRepository = getRepositoryManager().obtainMemoryService(TopicService.class);
        topicRepository.setActivity(true);
        List<ClickableUIViewModel> clickableUiViewModelList = new ArrayList<>();
        TopicHeadViewModel topicHeadData = getHeadModel(data);
        topicHeadData.setActivity(true);
        List<TopicModel> topicDetailBeanList = data.getArrangeTreeElements();
        if (topicDetailBeanList != null && topicDetailBeanList.size() > 0) {
            clickableUiViewModelList.add(topicHeadData);
            for (TopicModel topicModel : topicDetailBeanList) {
                List<PlayData> oneList = new ArrayList<>();
                if (ProgramConstants.TYPE_ARRANGE.equals(topicModel.getProgramType())) {
                    CardTopicUIViewModel cardTopicUIViewModel = new CardTopicUIViewModel();
                    cardTopicUIViewModel.convert(topicModel);
                    cardTopicUIViewModel.setBgColorType(CardTopicUIViewModel.TYPE_LINE_COLOR_1);
                    clickableUiViewModelList.add(cardTopicUIViewModel);
                } else if (ProgramConstants.TYPE_DYNAMIC.equals(topicModel.getProgramType())) {
                    CardVideoUIViewModel cardVideoUIViewModel = new CardVideoUIViewModel();
                    cardVideoUIViewModel.convert(topicModel, true);
                    cardVideoUIViewModel.setActivity();
                    cardVideoUIViewModel.setCornerResureId(R.mipmap.corner_drama);
                    clickableUiViewModelList.add(cardVideoUIViewModel);
                    oneList.add(topicModel.getPlayData());
                    cardVideoUIViewModel.setPageModel(FormatPageModel.getPayerListPagModel(oneList, 0, true));
                } else if (ProgramConstants.TYPE_RECORDED.equals(topicModel.getProgramType())
                        || ProgramConstants.VIDEO_TYPE_MORETV_2D.equals(topicModel.getProgramType())
                        || ProgramConstants.VIDEO_TYPE_MORETV_TV.equals(topicModel.getProgramType())) {
                    CardVideoUIViewModel cardVideoUIViewModel = new CardVideoUIViewModel();
                    cardVideoUIViewModel.convert(topicModel, true);
                    cardVideoUIViewModel.setActivity();
                    clickableUiViewModelList.add(cardVideoUIViewModel);
                    oneList.add(topicModel.getPlayData());
                    cardVideoUIViewModel.setPageModel(FormatPageModel.getPayerListPagModel(oneList, 0));
                } else {
                    continue;
                }
            }
            ShareBottomUIViewModel shareBottomUIViewModel = new ShareBottomUIViewModel();
            shareBottomUIViewModel.setActivity(true);
            clickableUiViewModelList.add(shareBottomUIViewModel);
            getRecyclerViewModel().setClickableUiViewModels(clickableUiViewModelList);
            ShareModel shareModel = ShareModel.createBuilder()
                    .setCode(topicHeadData.getCode())
                    .setDes(topicHeadData.getIntroduction())
                    .setTitle(topicHeadData.getName())
                    .setImgUrl(topicHeadData.getBigImageUrl())
                    .setShareType(ShareTypeConstants.TYPE_ACTIVITY_SHARE_TOPIC)
                    .setType(ShareConstants.TYPE_ALL)
                    .build();
            setShareModel(shareModel, topicRepository);
        }
    }

    private void setShareModel(ShareModel shareModel, final TopicService topicRepository) {
        Router.getInstance().buildExecutor("/share/service/sharemodel").putObjParam(shareModel).notTransParam()
                .callback(new Executor.Callback<ShareParam.Builder>() {
                    @Override
                    public void onCall(ShareParam.Builder builder) {
                        topicRepository.setBuilder(builder);
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                }).notTransCallbackData().excute();
    }

    private TopicHeadViewModel getHeadModel(TopicListModel data) {
        TopicHeadViewModel topicHeadData = new TopicHeadViewModel();
        topicHeadData.setBigImageUrl(data.getBigImageUrl());
        topicHeadData.setIntroduction(data.getIntroduction());
        topicHeadData.setName(data.getName());
        topicHeadData.setCode(data.getCode());
        return topicHeadData;
    }

    @Override
    public void init(Context context) {

    }
}
