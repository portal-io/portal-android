package com.whaley.biz.program.playersupport.component.newplaydatacontinue;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BufferingEvent;
import com.whaley.biz.playerui.event.NewPlayDataContinueEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerType;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/29 16:26.
 */

public class NewPlayDataContinueController extends BaseController{


    @Subscribe(priority = 1)
    public void onNewPlayDataContinueEvent(NewPlayDataContinueEvent newPlayDataEvent){
        PlayData playData = newPlayDataEvent.getPlayData();
        if(playData.getType() == PlayerType.TYPE_LIVE){
            getEventBus().cancelEventDelivery(newPlayDataEvent);
            emitEvent(new BufferingEvent());
            PlayData playData1 = getPlayerController().getRepository().getCurrentPlayData();
            playData1.setProgress(0);
            getPlayerController().startPlay(playData1);
        }
    }
}
