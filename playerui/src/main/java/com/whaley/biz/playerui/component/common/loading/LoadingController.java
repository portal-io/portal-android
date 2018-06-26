package com.whaley.biz.playerui.component.common.loading;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BufferingEvent;
import com.whaley.biz.playerui.event.BufferingOffEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.NetworkSpeedEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.core.debug.logger.Log;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/3 17:26.
 */

public class LoadingController extends BaseController<LoadingUIAdapter> {

    private java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#.##");

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        if(getPlayerController()!=null&&getPlayerController().getRepository()!=null&&getPlayerController().getRepository().isOnBuffering()){
            getUIAdapter().show();
            long currentSpeed = getPlayerController().getTcpSpeed();
            getUIAdapter().updateLoadingText(formatNetWorkSpeed(currentSpeed));
        }
    }

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        getUIAdapter().show();
    }

    @Subscribe
    public void onError(ErrorEvent errorEvent){
        getUIAdapter().hide();
    }

    @Subscribe
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent){
        getUIAdapter().hide();
    }

    @Subscribe
    public void onBuffering(BufferingEvent bufferingEvent){
        getUIAdapter().show();
    }

    @Subscribe
    public void onBufferingOff(BufferingOffEvent bufferingOffEvent){
        getUIAdapter().hide();
    }

    @Subscribe
    public void NetworkSpeedChanged(NetworkSpeedEvent event){
        getUIAdapter().updateLoadingText(event.getSpeedStr());
    }

    @Override
    public void registEvents() {
        super.registEvents();
        if(getPlayerController().getRepository().isOnBuffering()){
            if(getUIAdapter()!=null) {
                getUIAdapter().show();
            }
        }
    }

    @Override
    public void unRegistEvents() {
        super.unRegistEvents();
        if(getUIAdapter()!=null) {
            getUIAdapter().hide();
        }
    }

    private String formatNetWorkSpeed(long currentSpeed) {
        StringBuilder sb = new StringBuilder();
        sb.append("正在加载...");
        if (currentSpeed < 1024) {
            sb.append(currentSpeed);
            sb.append("byte/s");
        } else if (currentSpeed < 1024 * 1024) {
            sb.append(decimalFormat.format(1f * currentSpeed / 1024));
            sb.append("KB/s");
        } else {
            sb.append(decimalFormat.format(1f * currentSpeed / (1024 * 1024)));
            sb.append("MB/s");
        }
        return sb.toString();
    }

}
