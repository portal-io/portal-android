package com.whaley.biz.program.playersupport.component.normalplayer.normalreset;

import com.whaley.biz.playerui.component.simpleplayer.resetscreen.ResetController;
import com.whaley.biz.playerui.component.simpleplayer.resetscreen.ResetUIAdapter;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerType;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dell on 2017/11/15.
 */

public class NormalResetController<T extends ResetUIAdapter> extends ResetController<T> {

    @Subscribe(sticky = true)
    public void onVideoPrepaed(VideoPreparedEvent videoPreparedEvent) {
        int type = videoPreparedEvent.getPlayData().getType();
        if (type != PlayerType.TYPE_PANO) {
            hide();
        }else if(isLandScape()){
            super.show();
        }
    }

    @Override
    protected void show() {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (playData != null && playData.getType() == PlayerType.TYPE_PANO) {
            super.show();
        }
    }

    @Override
    protected void showUI(boolean anim) {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (playData != null && playData.getType() == PlayerType.TYPE_PANO) {
            super.showUI(anim);
        }
    }

}
