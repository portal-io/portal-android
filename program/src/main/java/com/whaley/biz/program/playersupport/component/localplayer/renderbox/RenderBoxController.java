package com.whaley.biz.program.playersupport.component.localplayer.renderbox;

import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.playersupport.component.normalplayer.resolvedefinition.RenderTypeUtil;
import com.whaley.biz.program.playersupport.event.RenderTypeSelectedEvent;
import com.whaley.biz.program.playersupport.event.ToggleRenderBoxVisibleEvent;
import com.whaley.wvrplayer.vrplayer.external.event.VideoConstantValue;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yangzhi on 2017/8/23.
 */

public class RenderBoxController extends ControlController<RenderBoxUIAdapter> {


    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        PlayData playData = preparingEvent.getPlayData();
        boolean isCanChangeRender = playData.getBooleanCustomData(PlayerDataConstants.IS_CAN_CHANGE_RENDER);
        if (isCanChangeRender) {
            getUIAdapter().setRenderBoxVisible();
        }
    }

    @Subscribe
    public void onToggleRenderBoxVisibleEvent(ToggleRenderBoxVisibleEvent event) {
        getUIAdapter().toggleRenderBox();
    }

    public void onRenderTypeSelected(String renderTypeStr, String formatRenderTypeStr) {

        int renderType = RenderTypeUtil.getRenderTypeByRenderTypeStr(renderTypeStr);
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        playData.putCustomData(PlayerDataConstants.CURRENT_RENDER_TYPE, renderTypeStr);
        playData.setRenderType(renderType);
        getPlayerController().setRenderType(renderType);
        RenderTypeSelectedEvent renderTypeSelectedEvent = new RenderTypeSelectedEvent();
        renderTypeSelectedEvent.setRenderTypeStr(formatRenderTypeStr);
        emitEvent(renderTypeSelectedEvent);
    }

}
