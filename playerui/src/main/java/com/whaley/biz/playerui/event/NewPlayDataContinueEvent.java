package com.whaley.biz.playerui.event;

import com.whaley.biz.playerui.model.PlayData;

/**
 * Created by YangZhi on 2017/8/29 16:23.
 */

public class NewPlayDataContinueEvent extends PriorityEvent {
    private PlayData playData;

    public NewPlayDataContinueEvent(PlayData playData) {
        this.playData = playData;
    }

    public PlayData getPlayData() {
        return playData;
    }
}
