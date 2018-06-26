package com.whaley.biz.program.playersupport.component.liveplayer.initblurbackground;

import com.whaley.biz.playerui.component.simpleplayer.initbackground.InitBackgroundController;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.model.State;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.playersupport.exception.ProgramErrorConstants;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/8 19:38.
 */

public class InitBlurBackgroundController extends InitBackgroundController<InitBlurBackgroundUIAdapter> {

    String imageUrl;

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        PlayData playData = preparingEvent.getPlayData();
        String imageUrl = playData.getCustomData(PlayerDataConstants.LIVE_INIT_BACKGROUND_IMAGE);
        this.imageUrl = imageUrl;
        getUIAdapter().loadBlurImage(imageUrl);
    }

    @Subscribe(priority = PlayerConstants.PREPARE_START_PLAY_PRIORITY_PROGRAM)
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent) {
        if(prepareStartPlayEvent.getMaxPriority()<PlayerConstants.PREPARE_START_PLAY_PRIORITY_PROGRAM){
            return;
        }
        PlayData playData = prepareStartPlayEvent.getPlayData();
        String imageUrl = playData.getCustomData(PlayerDataConstants.LIVE_INIT_BACKGROUND_IMAGE);
        if (this.imageUrl == null || !this.imageUrl.equals(imageUrl)) {
            this.imageUrl = imageUrl;
            getUIAdapter().loadBlurImage(imageUrl);
        }
    }

    @Override
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        super.onVideoPrepared(videoPreparedEvent);
        PlayData playData = videoPreparedEvent.getPlayData();
        String imageUrl = playData.getCustomData(PlayerDataConstants.LIVE_INIT_BACKGROUND_IMAGE);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void onClick() {
        int state = getPlayerController().getState().getCurrentState();
        if (state == State.STATE_ERROR) {
            PlayerException playerException = getPlayerController().getRepository().getPlayerException();
            if (playerException.getErrorCode() == ProgramErrorConstants.ERROR_PAY_NEED_PAY) {
                return;
            }
            getPlayerController().reStartPlay(true);
        }
    }
}
