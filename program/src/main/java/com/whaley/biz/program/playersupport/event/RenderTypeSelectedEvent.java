package com.whaley.biz.program.playersupport.event;

import com.whaley.biz.playerui.event.Event;
import com.whaley.biz.playerui.event.PriorityEvent;

/**
 * Created by yangzhi on 2017/8/23.
 */

public class RenderTypeSelectedEvent extends PriorityEvent {

    private String renderTypeStr;

    public void setRenderTypeStr(String renderTypeStr) {
        this.renderTypeStr = renderTypeStr;
    }

    public String getRenderTypeStr() {
        return renderTypeStr;
    }
}
