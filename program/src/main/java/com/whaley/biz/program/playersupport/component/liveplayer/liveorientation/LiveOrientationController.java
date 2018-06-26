package com.whaley.biz.program.playersupport.component.liveplayer.liveorientation;

import android.content.pm.ActivityInfo;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/22 16:53.
 */

public class LiveOrientationController extends BaseController{


    @Override
    protected boolean isRegistOrientation() {
        return true;
    }


    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        PlayData playData = preparingEvent.getPlayData();
        boolean isLandScape = playData.getBooleanCustomData(PlayerDataConstants.ORIENTATION_IS_LANDSCAPE);
        checkOrientation(isLandScape);
    }

    @Subscribe
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent){
        boolean isLandScape = prepareStartPlayEvent.getPlayData().getBooleanCustomData(PlayerDataConstants.ORIENTATION_IS_LANDSCAPE);
        checkOrientation(isLandScape);
    }

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent){
        PlayData playData = videoPreparedEvent.getPlayData();
        boolean isLandScape = playData.getBooleanCustomData(PlayerDataConstants.ORIENTATION_IS_LANDSCAPE);
        checkOrientation(isLandScape);
    }

    private void checkOrientation(boolean isLandScape){
        if(getActivity()==null)
            return;
        if(!isLandScape){
            if(getActivity().getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }else {
            if(getActivity().getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

}
