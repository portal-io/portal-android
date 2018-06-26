package com.whaley.biz.program.playersupport.component.bannerplayer.typeswitcher;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.playersupport.event.SwitchBannerTypeEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/28 16:19.
 */

public class TypeSwitcherController extends BaseController {


    public TypeSwitcherController() {
    }

    @Subscribe
    public void onPreparingEvent(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        PlayData playData = preparingEvent.getPlayData();
        int type = playData.getType();
        switch (type) {
            case PlayerType.TYPE_LIVE:
                changeToLive();
                break;
            case PlayerType.TYPE_3D:
            case PlayerType.TYPE_MORETV_2D:
            case PlayerType.TYPE_MORETV_TV:
                changeToMovie();
                break;
            case PlayerType.TYPE_PANO:
                changeToVR();
                break;
        }
    }

    private void changeToVR() {
        SwitchBannerTypeEvent switchBannerTypeEvent = new SwitchBannerTypeEvent();
        switchBannerTypeEvent.setType(SwitchBannerTypeEvent.TYPE_VR);
        emitStickyEvent(switchBannerTypeEvent);
    }

    private void changeToMovie() {
        SwitchBannerTypeEvent switchBannerTypeEvent = new SwitchBannerTypeEvent();
        switchBannerTypeEvent.setType(SwitchBannerTypeEvent.TYPE_MOVIE);
        emitStickyEvent(switchBannerTypeEvent);
    }

    private void changeToLive() {
        SwitchBannerTypeEvent switchBannerTypeEvent = new SwitchBannerTypeEvent();
        switchBannerTypeEvent.setType(SwitchBannerTypeEvent.TYPE_LIVE);
        emitStickyEvent(switchBannerTypeEvent);
    }
}
