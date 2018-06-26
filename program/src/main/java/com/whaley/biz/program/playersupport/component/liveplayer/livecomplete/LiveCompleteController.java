package com.whaley.biz.program.playersupport.component.liveplayer.livecomplete;

import android.app.Activity;
import android.widget.Toast;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BufferingEvent;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.playersupport.event.LiveStateCompletedEvent;
import com.whaley.core.appcontext.AppContextProvider;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/9/30 18:42.
 */

public class LiveCompleteController extends BaseController {
    static final int MAX_RETRY_COUNT = 5;

    int retryCount;
    boolean isOnBanner;

    public LiveCompleteController(boolean isOnBanner) {
        this.isOnBanner = isOnBanner;
    }

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        resetRetry();
    }

    @Subscribe(priority = 10)
    public void onCompleted(CompletedEvent completedEvent) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            getEventBus().cancelEventDelivery(completedEvent);
            emitEvent(new BufferingEvent());
            PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
            prepareStartPlayEvent.setMaxPriority(PlayerConstants.PREPARE_START_PLAY_PRIORITY_GET_INFO);
            emitEvent(prepareStartPlayEvent);
            return;
        }
        onRealCompleted();
    }

    @Subscribe
    public void onLiveStateCompleted(LiveStateCompletedEvent liveStateCompletedEvent) {
        onRealCompleted();
    }

    private void onRealCompleted() {
        Toast.makeText(AppContextProvider.getInstance().getContext(), "直播已结束...", Toast.LENGTH_LONG).show();
        if (isOnBanner)
            return;
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private void resetRetry() {
        retryCount = 0;
    }
}
