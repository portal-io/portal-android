package com.whaley.biz.program.playersupport.component.liveplayer.switchlotterydanmu;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.PollingEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.program.api.PlayerApi;
import com.whaley.biz.program.model.DmBoxSwitchModel;
import com.whaley.biz.program.playersupport.event.LotterySwitchEvent;
import com.whaley.biz.program.model.response.DanMuBoxSwitchResponse;
import com.whaley.core.debug.logger.Log;
import com.whaleyvr.core.network.http.HttpManager;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yangzhi on 2017/8/14.
 */

public class SwitchLotteryDanMuController extends BaseController{

    static final int POOLING_SWITCH_DURATION = 60;

    final PlayerApi playerApi;

    private String sid;

    private int duration;

    private Disposable disposable;

    private boolean isRegistPolling;

    public SwitchLotteryDanMuController(){
        playerApi = HttpManager.getInstance().getRetrofit(PlayerApi.class).create(PlayerApi.class);
    }

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        unregistPolling();
    }

    @Subscribe(sticky = true)
    public void onVideoPreparedEvent(VideoPreparedEvent videoPreparedEvent){
        sid = videoPreparedEvent.getPlayData().getId();
        toGetSwitchData(sid);
        registPolling();
    }

    @Subscribe
    public void onPolling(PollingEvent pollingEvent){
        if(!isRegistPolling)
            return;
        duration++;
        if (duration >= POOLING_SWITCH_DURATION) {
            toGetSwitchData(sid);
            duration = 0;
        }
    }


    private void toGetSwitchData(String sid){
        disposable = playerApi.getLiveConfigByCode(sid)
                .map(new ResponseFunction<DanMuBoxSwitchResponse, DanMuBoxSwitchResponse>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DanMuBoxSwitchResponse>() {
                    @Override
                    public void accept(@NonNull DanMuBoxSwitchResponse danMuBoxSwitchResponse) throws Exception {
                        DmBoxSwitchModel dmBoxSwitchModel = danMuBoxSwitchResponse.getData();

                        if (dmBoxSwitchModel.getDanmu() == 0) {
                            closeDanMu();
                        } else if (dmBoxSwitchModel.getDanmu() == 1) {
                            openDanMu();
                        }
                        if (dmBoxSwitchModel.getLottery() == 0) {
                            closeLottery();
                        } else if (dmBoxSwitchModel.getLottery() == 1) {
                            openLottery();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(throwable,"获取 弹幕宝箱开关 失败");
                    }
                });
    }

    private void closeDanMu(){
        emitEvent("/program/event/closedanmu", null);
    }

    private void openDanMu(){
        emitEvent("/program/event/opendanmu", null);
    }

    private void closeLottery(){
        LotterySwitchEvent lotterySwitchEvent = new LotterySwitchEvent();
        lotterySwitchEvent.setEnable(false);
        emitStickyEvent(lotterySwitchEvent);
    }

    private void openLottery(){
        LotterySwitchEvent lotterySwitchEvent = new LotterySwitchEvent();
        lotterySwitchEvent.setEnable(true);
        emitStickyEvent(lotterySwitchEvent);
    }

    private void registPolling(){
        isRegistPolling= true;
    }

    private void unregistPolling(){
        isRegistPolling = false;
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        if(disposable!=null){
            disposable.dispose();
        }
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        onDispose();
    }
}
