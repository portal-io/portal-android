package com.whaley.biz.playerui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.biz.playerui.event.ActivityPauseEvent;
import com.whaley.biz.playerui.event.ActivityResumeEvent;
import com.whaley.biz.playerui.event.ConfigurationChangeEvent;
import com.whaley.core.utils.DisplayUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yangzhi on 2017/9/22.
 */

public class LifeCycleFragment extends Fragment {

    EventBus eventBus;

    View realPlayerView;


    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }


    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getEventBus() == null) {
            return;
        }

        if (!getEventBus().hasSubscriberForEvent(ConfigurationChangeEvent.class)) {
            return;
        }

        ConfigurationChangeEvent event = new ConfigurationChangeEvent();
        event.setNewConfig(newConfig);
        getEventBus().post(event);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getEventBus() == null) {
            return;
        }

        if (!getEventBus().hasSubscriberForEvent(ActivityResumeEvent.class)) {
            return;
        }

        getEventBus().post(new ActivityResumeEvent());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getEventBus() == null) {
            return;
        }

        if (!getEventBus().hasSubscriberForEvent(ActivityPauseEvent.class)) {
            return;
        }

        getEventBus().post(new ActivityPauseEvent());
    }
}
