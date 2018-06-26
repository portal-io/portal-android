package com.whaley.biz.playerui.component.common.networkcompute;

import android.net.TrafficStats;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BufferingEvent;
import com.whaley.biz.playerui.event.BufferingOffEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.NetworkSpeedEvent;
import com.whaley.biz.playerui.event.PollingEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.exception.NetworkPlayerException;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.NetworkUtils;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/3 21:23.
 */

public class NetworkController extends BaseController {

    /**
     * 上一次获取的应用网络大小
     */
    private long lastTotalRxBytes = 0;

    /**
     * 当前网络流量
     */
    private String currentNetWorkSpeedStr = "正在加载...0kb/s";

    /**
     * 缓冲总量
     */
    private int totalBufferingAmount;


    private java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#.##");


    private boolean isRegistPolling;

    @Subscribe
    public void onPolling(PollingEvent pollingEvent) {
        if (isRegistPolling) {
            caculateNetWorkSpeed();
        }
    }

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        emitNetworkSpeed();
        isRegistPolling = true;
    }

    @Subscribe(sticky = true)
    public void onError(ErrorEvent errorEvent) {
        isRegistPolling = false;
    }

    @Subscribe(sticky = true)
    public void onVideoPreprepared(VideoPreparedEvent videoPreparedEvent) {
        ModuleEvent moduleEvent = new ModuleEvent("totalBufferingAmount", totalBufferingAmount);
        emitEvent(moduleEvent);
        isRegistPolling = false;
    }

    @Subscribe
    public void onBuffering(BufferingEvent bufferingEvent) {
        totalBufferingAmount = 0;
        emitNetworkSpeed();
        isRegistPolling = true;
    }

    @Subscribe
    public void onBufferingOff(BufferingOffEvent bufferingOffEvent) {
        ModuleEvent moduleEvent = new ModuleEvent("totalBufferingAmount", totalBufferingAmount);
        emitEvent(moduleEvent);
        isRegistPolling = false;
    }

    /**
     * 计算当前网络速度
     */
    private void caculateNetWorkSpeed() {
        long totalRxBytes = getTotalRxBytes();
        if (lastTotalRxBytes == 0) {
            lastTotalRxBytes = totalRxBytes;
            emitNetworkSpeed();
            return;
        }
        long currentSpeed = totalRxBytes - lastTotalRxBytes;
        currentSpeed = getPlayerController().getTcpSpeed();
        totalBufferingAmount += currentSpeed;
        currentNetWorkSpeedStr = formatNetWorkSpeed(currentSpeed);
        lastTotalRxBytes = totalRxBytes;
        emitNetworkSpeed();
    }

    private void emitNetworkSpeed() {
        if (!getEventBus().hasSubscriberForEvent(NetworkSpeedEvent.class)) {
            return;
        }
        NetworkSpeedEvent networkSpeedEvent = new NetworkSpeedEvent();
        networkSpeedEvent.setSpeedStr(currentNetWorkSpeedStr);
        emitEvent(networkSpeedEvent);
    }


    /**
     * 获得应用总网络下载量
     *
     * @return
     */
    private long getTotalRxBytes() {
        return TrafficStats.getUidRxBytes(getContext().getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getTotalRxBytes();
    }


    /**
     * 格式化当前网络速度的UI展示文字
     *
     * @param currentSpeed
     */
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
