package com.whaley.biz.program.playersupport.component.playerbitmap;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.api.PlayerApi;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.model.response.OpsResponse;
import com.whaley.core.repository.RemoteRepository;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yangzhi on 2017/8/20.
 */

public class PlayerBitmapController extends BaseController {

    PlayerApi playerApi;

    protected Disposable opsDisposable;

    Disposable bgDisposable;

    public PlayerBitmapController() {
        playerApi = RemoteRepository.getInstance().obtainService(PlayerApi.class);
    }

    @Subscribe
    public void onVideoPreparedEvent(VideoPreparedEvent videoPreparedEvent){
        PlayData playData = videoPreparedEvent.getPlayData();
        boolean isPublic = playData.getBooleanCustomData(PlayerDataConstants.CURRENT_CAMERA_IS_PUBLIC);
        boolean isFootball = playData.getBooleanCustomData(PlayerDataConstants.FOOTBALL_TYPE);
        if(!isFootball||isPublic) {
            switch (playData.getType()) {
                case PlayerType.TYPE_PANO:
                    setVROpsPic(playData);
                    break;
                case PlayerType.TYPE_LIVE:
                    setLiveOpsPic(playData);
                    break;
            }
        }
        String backgroundImage = playData.getCustomData(PlayerDataConstants.BACKGROUND_FINAL_IMAGE);
        if (!StrUtil.isEmpty(backgroundImage)) {
            setBackgroundPic(backgroundImage);
        }
    }

    private void subscribeGetOps(Observable<OpsResponse> observable) {
        disposeGetOps();
        opsDisposable = observable
                .map(new ResponseFunction<OpsResponse, OpsResponse>())
                .map(new Function<OpsResponse, BottomOverlayModel>() {
                    @Override
                    public BottomOverlayModel apply(@NonNull OpsResponse opsResponse) throws Exception {
                        BottomOverlayModel bottomOverlayModel = new BottomOverlayModel();
                        float scale;
                        try {
                            scale = Float.valueOf(opsResponse.getData().getRadius());
                        } catch (NumberFormatException e) {
                            scale = 0;
                        }
                        bottomOverlayModel.setScale(scale);
                        String picUrl = opsResponse.getData().getCenterPic();
                        if (picUrl == null)
                            return bottomOverlayModel;
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


    private void setVROpsPic(PlayData playData) {
        subscribeGetOps(playerApi.getProgramOps(playData.getId()));
    }

    private void setLiveOpsPic(PlayData playData) {
        subscribeGetOps(playerApi.getLiveOps(playData.getId()));
    }


    private void setBackgroundPic(String url) {
        disposeBgOps();
        bgDisposable = Observable.just(url)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull String url) throws Exception {
                        Bitmap bitmap = Glide.with(getContext())
                                .load(url)
                                .asBitmap()
                                .skipMemoryCache(true)
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get();
                        return bitmap;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {
                        getPlayerController().setBackgroundImage(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getPlayerController().setBackgroundImage(null);
                    }
                });
    }

    protected void disposeGetOps() {
        if (opsDisposable != null) {
            opsDisposable.dispose();
            opsDisposable = null;
        }
    }

    private void disposeBgOps() {
        if (bgDisposable != null) {
            bgDisposable.dispose();
            bgDisposable = null;
        }
    }


    @Override
    protected void onDispose() {
        super.onDispose();
        disposeGetOps();
        disposeBgOps();
    }


    @Override
    protected void onDestory() {
        super.onDestory();
        onDispose();
    }

    public static class BottomOverlayModel {

        private String bitmapUrl;

        private float scale;

        private Bitmap bitmap;

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public float getScale() {
            return scale;
        }

        public void setBitmapUrl(String bitmapUrl) {
            this.bitmapUrl = bitmapUrl;
        }

        public String getBitmapUrl() {
            return bitmapUrl;
        }
    }
}
