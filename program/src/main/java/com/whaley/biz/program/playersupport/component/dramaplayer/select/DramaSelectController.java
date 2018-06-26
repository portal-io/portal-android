package com.whaley.biz.program.playersupport.component.dramaplayer.select;

import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import android.widget.Toast;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BufferingEvent;
import com.whaley.biz.playerui.event.BufferingOffEvent;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.PollingEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.ScreenChangedEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.model.MediaModel;
import com.whaley.biz.program.model.NodeModel;
import com.whaley.biz.program.model.ProgramDramaDetailModel;
import com.whaley.biz.program.playersupport.event.ShowDramaSelectEvent;
import com.whaley.biz.program.playersupport.model.MediaResultInfo;
import com.whaley.core.router.Router;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dell on 2017/11/13.
 */

public class DramaSelectController extends BaseController<DramaSelectUIAdapter> implements BIConstants {

    boolean isBuffing = false;

    boolean isDramaSelect;
    boolean isFullScreen;

    @Subscribe
    public void onPolling(PollingEvent pollingEvent) {
        if (!getPlayerController().isStarted()) {
            return;
        }
        checkDramaVisible(false);
    }

    private void checkDramaVisible(boolean isCompleted) {
        long currentProgress = getPlayerController().getCurrentProgress();
        NodeModel nodeModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.CURRENT_DRAMA_NODE);
        if (nodeModel != null && currentProgress >= nodeModel.getTipTime() && (isCompleted || nodeModel.getTipTime() > 0)) {
            if (isDramaSelect) {
                return;
            }
            String childrenCode = nodeModel.getChildrenCode();
            if (!TextUtils.isEmpty(childrenCode)) {
                List<NodeModel> currentNodeList = new ArrayList<>();
                String[] childrens = childrenCode.split("-");
                for (String children : childrens) {
                    NodeModel childrenNodeModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(children);
//                    if (childrenNodeModel.getDefaultVisible() == 0) {
//                        continue;
//                    }
                    if (childrenNodeModel != null && (StrUtil.isEmpty(nodeModel.getDefaultItem()) || !childrenNodeModel.getCode().equals(nodeModel.getDefaultItem()) || nodeModel.getDefaultVisible() == 1)) {
                        currentNodeList.add(childrenNodeModel);
                    }
                }
//                currentNodeList.add(nodeModel);
                if (currentNodeList == null || currentNodeList.size() <= 1) {
                    return;
                }
                if (getUIAdapter() != null) {
                    getUIAdapter().setData(currentNodeList);
                }
                isDramaSelect = true;
                if (!isBuffing) {
                    if (getUIAdapter() != null) {
                        getUIAdapter().show();
                    }
                    getEventBus().post(new ShowDramaSelectEvent(true));
                }
            }
        } else {
            isDramaSelect = false;
            if (getUIAdapter() != null) {
                getUIAdapter().hide();
            }
            getEventBus().post(new ShowDramaSelectEvent(false));
        }
    }

    @Subscribe
    public void onScreenChangedEvent(ScreenChangedEvent screenChangedEvent) {
        if (screenChangedEvent.getRequestOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            isFullScreen = true;
            if (isDramaSelect && getUIAdapter() != null) {
                getUIAdapter().switchScreen(isFullScreen);
            }
        } else {
            isFullScreen = false;
            getUIAdapter().switchScreen(isFullScreen);
        }
    }

    @Subscribe
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        cacheSelectImage();
        showSelect();
        isBuffing = false;
    }


    private void cacheSelectImage() {
        NodeModel nodeModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.CURRENT_DRAMA_NODE);
        String childrenCode = nodeModel.getChildrenCode();
        if (StrUtil.isEmpty(childrenCode)) {
            return;
        }
        String[] childrens = childrenCode.split("-");
        for (String children : childrens) {
            NodeModel childrenNodeModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(children);
            if (childrenNodeModel != null && (StrUtil.isEmpty(nodeModel.getDefaultItem()) || !childrenNodeModel.getCode().equals(nodeModel.getDefaultItem()) || nodeModel.getDefaultVisible() == 1)) {
                getUIAdapter().requestManager.load(childrenNodeModel.getSmallPic()).get(null);
            }
        }
    }

    @Subscribe
    public void onBuffering(BufferingEvent bufferingEvent) {
        isBuffing = true;
        if (getUIAdapter() != null) {
            getUIAdapter().hide();
        }
        getEventBus().post(new ShowDramaSelectEvent(false));
    }

    @Subscribe
    public void onBufferingOff(BufferingOffEvent bufferingOffEvent) {
        showSelect();
        isBuffing = false;
    }

    private void showSelect() {
        if (isDramaSelect && getUIAdapter() != null) {
            getUIAdapter().show();
            getEventBus().post(new ShowDramaSelectEvent(true));
        }
    }


    @Subscribe
    public void onCompletedEvent(CompletedEvent completedEvent) {
        NodeModel nodeModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.CURRENT_DRAMA_NODE);
        if (nodeModel == null)
            return;
        if (nodeModel.getTipTime() <= 0) {
            checkDramaVisible(true);
            return;
        }
        onNodeSelected(nodeModel.getDefaultItem());
    }

    public void onNodeSelected(String nodeCode) {
        if (TextUtils.isEmpty(nodeCode))
            return;
        NodeModel nodeModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(nodeCode);
        onNodeSelected(nodeModel, false);
    }

    public void onNodeSelected(NodeModel nodeModel) {
        onNodeSelected(nodeModel, true);
    }

    public void onNodeSelected(NodeModel nodeModel, boolean isBury){
        onNodeSelected(nodeModel, isBury, 0, true);
    }

    public void onNodeSelected(NodeModel nodeModel, boolean isBury, long progress, boolean isAddTrack) {
        if (nodeModel == null) {
            Toast.makeText(getContext(), getContext().getString(R.string.drama_no_media_url), Toast.LENGTH_SHORT).show();
            return;
        }
        List<MediaModel> mediaDtos = nodeModel.getMediaDtos();
        if (mediaDtos == null || mediaDtos.size() == 0) {
            Toast.makeText(getContext(), getContext().getString(R.string.drama_no_media_url), Toast.LENGTH_SHORT).show();
            return;
        }
        Iterator<MediaModel> iterator = mediaDtos.iterator();
        MediaModel mediaModel = null;
        while (iterator.hasNext()) {
            MediaModel nextMediaModel = iterator.next();
            if (nextMediaModel.getSource() != null && (nextMediaModel.getSource().equals("vr_share") || nextMediaModel.getSource().equals("vr_share_4k"))) {
                continue;
            }
            mediaModel = nextMediaModel;
            break;
        }
        if (mediaModel == null) {
            Toast.makeText(getContext(), getContext().getString(R.string.drama_no_media_url), Toast.LENGTH_SHORT).show();
            return;
        }
        BufferingEvent bufferingEvent = new BufferingEvent();
        emitStickyEvent(bufferingEvent);
        getPlayerController().pause();
        if (isBury) {
            selectDrama(nodeModel);
        }
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        playData.putCustomData(PlayerDataConstants.CURRENT_DRAMA_NODE, nodeModel);
        playData.setOrginUrl(mediaModel.getPlayUrl());
        playData.putCustomData(PlayerDataConstants.SERVER_RENDER_TYPE, mediaModel.getRenderType());
        playData.setProgress(progress);
        if(isAddTrack) {
            List<String> trackList = playData.getCustomData(PlayerDataConstants.DRAMA_TRACK_LIST);
            trackList.add(nodeModel.getCode());
            playData.putCustomData(PlayerDataConstants.DRAMA_TRACK_LIST, trackList);
        }
        PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
        prepareStartPlayEvent.setPlayData(playData);
        prepareStartPlayEvent.setMaxPriority(PlayerConstants.PREPARE_START_PLAY_PRIORITY_GET_INFO - 1);
        emitStickyEvent(prepareStartPlayEvent);
        isDramaSelect = false;
        if (getUIAdapter() != null) {
            getUIAdapter().hide();
        }
        getEventBus().post(new ShowDramaSelectEvent(false));
    }

//    @Subscribe(priority = 1)
//    public void onExitSplitEvent(BaseEvent baseEvent){
//        if(baseEvent.getEventType().equals("changeDramaProgress")){
//            MediaResultInfo mediaResultInfo = baseEvent.getObject(MediaResultInfo.class);
//            NodeModel nodeModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.CURRENT_DRAMA_NODE);
//            if(nodeModel!=null&&nodeModel.getCode()!=null&&nodeModel.getCode().equals(mediaResultInfo.getCurrentNode())){
//                PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
//                if(playData!=null&&playData.getId().equals(mediaResultInfo.getCode())&&mediaResultInfo.getNodeTrack()!=null
//                        &&!mediaResultInfo.getNodeTrack().isEmpty()) {
//                    playData.putCustomData(PlayerDataConstants.DRAMA_TRACK_LIST, mediaResultInfo.getNodeTrack());
//                }
//                //事件继续往下传递
//            }else{
//                EventController.cancelEventDelivery(baseEvent);
//                PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
//                if(playData!=null&&playData.getId().equals(mediaResultInfo.getCode())) {
//                    if(mediaResultInfo.getNodeTrack()!=null
//                            &&!mediaResultInfo.getNodeTrack().isEmpty()) {
//                        playData.putCustomData(PlayerDataConstants.DRAMA_TRACK_LIST, mediaResultInfo.getNodeTrack());
//                    }
//                    NodeModel currentNodeModel = playData.getCustomData(mediaResultInfo.getCurrentNode());
//                    if(currentNodeModel!=null) {
//                        onNodeSelected(currentNodeModel, false, Long.valueOf(mediaResultInfo.getCurrentPosition()), false);
//                    }
//                }
//            }
//        }
//    }

//    @Override
//    public void registEvents() {
//        super.registEvents();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
//    }
//
//    @Override
//    public void unRegistEvents() {
//        super.unRegistEvents();
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
//    }


    //===================================互动剧选择剧情埋点=======================================//
    private void selectDrama(NodeModel nodeModel) {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        ProgramDramaDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.DRAMA_INFO);
        if (programDetailModel == null)
            return;
        NodeModel odlNodeModel = playData.getCustomData(PlayerDataConstants.CURRENT_DRAMA_NODE);
        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                .setEventId(PREVUE_DYNAMIC_SELECTED)
                .setCurrentPageId(ROOT_PLAY)
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, programDetailModel.getCode())
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, programDetailModel.getDisplayName())
                .putCurrentPagePropKeyValue(CURRENT_PROP_CURRENT_DRAMA_ID, odlNodeModel.getCode())
                .putCurrentPagePropKeyValue(CURRENT_PROP_CURRENT_DRAMA_TITLE, odlNodeModel.getTitle())
                .putEventPropKeyValue(CURRENT_PROP_DRAMA_ID, nodeModel.getCode())
                .putEventPropKeyValue(CURRENT_PROP_DRAMA_TITLE, nodeModel.getTitle())
                .setNextPageId(ROOT_PLAY);
        if (builder != null) {
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

}
