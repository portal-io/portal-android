package com.whaley.biz.program.playersupport.component.liveplayer.playcount;

import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.PollingEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.interactor.GetPlayStatisic;
import com.whaley.biz.program.model.CountModel;
import com.whaley.biz.program.playersupport.event.PlayCountGetEvent;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yangzhi on 2017/9/8.
 */

public class PlayCountController extends BaseController {

    /**
     * 30秒刷新一次播放次数
     */
    private static final int REFRESH_PLAYCOUNT_DURATION = 30;

    private int duration;

    private boolean isAttached;

    private GetPlayStatisic getPlayStatisic;


    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        dettach();
    }

    @Subscribe
    public void onError(ErrorEvent errorEvent) {
        dettach();
    }

    @Subscribe
    public void onCompeleted(CompletedEvent completedEvent) {
        dettach();
    }

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        attach();
    }

    @Subscribe
    public void onPolling(PollingEvent pollingEvent) {
        if (!isAttached) {
            return;
        }
        duration++;
        if (duration >= REFRESH_PLAYCOUNT_DURATION) {
            duration = 0;
            getPlayCount();
        }
    }

    private void attach() {
        duration = 0;
        isAttached = true;
        getPlayCount();
    }

    private void dettach() {
        isAttached = false;
        duration = 0;
    }


    private void getPlayCount() {
        if (getPlayStatisic == null) {
            getPlayStatisic = new GetPlayStatisic();
        }
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        getPlayStatisic.buildUseCaseObservable(playData.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<CMSResponse<CountModel>>() {
                    @Override
                    public void onFinalError(Throwable e) {

                    }

                    @Override
                    public void onStatusError(int status, String message) {

                    }

                    @Override
                    public void onNoDataError() {

                    }

                    @Override
                    public void onNext(@NonNull CMSResponse<CountModel> countModelCMSResponse) {
                        int playCount = countModelCMSResponse.getData().getPlayCount();
                        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                        if(playData != null) {
                            playData.putCustomData(PlayerDataConstants.LIVE_PLAY_COUNT, playCount);
                        }
                        PlayCountGetEvent playCountGetEvent = new PlayCountGetEvent();
                        playCountGetEvent.setPlayCount(playCount);
                        emitEvent(playCountGetEvent);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
