package com.whaley.biz.program.playersupport.component.localplayer.localbottomui;

import com.whaley.biz.playerui.component.simpleplayer.bottomcontrol.BottomControlController;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.playersupport.event.RenderTypeSelectedEvent;
import com.whaley.biz.program.playersupport.event.ToggleRenderBoxVisibleEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yangzhi on 2017/8/22.
 */

public class LocalBottomUIController extends BottomControlController<LocalBottomUIAdapter>{

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        PlayData playData = preparingEvent.getPlayData();
        boolean isCanChangeRender = playData.getBooleanCustomData(PlayerDataConstants.IS_CAN_CHANGE_RENDER);
        if(isCanChangeRender){
            getUIAdapter().setChangeRenderVisible();
        }
    }

    @Subscribe
    public void onRenderTypeSelected(RenderTypeSelectedEvent renderTypeSelectedEvent){
        getUIAdapter().updateChangeRenderBtnText(renderTypeSelectedEvent.getRenderTypeStr());
    }

    public void onChangeRenderClick(){
        emitEvent(new ToggleRenderBoxVisibleEvent());
    }

    public void onSplitClick(){
        if (isForbidClick()) {
            return;
        }
        //
        boolean isMonocular = !getPlayerController().getMonocular();
        getPlayerController().setMonocular(isMonocular);
        getUIAdapter().setMonocular(isMonocular);
    }

}
