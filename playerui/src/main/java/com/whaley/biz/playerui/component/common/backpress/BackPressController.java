package com.whaley.biz.playerui.component.common.backpress;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.playerui.event.SplitChangeEvent;
import com.whaley.biz.playerui.event.SwitchScreenEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/4 17:08.
 */

public class BackPressController extends BaseController{

    private SwitchScreenHandle switchScreenHandle;

    public BackPressController(SwitchScreenHandle switchScreenHandle) {
        this.switchScreenHandle = switchScreenHandle;
    }

    @Subscribe
    public void onBackPressed(BackPressEvent backPressEvent){
        if(getSwitchScreenHandle()==null || !getSwitchScreenHandle().shouldSwitchOnBack()){
            if(getActivity()!=null){
                getActivity().finish();
            }
            return;
        }
        SwitchScreenEvent switchScreenEvent = new SwitchScreenEvent();
        emitEvent(switchScreenEvent);
    }


    public SwitchScreenHandle getSwitchScreenHandle() {
        return switchScreenHandle;
    }

    public interface SwitchScreenHandle{
        boolean shouldSwitchOnBack();
    }
}
