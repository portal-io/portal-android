package com.whaley.biz.launcher.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.launcher.activitys.AdvertiseActivity;
import com.whaley.biz.launcher.activitys.MainActivity;
import com.whaley.biz.launcher.event.DownloadSpalshImageEvent;
import com.whaley.biz.launcher.event.ModifyFestivalEvent;
import com.whaley.biz.launcher.event.SplashEventController;
import com.whaley.biz.launcher.event.UpdateEvent;
import com.whaley.biz.launcher.festival.FestivalManager;
import com.whaley.biz.launcher.interactor.GetFestival;
import com.whaley.biz.launcher.model.FestivalModel;
import com.whaley.biz.launcher.util.ComputeUtil;
import com.whaley.biz.launcher.util.SharedPreferencesUtil;
import com.whaley.biz.launcher.view.BaseSplashView;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;
import com.whaley.core.utils.PermissionUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/9/5.
 */

public class BaseSplashPresenter<VIEW extends BaseSplashView> extends BasePagePresenter<VIEW> {

    @UseCase
    GetFestival getFestival;

    private Disposable disposable;

    public BaseSplashPresenter(VIEW view) {
        super(view);
    }

    private static final String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        SplashEventController.init();
        if (!PermissionUtil.requestPermission(getUIView().getAttachActivity(), permissions, 1)) {
            if (getUIView() != null) {
                getUIView().hideCover();
            }
            Router.getInstance().buildExecutor("/download/service/downloadDirInit").excute();
            Router.getInstance().buildExecutor("/hybrid/service/checkH5Zip").excute();
        }
        getFestival();
        EventController.postEvent(new DownloadSpalshImageEvent());
        EventController.postEvent(new UpdateEvent());
    }

    public void startActivity() {
        String path = SharedPreferencesUtil.getSplashUrl(CommonConstants.KEY_BOOT);
        if (!TextUtils.isEmpty(path) && ComputeUtil.isShowSplash(CommonConstants.KEY_BOOT)) {
            SharedPreferencesUtil.setSplashTime(CommonConstants.KEY_BOOT, System.currentTimeMillis());
            AdvertiseActivity.launch(getStater(), path);
            getStater().finish();
            ((Activity) getStater()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            startMainActivity();
        }
    }


    public void startMainActivity() {
        MainActivity.launch(getStater());
        if (getStater() != null) {
            getStater().finish();
            ((Activity) getStater()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void getFestival(){
        FestivalManager.getInstance().setFestivalModel(null);
        dispose();
        disposable = getFestival.buildUseCaseObservable(null).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<FestivalModel>(){
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull FestivalModel festivalModel) {
                        FestivalManager.getInstance().setFestivalModel(festivalModel);
                        if(festivalModel!=null){
                            EventController.postEvent(new ModifyFestivalEvent(true));
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        FestivalManager.getInstance().setFestivalModel(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Router.getInstance().buildExecutor("/download/service/downloadDirInit").excute();
                    Router.getInstance().buildExecutor("/hybrid/service/checkH5Zip").excute();
                } else {
                }
                if (getUIView() != null) {
                    getUIView().hideCover();
                }
                break;
        }
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

}
