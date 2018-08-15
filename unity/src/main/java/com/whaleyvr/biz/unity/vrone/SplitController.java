package com.whaleyvr.biz.unity.vrone;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.whaley.biz.playerui.utils.StringFormatUtil;
import com.whaleyvr.biz.unity.util.HermesConstants;
import com.whaleyvr.biz.unity.IServerMessage;
import com.whaleyvr.biz.unity.model.MediaInfo;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import xiaofei.library.hermes.Hermes;
import xiaofei.library.hermes.HermesService;

/**
 * Created by dell on 2017/12/5.
 */

public class SplitController implements PollingController.PollingCallback, EventListener{

    Context mContext;

    PollingController splitPollingController;

    SplitView splitView;

    boolean isHide;

    long unTouchDuration;

    MediaInfo mediaInfo;

    boolean isSeekTouched;

    boolean isStarted;

    private Handler handler;

    boolean isPrepared;

    boolean isLive;

    Disposable disposable;

    public SplitController(Context context, SplitView splitView, boolean isLive){
        this.mContext = context;
        this.splitView = splitView;
        this.isLive = isLive;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void setData(MediaInfo mediaInfo){
        this.mediaInfo = mediaInfo;
    }

    public void onViewCreated(){
        initData();
        SplitPlayerTool.getInstance().setEventListener(this);
        splitPollingController = new PollingController(this);
        splitPollingController.startPolling();
    }

    private void initData(){
        if (splitView != null) {
            splitView.setTitle(mediaInfo.getTitle());
            long duration = mediaInfo.getDuration();
            long progress = (long)(mediaInfo.getDuration()* mediaInfo.getProgress());
            splitView.updateTotalTimeText(StringFormatUtil.formatTime(duration));
            splitView.updateCurrentTimeText(StringFormatUtil.formatTime(progress));
            splitView.changeSeekMax((int)duration);
            splitView.changeSeekProgress((int)progress);
            splitView.updateDefinitionText(getDefinitionText(mediaInfo.getMovieQuality()));
            if(isLive()){
                splitView.setPlayCount(getCuttingCount(mediaInfo.getPlayCount()));
            }
        }
    }

    private String getDefinitionText(String definition){
        String text;
        switch (definition) {
            case "SD":
            case "SDB":
            case "TDA":
            case "TDB":
                text = "超清";
                break;
            case "HD":
                text = "原画";
                break;
            case "ST":
            case "SDA":
                text = "高清";
                break;
            default:
                text = "";
                break;
        }
        return text;
    }

    public void onDestroy(boolean isBack){
        if(splitPollingController != null){
            splitPollingController.stopPolling();
        }
        SplitPlayerTool.getInstance().setEventListener(null);
        cancelToastTask();
        SplitPlayerTool.getInstance().destroy(isBack);
    }

    @Override
    public void onPolling() {
        unTouchDuration++;
        if (unTouchDuration >= 5) {
            emitHide();
        }
    }

    public void onBlankClick(){
        if(isHide){
            emitShow();
        }else{
            emitHide();
        }
    }

    public void onStartPauseClick() {
        resetDuration();
        if (isStarted) {
            SplitPlayerTool.getInstance().pause();
        } else {
            SplitPlayerTool.getInstance().start();
        }
    }

    public void onSeekChanging(long progress) {
        resetDuration();
        splitView.updateCurrentTimeText(StringFormatUtil.formatTime(progress));
    }

    public void onStartSeekTouch() {
        isSeekTouched = true;
    }

    public void onSeekChanged(long progress) {
        resetDuration();
        isSeekTouched = false;
        SplitPlayerTool.getInstance().changeProgress(String.valueOf(progress));
    }

    public boolean isSeekTouched() {
        return isSeekTouched;
    }

    private boolean isLive(){
        return isLive;
    }

    public void onSwitchDefinitionClick() {
        resetDuration();
        SplitPlayerTool.getInstance().nextDefinition();
    }

    public void onRetryClick(){
        resetDuration();
        SplitPlayerTool.getInstance().reStart();
    }

    public void onHotSpotClick(int type){
        SplitPlayerTool.getInstance().clickHotSpot(type);
    }

    public void onBackClick(){
        onDestroy(true);
    }

    public void onExitClick(){
        try {
            IServerMessage message = Hermes.getInstanceInService(HermesService.HermesService0.class, IServerMessage.class);
            message.sendEvent(HermesConstants.EVENT_EXIT_PLAY, "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        onDestroy(true);
    }

    public void onSplitClick(){
        try {
            IServerMessage message = Hermes.getInstanceInService(HermesService.HermesService0.class, IServerMessage.class);
            message.sendEvent(HermesConstants.EVENT_SWITCH_FULLSCREEN, "", mediaInfo.getSid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        onDestroy(false);
    }

    private void emitShow(){
        if(isHide) {
            isHide = false;
            if (splitView != null) {
                splitView.show();
            }
            resetDuration();
        }
    }

    private void emitHide(){
        if(!isHide) {
            isHide = true;
            if (splitView != null) {
                splitView.hide();
            }
        }
    }

    private void resetDuration() {
        unTouchDuration = 0;
    }

    private void delayToastTask(){
        cancelToastTask();
        disposable = Observable.just(1).delay(2, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<Integer>(){
            @Override
            public void onNext(@NonNull Integer integer) {

            }
            @Override
            public void onError(@NonNull Throwable e) {

            }
            @Override
            public void onComplete() {
                if(splitView != null){
                    splitView.clearToast();
                }
            }
        });
    }

    private void cancelToastTask(){
        if(disposable!=null){
            disposable.dispose();
            disposable = null;
        }
    }

    private String getCuttingCount(int playCount) {
        if (playCount >= 10000) {
            int million = playCount / 10000;
            int thousand = (playCount % 10000) / 1000;
            if (thousand > 0) {
                return million + "." + thousand + "万人";
            } else {
                return million + "万人";
            }
        } else {
            return playCount + "";
        }
    }

    //==============================================================================================

    @Override
    public void onStarted() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                isStarted = true;
                if(splitView != null){
                    splitView.changeStartPauseBtn(true);
                }
            }
        });
    }

    @Override
    public void onPaused() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                isStarted = false;
                if(splitView != null){
                    splitView.changeStartPauseBtn(false);
                }
            }
        });
    }

    @Override
    public void onBufferingUpdate(final long progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(splitView != null){
                    splitView.changeSeekSecondProgress((int)progress);
                }
            }
        });
    }

    @Override
    public void onBuffering(final String speed) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                delayToastTask();
                if(splitView != null){
                    splitView.showToast(speed, null);
                }
            }
        });
    }

    @Override
    public void onBufferingOff() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                cancelToastTask();
                if(splitView != null){
                    splitView.clearToast();
                }
            }
        });
    }

    @Override
    public void onDefinitionChange(final String definition) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(splitView != null){
                    splitView.updateDefinitionText(getDefinitionText(definition));
                }
            }
        });
    }

    @Override
    public void onCompleted() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(splitView != null){
                    splitView.onCompleted();
                    splitView.changeSeekProgress(0);
                    splitView.updateCurrentTimeText(StringFormatUtil.formatTime(0));
                }
            }
        });
    }

    @Override
    public void onPrepared(final long duration, final long progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                isPrepared = true;
                isStarted = true;
                if(splitView != null){
                    splitView.changeWidgetClickable(true);
                    splitView.changeStartPauseBtn(true);
                    splitView.changeSeekMax((int)duration);
                    splitView.changeSeekProgress((int)progress);
                    splitView.updateTotalTimeText(StringFormatUtil.formatTime(duration));
                    splitView.updateCurrentTimeText(StringFormatUtil.formatTime(progress));
                }
            }
        });
    }

    @Override
    public void onProgress(final long progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(splitView != null && !isSeekTouched()){
                    splitView.changeSeekProgress((int)progress);
                    splitView.updateCurrentTimeText(StringFormatUtil.formatTime(progress));
                }
            }
        });
    }

    @Override
    public void onPreparing() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                isPrepared = false;
                if(splitView != null){
                    splitView.changeWidgetClickable(false);
                }
            }
        });
    }

    @Override
    public void onError(final String errorMsg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                isPrepared = false;
                cancelToastTask();
                if(splitView != null){
                    splitView.showToast(errorMsg, null);
                    splitView.changeWidgetClickable(false);
                }
            }
        });
    }

    @Override
    public void onToast(final String toastMsg, final String toastColor, final boolean isTemporary) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(isTemporary){
                    delayToastTask();
                }else{
                    cancelToastTask();
                }
                if(splitView != null){
                    splitView.showToast(toastMsg, toastColor);
                }
            }
        });
    }

    @Override
    public void onClearToast() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                cancelToastTask();
                if(splitView != null){
                    splitView.clearToast();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onDestroy(true);
            }
        });
    }

}
