package com.whaley.biz.program.playersupport.component.uploadplaycount;

import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.interactor.UploadPlayCount;
import com.whaley.biz.program.model.CountModel;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/9/19 21:35.
 */

public class UpLoadPlayCountController extends BaseController {

    protected UploadPlayCount uploadPlayCount;

    public UpLoadPlayCountController() {
        uploadPlayCount = new UploadPlayCount();
    }

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        uploadPlayCount(videoPreparedEvent.getPlayData());
    }

    public void uploadPlayCount(PlayData playData) {
        UploadPlayCount.Param param = new UploadPlayCount.Param();
        param.setItemid(playData.getId());
        String programType = "recorded";
        String videoType = "2d";
        int playerType = playData.getType();
        switch (playerType) {
            case PlayerType.TYPE_LIVE:
                programType = "live";
                videoType = "vr";
                break;
            case PlayerType.TYPE_MORETV_2D:
                programType = "moretv_movie";
                break;
            case PlayerType.TYPE_MORETV_TV:
                programType = "moretv_tv";
                break;
            case PlayerType.TYPE_3D:
                videoType = "3d";
                break;
            case PlayerType.TYPE_PANO:
                videoType = "vr";
                break;
        }
        param.setProgramType(programType);
        param.setVideoType(videoType);
        uploadPlayCount.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new ErrorReturnFunction())
                .subscribe();
    }


    static class ErrorReturnFunction implements Function<Throwable, CMSResponse<CountModel>> {
        @Override
        public CMSResponse<CountModel> apply(@NonNull Throwable throwable) throws Exception {
            return new CMSResponse<CountModel>();
        }
    }


}
