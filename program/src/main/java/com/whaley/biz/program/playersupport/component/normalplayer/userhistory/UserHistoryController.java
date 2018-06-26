package com.whaley.biz.program.playersupport.component.normalplayer.userhistory;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.model.State;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.AddUserHistory;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.model.ProgramDramaDetailModel;
import com.whaley.biz.program.model.eventmodel.HistoryModel;
import com.whaley.biz.program.model.response.StringResponse;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/9/12
 * Introduction:
 */

public class UserHistoryController extends BaseController {
    public static final String PLAY_STATUS_START = "0";
    public static final String PLAY_STATUS_IN = "1";
    public static final String PLAY_STATUS_END = "2";
    AddUserHistory addUserHistory;

    boolean isDarma;

    public UserHistoryController(boolean isDarma) {
        this.isDarma = isDarma;
        addUserHistory = new AddUserHistory(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Subscribe(sticky = true)
    public void onCompletedEvent(CompletedEvent completedEvent) {
        PlayData playData = completedEvent.getPlayData();
        updataUserHistory(PLAY_STATUS_END, playData);
    }

//    @Subscribe(sticky = true)
//    public void onPausedEvent(PausedEvent pausedEvent) {
//        PlayData playData = pausedEvent.getPlayData();
//        updataUserHistory(PLAY_STATUS_IN, playData);
//    }

    @Subscribe(sticky = true)
    public void onVideoPreparedEvent(VideoPreparedEvent videoPreparedEvent) {
        PlayData playData = videoPreparedEvent.getPlayData();
        updataUserHistory(PLAY_STATUS_START, playData);
    }

    private void updataUserHistory(String playStatus, PlayData playData) {
        AddUserHistory.Param param = new AddUserHistory.Param();
        if (isDarma) {
            ProgramDramaDetailModel programDramaDetailModel = playData.getCustomData(PlayerDataConstants.DRAMA_INFO);
            param.setProgramCode(programDramaDetailModel.getCode());
            param.setProgramType(ProgramConstants.TYPE_DYNAMIC);
            param.setPlayStatus(playStatus);
        } else {
            String programType = getUserHistoryType(playData.getType());
            if (StrUtil.isEmpty(programType)) {
                return;
            }
            ProgramDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.PROGRAM_INFO);
            param.setProgramCode(programDetailModel.getCode());
            param.setProgramType(programType);
            param.setPlayStatus(playStatus);
            param.setTotalPlayTime(String.valueOf(getPlayerController().getRepository().getDuration()));
            switch (playStatus) {
                case PLAY_STATUS_IN:
                    param.setPlayTime(String.valueOf(getPlayerController().getCurrentProgress()));
                    HistoryModel historyModel = new HistoryModel(param.getProgramCode(), param.getPlayTime(), param.getTotalPlayTime(), programDetailModel.getVideoType(), programDetailModel.getCurEpisode());
                    BaseEvent baseEvent = new BaseEvent("history_update", GsonUtil.getGson().toJson(historyModel));
                    EventController.postEvent(baseEvent);
                    break;
                case PLAY_STATUS_START:
                    if (playData.getProgress() != 0) {
                        param.setPlayTime(String.valueOf(playData.getProgress()));
                    }
                    break;
            }
        }
        addUserHistory.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new ErrorReturnFunction())
                .subscribe();
    }

    private String getUserHistoryType(int type) {
        String programType = "";
//        if (SCREEM_TYPE_3.equals(getBIFullScreen())) {
//            return programType;
//        }
        switch (type) {
            case ProgramConstants.TYPE_PLAY_LIVE:
                break;
            case ProgramConstants.TYPE_PLAY_PANO:
            case ProgramConstants.TYPE_PLAY_3D:
                programType = "program";
                break;
            case ProgramConstants.TYPE_PLAY_MORETV_TV:
            case ProgramConstants.TYPE_PLAY_MORETV_2D:
                programType = "moretv";
                break;
            case ProgramConstants.TYPE_PLAY_LOCALVIDEO:
                break;
        }
        return programType;
    }


    @Override
    protected void onDestory() {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        int state = getPlayerController().getState().getCurrentState();
        if (playData != null && (state == State.STATE_PAUSED || State.STATE_STARTED == state)) {
            updataUserHistory(PLAY_STATUS_IN, playData);
        }
        super.onDestory();
    }

    private static class ErrorReturnFunction implements Function<Throwable, StringResponse> {
        @Override
        public StringResponse apply(@NonNull Throwable throwable) throws Exception {
            return new StringResponse();
        }
    }
}
