package com.whaley.biz.program.ui.detail.component;


import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.ui.detail.ProgramDetailView;
import com.whaley.biz.program.ui.detail.presenter.BaseProgramDetailPresenter;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.ShareUtil;
import com.whaley.core.share.model.ShareParam;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.disposables.Disposable;

public class ProgramShareController extends BaseController {
    ProgramDetailView programDetailView;
    Disposable disposable;
    protected ShareParam.Builder shareParamBuilder;


    public ProgramDetailView getUIView() {
        return programDetailView;
    }

    public ProgramShareController(ProgramDetailView programDetailView) {
        this.programDetailView = programDetailView;
    }

    @Subscribe
    public void onFollowEvent(ModuleEvent event) {
        if (BaseProgramDetailPresenter.KEY_EVENT_SHARE.equals(event.getEventName())) {
            shareParamBuilder.setContext(getUIView().getAttachActivity());
            ShareUtil.share(shareParamBuilder.build());
            return;
        }
    }

    @Subscribe
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent) {
        PlayData playData = prepareStartPlayEvent.getPlayData();
        ShareModel shareModel = getShareModel(playData);
        Router.getInstance().buildExecutor("/share/service/sharemodel").putObjParam(shareModel).notTransParam()
                .callback(new Executor.Callback<ShareParam.Builder>() {
                    @Override
                    public void onCall(ShareParam.Builder builder) {
                        shareParamBuilder = builder;
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                }).notTransCallbackData().excute();
    }

    protected ShareModel getShareModel(PlayData playData){
        ProgramDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.PROGRAM_INFO);
        ShareModel shareModel = ShareModel.createBuilder()
                .setImgUrl(getImg(programDetailModel))
                .setTitle(getVideoName(programDetailModel))
                .setType(ShareConstants.TYPE_ALL)
                .setCode(programDetailModel.getCode())
                .setDes(getDes(programDetailModel))
                .setShareType(getType(programDetailModel))
                .build();
        return shareModel;
    }

    private int getType(ProgramDetailModel bean) {
        if (ProgramConstants.VIDEO_TYPE_VR.equals(bean.getVideoType())) {
            return ShareTypeConstants.TYPE_SHARE_VIDEO;
        }
        if (ProgramConstants.VIDEO_TYPE_3D.equals(bean.getVideoType())) {
            return ShareTypeConstants.TYPE_SHARE_MOVIE;
        }
        if (ProgramConstants.VIDEO_TYPE_MORETV_2D.equals(bean.getVideoType())) {
            return ShareTypeConstants.TYPE_SHARE_MM;
        }
        if (ProgramConstants.VIDEO_TYPE_MORETV_TV.equals(bean.getVideoType())) {
            return ShareTypeConstants.TYPE_SHARE_MT;
        }
        return ShareTypeConstants.TYPE_SHARE_VIDEO;
    }

    private String getDes(ProgramDetailModel bean) {
//        if (StrUtil.isEmpty(bean.getDescription()) && ProgramConstants.VIDEO_TYPE_MORETV_TV.equals(bean.getVideoType()))
//            return bean.getSeriesDescription();
        return bean.getDescription();
    }

    public String getImg(ProgramDetailModel bean) {
        if (ProgramConstants.VIDEO_TYPE_MORETV_2D.equals(bean.getVideoType())) {
            if (StrUtil.isEmpty(bean.getSmallPic()))
                return bean.getLunboPic();
            return bean.getSmallPic();
        }
        if (ProgramConstants.VIDEO_TYPE_MORETV_TV.equals(bean.getVideoType())) {
            return bean.getLunboPic();
        }
        if (StrUtil.isEmpty(bean.getBigPic()))
            return bean.getLunboPic();
        return bean.getBigPic();

    }

    /**
     * 获取标题 主要由于电视剧标题特殊
     *
     * @return
     */
    private String getVideoName(ProgramDetailModel programDetailModel) {
        String displayName = programDetailModel.getDisplayName();
        if (StrUtil.isEmpty(displayName)) {
            return displayName;
        }
        if (!ProgramConstants.VIDEO_TYPE_MORETV_TV.equals(programDetailModel.getVideoType())) {
            return displayName;
        }
        int index = displayName.indexOf("[");
        if (index > 0) {
            displayName = displayName.substring(0, index);
        }
        return displayName;
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        dispose();
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        dispose();
        programDetailView = null;
        shareParamBuilder = null;
    }


}