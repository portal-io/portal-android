package com.whaley.biz.launcher.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.snailvr.manager.R;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.biz.launcher.presenter.SplashLoginPresenter;
import com.whaley.biz.launcher.view.BaseSplashView;
import com.whaley.wvrplayer.vrplayer.external.event.callback.PanoPictureListener;
import com.whaley.wvrplayer.vrplayer.external.view.VRPictureView;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/9/5.
 */

public class SplashLoginFragment extends BaseMVPFragment<SplashLoginPresenter> implements BaseSplashView, PanoPictureListener {

    private Disposable disposable;

    @BindView(R.id.pano_view)
    VRPictureView panoView;
    @BindView(R.id.cover)
    View cover;

    @OnClick({R.id.iv_leapfrog, R.id.btn_login, R.id.btn_register, R.id.start_qq, R.id.start_weixin, R.id.start_weibo})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_leapfrog:
                getPresenter().onLeapfrog();
                break;
            case R.id.btn_login:
                getPresenter().onLogin();
                break;
            case R.id.btn_register:
                getPresenter().onRegister();
                break;
            case R.id.start_qq:
                getPresenter().loginQQ();
                break;
            case R.id.start_weixin:
                getPresenter().loginWeixin();
                break;
            case R.id.start_weibo:
                getPresenter().loginWeibo();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_splash_login;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        panoView.setPictureViewPreparedListener(this);
    }

    @Override
    public void onViewPrepared() {
        dispose();
        disposable = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
                InputStream fis = null;
                try {
                    fis = getActivity().getAssets().open("splash_pano.jpg");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (fis != null) {
                    e.onNext(BitmapFactory.decodeStream(fis));
                    e.onComplete();
                } else {
                    e.onError(new Exception("InputStream Open Error"));
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Bitmap>() {
                    @Override
                    public void onNext(@NonNull Bitmap bitmap) {
                        if (panoView != null) {
                            panoView.loadImage(bitmap, true);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    public void onDestroyView() {
        dispose();
        if (panoView != null) {
            panoView.setPictureViewPreparedListener(null);
        }
        super.onDestroyView();
    }

    @Override
    public void showCover() {
        cover.setBackgroundResource(R.color.color2);
    }

    @Override
    public void hideCover() {
        cover.setBackgroundResource(0);
    }

}
