package com.whaley.biz.program.playersupport.event;

import com.whaley.biz.playerui.event.Event;
import com.whaley.biz.playerui.event.PriorityEvent;

/**
 * Created by yangzhi on 2017/8/5.
 */

public class SwitchDefinitionEvent extends PriorityEvent{

    private int definition;

    public int getDefinition() {
        return definition;
    }

    public void setDefinition(int definition) {
        this.definition = definition;
    }

}
