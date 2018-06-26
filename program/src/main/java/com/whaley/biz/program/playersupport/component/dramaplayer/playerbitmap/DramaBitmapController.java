package com.whaley.biz.program.playersupport.component.dramaplayer.playerbitmap;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.playersupport.component.playerbitmap.PlayerBitmapController;
import com.whaley.core.utils.StrUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/11/23.
 */

public class DramaBitmapController extends PlayerBitmapController {

    @Override
    public void onVideoPreparedEvent(VideoPreparedEvent videoPreparedEvent){
        PlayData playData = videoPreparedEvent.getPlayData();
        String centerImage = playData.getCustomData(PlayerDataConstants.CENTER_IMAGE);
        String radius = playData.getCustomData(PlayerDataConstants.CENTER_IMAGE_RADIUS);
        if (!StrUtil.isEmpty(centerImage)) {
            setCenterPic(centerImage, radius);
        }
    }

    private void setCenterPic(String centerImage, final String radius){
        disposeGetOps();
        opsDisposable = Observable.just(centerImage)
                .map(new Function<String, BottomOverlayModel>() {
                    @Override
                    public BottomOverlayModel apply(@NonNull String picUrl) throws Exception {
                        BottomOverlayModel bottomOverlayModel = new BottomOverlayModel();
                        float scale;
                        try {
                            scale = Float.valueOf(radius);
                        } catch (NumberFormatException e) {
                            scale = 0;
                        }
                        bottomOverlayModel.setScale(scale);
                        int index = picUrl.indexOf("/zoom/");
                        if (index > 0) {
                            picUrl = picUrl.substring(0, index);
                        }
                        bottomOverlayModel.setBitmapUrl(picUrl);
                        return bottomOverlayModel;
                    }
                })
                .map(new Function<BottomOverlayModel, BottomOverlayModel>() {
                    @Override
                    public BottomOverlayModel apply(@NonNull BottomOverlayModel bottomOverlayModel) throws Exception {
                        Bitmap bitmap = Glide.with(getContext())
                                .load(bottomOverlayModel.getBitmapUrl())
                                .asBitmap()
                                .skipMemoryCache(true)
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get();
                        bottomOverlayModel.setBitmap(bitmap);
                        return bottomOverlayModel;
                    }
                })
                .onErrorReturn(new Function<Throwable, BottomOverlayModel>() {
                    @Override
                    public BottomOverlayModel apply(@NonNull Throwable throwable) throws Exception {
                        BottomOverlayModel bottomOverlayModel = new BottomOverlayModel();
                        return bottomOverlayModel;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BottomOverlayModel>() {
                    @Override
                    public void accept(@NonNull BottomOverlayModel bottomOverlayModel) throws Exception {
                        getPlayerController().setBottomOverlayView(bottomOverlayModel.getScale(), bottomOverlayModel.getBitmap());
                    }
                });
    }

}
