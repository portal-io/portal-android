package com.whaley.biz.playerui.event;

import com.whaley.biz.playerui.model.PlayData;

/**
 * Created by YangZhi on 2017/8/1 13:49.
 */

public class VideoPreparedEvent extends PriorityEvent{

    private PlayData playData;

    public void setPlayData(PlayData playData) {
        this.playData = playData;
    }

    public PlayData getPlayData() {
        return playData;
    }
}
