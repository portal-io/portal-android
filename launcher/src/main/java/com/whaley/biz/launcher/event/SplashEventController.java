package com.whaley.biz.launcher.event;

import android.app.Application;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.launcher.api.RecommendAPI;
import com.whaley.biz.launcher.interactor.GetRecommend;
import com.whaley.biz.launcher.model.RecommendAreasBean;
import com.whaley.biz.launcher.model.RecommendModel;
import com.whaley.biz.launcher.model.UpdateModel;
import com.whaley.biz.launcher.model.responce.RecommendResponse;
import com.whaley.biz.launcher.util.SharedPreferencesUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageLoaderCallback;
import com.whaley.core.image.OnResourceLoadCallback;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/9/5.
 */

public class SplashEventController implements CommonConstants {

    Disposable disposable;
    private Disposable upateDisposable;
    private volatile static SplashEventController instance;

    GetRecommend getRecommend;


    private SplashEventController() {
        getRecommend = new GetRecommend();
        EventController.regist(this);
    }

    public static SplashEventController init() {
        if (instance == null) {
            synchronized (SplashEventController.class) {
                if (instance == null) {
                    instance = new SplashEventController();
                }
            }
        }
        return instance;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateEvent event) {
        if (upateDisposable != null) {
            upateDisposable.dispose();
        }
        SharedPreferencesUtil.deleteUpdateData();
        upateDisposable = new UpdateUIObserver<String>(null) {
            @Override
            public void onNext(String updateModelString) {
                SharedPreferencesUtil.setUpDateData(updateModelString);
            }

            @Override
            public void onError(Throwable e) {
            }
        };
        Router.getInstance().buildExecutor("/setting/service/checkUpdate").putObjParam(upateDisposable).notTransParam().excute();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DownloadSpalshImageEvent event) {
        if (disposable != null) {
            disposable.dispose();
        }
        getRecommend.buildUseCaseObservable(RecommendAPI.HOME_WELCOME_AD)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RecommendResponse>() {
                    @Override
                    public void onNext(@NonNull RecommendResponse recommendResponse) {
                        List<RecommendAreasBean> recommendAreasBeans = recommendResponse.getList();
                        if (recommendAreasBeans == null || recommendAreasBeans.size() == 0) {
                            SharedPreferencesUtil.deleteAllSplashLastUpdate();
                        }
                        boolean isBoot = false;
                        boolean isPoster = false;
                        boolean isContent = false;
                        for (RecommendAreasBean recommendAreasBean : recommendAreasBeans) {
                            final String keyString;
                            switch (recommendAreasBean.getType()) {
                                case 13:
                                    keyString = KEY_BOOT;
                                    isBoot = true;
                                    break;
                                case 14:
                                    keyString = KEY_POSTER;
                                    isPoster = true;
                                    break;
                                case 15:
                                    keyString = KEY_CONTENT;
                                    isContent = true;
                                    break;
                                default:
                                    continue;
                            }
                            RecommendModel recommendModel = getRecommendModel(recommendAreasBean);
                            if (recommendModel == null) {
                                SharedPreferencesUtil.deleteSplashLastUpdate(keyString);
                                continue;
                            }
                            SharedPreferencesUtil.setSplashData(keyString, GsonUtil.getGson().toJson(recommendModel));
                            SharedPreferencesUtil.setSplashType(keyString, recommendModel.getShowRate());
                            final String realUrl = recommendModel.getNewPicUrl();
                            String lastUrl = SharedPreferencesUtil.getSplashUrl(keyString);
                            if (TextUtils.isEmpty(realUrl)) {
                                Log.d("splash image realUrl is empty");
                                SharedPreferencesUtil.deleteSplashLastUpdate(keyString);
                                continue;
                            }
                            if (lastUrl.equals(realUrl)) {
                                Log.d("splash image realUrl");
                                continue;
                            }
                            final String newPath = realUrl.split("/zoom/")[0];
                            if (lastUrl.equals(newPath)) {
                                Log.d("splash image newPath");
                                continue;
                            }
                            final String key = keyString;
                            SharedPreferencesUtil.setSplashTime(key, 0);
                            SharedPreferencesUtil.setSplashLastUpdate(key, realUrl);
//                            ImageLoader.with(AppContextProvider.getInstance().getContext()).load(realUrl).get(new OnResourceLoadCallback<Bitmap>() {
//                                @Override
//                                public void onResourceLoaded(Bitmap bitmap) {
//                                    SharedPreferencesUtil.setSplashTime(key, 0);
//                                    SharedPreferencesUtil.setSplashLastUpdate(key, realUrl);
//                                }
//                            });
//                            String newUrl = SharedPreferencesUtil.getSplashUrl(keyString);
//                            if (TextUtils.isEmpty(newUrl) || !realUrl.equals(newUrl)){
//                                SharedPreferencesUtil.setSplashTime(key, 0);
//                                SharedPreferencesUtil.setSplashLastUpdate(key, newPath);
//                            }
                        }
                        if (!isBoot) {
                            SharedPreferencesUtil.deleteSplashLastUpdate(KEY_BOOT);
                        }
                        if (!isPoster) {
                            SharedPreferencesUtil.deleteSplashLastUpdate(KEY_POSTER);
                        }
                        if (!isContent) {
                            SharedPreferencesUtil.deleteSplashLastUpdate(KEY_CONTENT);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private RecommendModel getRecommendModel(RecommendAreasBean recommendAreasBean) {
        RecommendModel recommendModel = null;
        if (recommendAreasBean != null && recommendAreasBean.getRecommendElements() != null && recommendAreasBean.getRecommendElements().size() > 0) {
            recommendModel = recommendAreasBean.getRecommendElements().get(0);
        }
        return recommendModel;
    }

}
