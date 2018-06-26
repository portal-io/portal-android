package com.whaley.biz.playerui.event;

import com.whaley.biz.playerui.model.PlayData;

/**
 * Created by yangzhi on 2017/8/29.
 */

public class StartedEvent extends PriorityEvent{
    private PlayData playData;

    public void setPlayData(PlayData playData) {
        this.playData = playData;
    }

    public PlayData getPlayData() {
        return playData;
    }
}
