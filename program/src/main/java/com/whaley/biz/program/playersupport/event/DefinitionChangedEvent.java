package com.whaley.biz.program.playersupport.event;

import com.whaley.biz.playerui.event.Event;
import com.whaley.biz.playerui.event.PriorityEvent;
import com.whaley.biz.program.playersupport.model.DefinitionModel;

/**
 * Created by YangZhi on 2017/8/4 22:10.
 */

public class DefinitionChangedEvent extends PriorityEvent{

    private DefinitionModel definitionModel;

    public void setDefinitionModel(DefinitionModel definitionModel) {
        this.definitionModel = definitionModel;
    }

    public DefinitionModel getDefinitionModel() {
        return definitionModel;
    }
}
