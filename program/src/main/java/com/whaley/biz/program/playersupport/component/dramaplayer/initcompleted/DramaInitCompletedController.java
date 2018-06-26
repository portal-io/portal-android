package com.whaley.biz.program.playersupport.component.dramaplayer.initcompleted;

import android.text.TextUtils;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.simpleplayer.initCompleted.InitCompletedController;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.model.MediaModel;
import com.whaley.biz.program.model.NodeModel;
import com.whaley.biz.program.model.SeriesModel;
import com.whaley.biz.program.playersupport.event.ChangeSerieEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dell on 2017/11/13.
 */

public class DramaInitCompletedController extends InitCompletedController<DramaInitCompletedUIAdapter> {

    @Override
    public void onCompletedEvent(CompletedEvent completedEvent) {
        NodeModel nodeModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.CURRENT_DRAMA_NODE);
        if (isLastNode(nodeModel) && TextUtils.isEmpty(nodeModel.getDefaultItem())) {
            getUIAdapter().changeVisibleOnComplete();
            getUIAdapter().showOrHideLayoutNext(hasNext());
            getEventBus().post(new ModuleEvent("drama_completed", null));
        }
    }

    private boolean isLastNode(NodeModel nodeModel) {
        boolean isLast = false;
        if (TextUtils.isEmpty(nodeModel.getChildrenCode())) {
            isLast = true;
        } else {
            String[] childrens = nodeModel.getChildrenCode().split("-");
            if (childrens.length <= 1) {
                isLast = true;
            }
        }
        return isLast;
    }

    @Override
    public void onPlayReturnClick() {
        NodeModel nodeModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.START_DRAMA_NODE);
        if (nodeModel == null) {
            return;
        }
        List<MediaModel> mediaDtos = nodeModel.getMediaDtos();
        if (mediaDtos == null || mediaDtos.size() == 0) {
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
        if (mediaModel == null)
            return;
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        playData.putCustomData(PlayerDataConstants.CURRENT_DRAMA_NODE, nodeModel);
        playData.setOrginUrl(mediaModel.getPlayUrl());
        playData.putCustomData(PlayerDataConstants.SERVER_RENDER_TYPE, mediaModel.getRenderType());
        playData.setProgress(0);
        List<String> trackList = new ArrayList<>();
        trackList.add(nodeModel.getCode());
        playData.putCustomData(PlayerDataConstants.DRAMA_TRACK_LIST, trackList);
        getPlayerController().replay();
    }

}
