package com.whaleyvr.biz.unity.event;

import com.whaley.biz.common.event.BaseEvent;
import com.whaleyvr.biz.unity.model.MediaResultInfo;

/**
 * Created by dell on 2017/12/12.
 */

public class ChangeVideoProgressEvent extends BaseEvent {

    public ChangeVideoProgressEvent(MediaResultInfo mediaResultInfo) {
        super("changeVideoProgress", mediaResultInfo);
    }

}
