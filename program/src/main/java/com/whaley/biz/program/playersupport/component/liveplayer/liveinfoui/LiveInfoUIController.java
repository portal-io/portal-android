package com.whaley.biz.program.playersupport.component.liveplayer.liveinfoui;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.SwicthInitBgVisibleEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.playersupport.event.PlayCountGetEvent;
import com.whaley.biz.program.playersupport.event.ShowLiveInfoEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/9/8 19:36.
 */

public class LiveInfoUIController extends BaseController<LiveInfoUIAdapter>{

    private LiveInfoViewModel liveInfoViewModel;

    private String playCountStr;

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent){
        PlayData playData = videoPreparedEvent.getPlayData();
        LiveDetailsModel liveDetailsModel = playData.getCustomData(PlayerDataConstants.LIVE_INFO);
        convertLiveInfoViewModel(liveDetailsModel);
    }

    @Subscribe(sticky = true)
    public void onErrorEvent(ErrorEvent event){
        getUIAdapter().hide();
    }

    @Subscribe(sticky = true)
    public void onCompletedEvent(CompletedEvent event){
        getUIAdapter().hide();
    }

    @Subscribe(sticky = true)
    public void onPreparingEvent(PreparingEvent event){
        getUIAdapter().hide();
    }

    @Subscribe(sticky = true)
    public void onSwitchInitBgVisibleEvent(SwicthInitBgVisibleEvent event){
        if(event.isVisible()) {
            getUIAdapter().hide();
        }
    }

    public void convertLiveInfoViewModel(LiveDetailsModel liveDetailsModel) {
        LiveInfoViewModel liveInfoViewModel = new LiveInfoViewModel();
        liveInfoViewModel.setTvTitle(liveDetailsModel.getDisplayName());
        liveInfoViewModel.setTvContent(liveDetailsModel.getDescription());
        liveInfoViewModel.setTvLocation(liveDetailsModel.getAddress());
        long time = System.currentTimeMillis() - liveDetailsModel.getBeginTime();
        liveInfoViewModel.setTvPlayerTime("已开播" + time / 1000 / 60 + "分钟");
        if(playCountStr==null&&liveDetailsModel.getStat()!=null){
            playCountStr = getCuttingCount(liveDetailsModel.getStat().getPlayCount())+"正在观看";
        }
        liveInfoViewModel.setPlayCount(playCountStr);
        this.liveInfoViewModel = liveInfoViewModel;
    }

    @Subscribe
    public void onPlayCountGetEvent(PlayCountGetEvent event) {
        playCountStr = getCuttingCount(event.getPlayCount())+"正在观看";
        if(this.liveInfoViewModel!=null){
            this.liveInfoViewModel.setPlayCount(playCountStr);
        }
    }

    private String getCuttingCount(int playCount) {
        if (playCount >= 10000) {
            int million = playCount / 10000;
            int thousand = (playCount % 10000) / 1000;
            if (thousand > 0) {
                return million + "." + thousand + "万";
            } else {
                return million + "万";
            }
        } else {
            return playCount + "";
        }
    }

    @Subscribe
    public void onShowLiveInfoEvent(ShowLiveInfoEvent event) {
        if (liveInfoViewModel != null) {
            getUIAdapter().show(liveInfoViewModel);
        }
    }

    @Subscribe(priority = 10)
    public void onBackPressEvent(BackPressEvent backPressEvent) {
        if (getUIAdapter() != null && getUIAdapter().isOnShow()) {
            getUIAdapter().hide();
            getEventBus().cancelEventDelivery(backPressEvent);
        }
    }
}
