package com.whaley.biz.playerui.component;

import android.content.Context;

import com.whaley.biz.playerui.playercontroller.IPlayerController;

/**
 * Created by YangZhi on 2017/8/1 11:34.
 */

public abstract class BaseComponent implements Component{

    private Context context;

    private IPlayerController playerController;

    private BaseController controller;

    private BaseUIAdapter uiAdapter;

    @Override
    public void init(Context context, IPlayerController playerController) {
        this.context = context;
        controller = onCreateController();
        uiAdapter = onCreateUIAdapter();

        if(controller!=null) {
            controller.setContext(context);
            controller.setPlayerController(playerController);
            controller.init();
        }
        if(uiAdapter!=null) {
            uiAdapter.setContext(context);
            uiAdapter.setController(controller);
        }

    }

    protected abstract BaseController onCreateController();

    protected abstract BaseUIAdapter onCreateUIAdapter();

    @Override
    public final UIAdapter getUIAdapter() {
        return uiAdapter;
    }

    @Override
    public final BaseController getController() {
        return controller;
    }

    public IPlayerController getPlayerController() {
        return playerController;
    }

    @Override
    public void destroy() {
        context = null;
        if(controller!=null) {
            controller.destroy();
            controller = null;
        }
        if(uiAdapter!=null) {
            uiAdapter.destroy();
            uiAdapter = null;
        }
    }

}
