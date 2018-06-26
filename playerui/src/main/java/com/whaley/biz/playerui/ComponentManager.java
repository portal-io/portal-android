package com.whaley.biz.playerui;

import android.content.Context;

import com.whaley.biz.playerui.component.Component;
import com.whaley.biz.playerui.playercontroller.IPlayerController;
import com.whaley.core.debug.logger.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by YangZhi on 2017/8/1 11:02.
 */

public class ComponentManager {
    private final List<Component> COMPONENTS = new ArrayList<>();

    private final Context context;

    private final IPlayerController playerController;

    public ComponentManager(Context context, IPlayerController playerController) {
        this.context = context;
        this.playerController = playerController;
    }

    public void regist(Component component) {
        component.init(context, playerController);
        COMPONENTS.add(component);
    }

    public void registAll(List<Component> components) {
        for (Component component : components) {
            regist(component);
        }
    }

    public void unRegist(Component component) {
        try {
            component.destroy();
        }catch (Exception e){
            Log.e(e,"ComponentManager unRegist component = "+component);
        }
        COMPONENTS.remove(component);
    }

    public void unRegistAll() {
        Iterator<Component> componentIterator = getComponents().iterator();
        while (componentIterator.hasNext()) {
            Component component = componentIterator.next();
            try {
                component.destroy();
            }catch (Exception e){
                Log.e(e,"ComponentManager unregistAll component = "+component);
            }
        }
        getComponents().clear();
    }

    public List<Component> getComponents() {
        return COMPONENTS;
    }
}
