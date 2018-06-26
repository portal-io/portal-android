package com.whaley.biz.program.playersupport.component.normalplayer.tvmore;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.model.MediaModel;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.playersupport.exception.ProgramErrorConstants;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by YangZhi on 2017/8/24 16:23.
 */

public class TVMoreController extends BaseController{

    @Subscribe(priority = PlayerConstants.PREPARE_START_PLAY_PRIORITY_TVMORE)
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent){
        if(prepareStartPlayEvent.getMaxPriority()<PlayerConstants.PREPARE_START_PLAY_PRIORITY_TVMORE)
            return;
        PlayData playData = prepareStartPlayEvent.getPlayData();
        getPlayerController().pause();
        int type = playData.getType();
        if(checkIsTVMore(type)){
            ProgramDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.PROGRAM_INFO);
            List<MediaModel> mediaModels = programDetailModel.getMedias();
            int index = playData.getIntegerCustomData(PlayerDataConstants.TVMORE_SOURCE_INDEX);
            MediaModel mediaModel = mediaModels.get(index);
            playData.setOrginUrl(mediaModel.getPlayUrl());
        }
    }

    @Subscribe(priority = PlayerConstants.ERROR_EVENT_PRIORITY_TV_MORE)
    public void onErrorEvent(ErrorEvent errorEvent){
        PlayerException e = errorEvent.getPlayerException();
        if (e.getErrorCode() == ProgramErrorConstants.ERROR_ALL_DEFINITION_ERROR ){
            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
            int type = playData.getType();
            if(checkIsTVMore(type)){
                ProgramDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.PROGRAM_INFO);
                int index = playData.getIntegerCustomData(PlayerDataConstants.TVMORE_SOURCE_INDEX);
                List<MediaModel> mediaModels = programDetailModel.getMedias();
                if(index<mediaModels.size()-1){
                    index = index+1;
                    playData.putCustomData(PlayerDataConstants.TVMORE_SOURCE_INDEX,index);

                    getEventBus().cancelEventDelivery(errorEvent);
                    PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
                    prepareStartPlayEvent.setMaxPriority(PlayerConstants.PREPARE_START_PLAY_PRIORITY_TVMORE);
                    prepareStartPlayEvent.setPlayData(playData);
                    getEventBus().post(prepareStartPlayEvent);
//                    getPlayerController().prepareStartPlay();
                }
            }
        }
    }

    private boolean checkIsTVMore(int type){
        return type == PlayerType.TYPE_MORETV_TV || type == PlayerType.TYPE_MORETV_2D;
    }
}
