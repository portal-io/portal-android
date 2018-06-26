package com.whaley.biz.program.playersupport.component.mobilenetwork;

import android.app.Dialog;
import android.view.View;

import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.ActivityPauseEvent;
import com.whaley.biz.playerui.event.ActivityResumeEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.core.utils.NetworkUtils;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/9/30 17:07.
 */

public class MobileNetworkController extends BaseController {

    Dialog dialog;

    @Subscribe(priority = 1000)
    public void onPrepareStart(final PrepareStartPlayEvent prepareStartPlayEvent) {
        if(prepareStartPlayEvent.getMaxPriority()<1000){
            return;
        }
        if (NetworkUtils.getNetworkState() != NetworkUtils.WIFI) {
            dialog = DialogUtil.showWifiDialog(getActivity(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prepareStartPlayEvent.setMaxPriority(999);
                    getEventBus().post(prepareStartPlayEvent);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                }
            });
            getEventBus().cancelEventDelivery(prepareStartPlayEvent);
        }
    }

    @Subscribe
    public void onActivityResumeEvnt(ActivityResumeEvent activityResumeEvent) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Subscribe
    public void onActivityPauseEvent(ActivityPauseEvent activityPauseEvent) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        dismissDialog();
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

}
