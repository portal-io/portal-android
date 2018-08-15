package com.whaleyvr.biz.unity.event;

import com.whaley.biz.common.event.BaseEvent;
import com.whaleyvr.biz.unity.model.MediaResultInfo;

/**
 * Created by dell on 2017/12/13.
 */

public class ChangeDramaProgressEvent extends BaseEvent {

    public ChangeDramaProgressEvent(MediaResultInfo mediaResultInfo) {
        super("changeDramaProgress", mediaResultInfo);
    }

}
