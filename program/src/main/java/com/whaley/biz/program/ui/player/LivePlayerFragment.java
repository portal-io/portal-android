package com.whaley.biz.program.ui.player;

import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.playerui.playercontroller.PlayerController;
import com.whaley.biz.program.playersupport.event.ActivityResultEvent;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.core.appcontext.AppContextProvider;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by YangZhi on 2017/8/22 12:16.
 */

@Route(path = ProgramRouterPath.PLAYER_LIVE)
public class LivePlayerFragment extends PlayerFragment{

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_player_live;
    }


    @Override
    protected boolean isDefaultLandScape() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CommonConstants.SHARE_REQUEST_CODE && !data.getBooleanExtra(CommonConstants.SHARE_PARAM_OUTSIDE, true))
            return;
        EventBus eventBus = PlayerController.getInstance(AppContextProvider.getInstance().getContext()).getEventBus();
        if(eventBus==null){
            return;
        }
        if (!eventBus.hasSubscriberForEvent(ActivityResultEvent.class)) {
            return;
        }
        eventBus.post(new ActivityResultEvent());
    }

}
