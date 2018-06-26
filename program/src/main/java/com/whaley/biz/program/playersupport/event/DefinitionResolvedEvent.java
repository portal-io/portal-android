package com.whaley.biz.program.playersupport.event;

import android.support.v4.util.SimpleArrayMap;

import com.whaley.biz.playerui.event.Event;
import com.whaley.biz.playerui.event.PriorityEvent;
import com.whaley.biz.program.playersupport.model.DefinitionModel;

/**
 * Created by YangZhi on 2017/8/4 21:47.
 */

public class DefinitionResolvedEvent extends PriorityEvent{

    private SimpleArrayMap<Integer, DefinitionModel> definitionModelMap;


    public void setDefinitionModelMap(SimpleArrayMap<Integer, DefinitionModel> definitionModelMap) {
        this.definitionModelMap = definitionModelMap;
    }

    public SimpleArrayMap<Integer, DefinitionModel> getDefinitionModelMap() {
        return definitionModelMap;
    }
}
