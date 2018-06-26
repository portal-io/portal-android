package com.whaley.biz.program.playersupport.component.dramaplayer.bottomui;

import android.widget.Toast;

import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.program.R;
import com.whaley.biz.program.playersupport.component.normalplayer.normalbottomui.NormalBottomController;
import com.whaley.biz.program.utils.UnityUtil;
import com.whaley.core.appcontext.AppContextProvider;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dell on 2017/11/14.
 */

public class DramaBottomController extends NormalBottomController<DramaBottomUIAdapter> {

    public DramaBottomController() {
        super(false);
    }

    @Override
    public void onCameraClick() {
        //
    }

    @Subscribe
    public void onModuleEvent(ModuleEvent moduleEvent) {
        if ("event/chunjie/activity".equals(moduleEvent.getEventName())) {
            if ((boolean) moduleEvent.getParam()){
                getUIAdapter().showActivityIcon();
            }
            else {
                getUIAdapter().hideActivityIcon();
            }
        }
    }

}
