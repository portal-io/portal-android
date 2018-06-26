package com.whaley.biz.program.playersupport.component.dramaplayer.uploadplaycount;

import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.UploadPlayCount;
import com.whaley.biz.program.model.CountModel;
import com.whaley.biz.program.playersupport.component.uploadplaycount.UpLoadPlayCountController;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/9/19 21:35.
 */

public class DramaUpLoadPlayCountController extends UpLoadPlayCountController {


    public DramaUpLoadPlayCountController() {
        super();
    }

    @Override
    public void uploadPlayCount(PlayData playData) {
        UploadPlayCount.Param param = new UploadPlayCount.Param();
        param.setItemid(playData.getId());
        param.setProgramType(ProgramConstants.TYPE_DYNAMIC);
        param.setVideoType("vr");
        uploadPlayCount.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new ErrorReturnFunction())
                .subscribe();
    }

    //    @Override
//    public void uploadPlayCount(PlayData playData) {
//        UploadPlayCount.Param param = new UploadPlayCount.Param();
//        param.setItemid(playData.getId());
//        param.setProgramType(ProgramConstants.TYPE_DYNAMIC);
//        param.setVideoType("vr");
//        uploadPlayCount.buildUseCaseObservable(param)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .onErrorReturn(new ErrorReturnFunction())
//                .subscribe();
//    }


    static class ErrorReturnFunction implements Function<Throwable, CMSResponse<CountModel>> {
        @Override
        public CMSResponse<CountModel> apply(@NonNull Throwable throwable) throws Exception {
            return new CMSResponse<CountModel>();
        }
    }


}
