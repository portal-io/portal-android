package com.whaley.biz.playerui.component.common.networkchanged;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.InitEvent;
import com.whaley.biz.playerui.event.NetworkChangedEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.exception.NetworkPlayerException;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.utils.NetworkUtils;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/7 20:37.
 */

public class NetworkChangedController extends BaseController{

    int onVideoPreparedNetworkState;


    @Subscribe(priority = Integer.MAX_VALUE)
    public void onPrepareStart(PrepareStartPlayEvent prepareStartPlayEvent){
        if(!NetworkUtils.isNetworkAvailable()){
            getPlayerController().setError(new NetworkPlayerException());
            getEventBus().cancelEventDelivery(prepareStartPlayEvent);
        }
    }

    @Subscribe
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent){
        onVideoPreparedNetworkState = NetworkUtils.getNetworkState();
        registNetworkChanged();
    }

    @Subscribe
    public void onError(ErrorEvent errorEvent){
        unRegistNetworkChanged();
    }

    @Subscribe(priority = Integer.MAX_VALUE)
    public void onCompleted(CompletedEvent completedEvent){
        int currentNetworkState = NetworkUtils.getNetworkState();
        if(currentNetworkState!=onVideoPreparedNetworkState){
            getPlayerController().setError(new NetworkPlayerException());
            getEventBus().cancelEventDelivery(completedEvent);
        }
        unRegistNetworkChanged();
    }

    @Subscribe
    public void onInit(InitEvent initEvent){
        unRegistNetworkChanged();
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        unRegistNetworkChanged();
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        onDispose();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(0);
            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(1);
            if(mobNetInfo != null && wifiNetInfo != null && !mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                dispatchState(NetworkUtils.NONE);
            } else if(wifiNetInfo != null && wifiNetInfo.isConnected()) {
                dispatchState(NetworkUtils.WIFI);
            } else {
                dispatchState(NetworkUtils.MOBILE);
            }
        }
    };

    private boolean isRegistNetWorkChanged;


    private void registNetworkChanged(){
        if(!isRegistNetWorkChanged) {
            IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            AppContextProvider.getInstance().getContext().registerReceiver(broadcastReceiver, filter);
            isRegistNetWorkChanged = true;
        }
    }

    private void unRegistNetworkChanged(){
        if(isRegistNetWorkChanged) {
            AppContextProvider.getInstance().getContext().unregisterReceiver(broadcastReceiver);
            isRegistNetWorkChanged = false;
        }
    }

    private void dispatchState(int state){
        NetworkChangedEvent networkChangedEvent = new NetworkChangedEvent();
        networkChangedEvent.setNetworkState(state);
        emitEvent(networkChangedEvent);
    }
}
