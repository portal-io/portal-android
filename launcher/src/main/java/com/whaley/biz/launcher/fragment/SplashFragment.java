package com.whaley.biz.launcher.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.snailvr.manager.R;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.launcher.presenter.SplashPresenter;
import com.whaley.biz.launcher.view.SplashView;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageLoaderCallback;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.wvrplayer.vrplayer.external.event.callback.PanoPictureListener;
import com.whaley.wvrplayer.vrplayer.external.view.VRPictureView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
 * Author: qxw
 * Date:2017/8/8
 * Introduction:
 */

public class SplashFragment extends BaseMVPFragment<SplashPresenter> implements SplashView, PanoPictureListener {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.iv_logo_text)
    ImageView ivLogoText;
    @BindView(R.id.enter)
    ImageButton enter;
    @BindView(R.id.iv_channel_logo)
    ImageView ivChannelLogo;
    @BindView(R.id.pano_layout)
    RelativeLayout panoLayout;
    @BindView(R.id.pano_view)
    VRPictureView panoView;
    @BindView(R.id.cover)
    View cover;

    private AnimatorSet set;

    private Disposable disposable;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_splash_layout;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        panoView.setPictureViewPreparedListener(this);
        measure();
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

    private void cancelAnim() {
        if (set != null) {
            List<Animator> animatorList = set.getChildAnimations();
            Iterator<Animator> iterator = animatorList.iterator();
            while (iterator.hasNext()) {
                Animator animator = iterator.next();
                animator.removeAllListeners();
                if (animator.isRunning())
                    animator.cancel();
            }
            set.removeAllListeners();
            set.cancel();
        }
    }

    private void measure() {
        ViewTreeObserver vto = panoLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (panoLayout == null)
                    return;
                panoLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startAnim();
            }
        });
    }

    private void startAnim() {
        cancelAnim();
        List<Animator> animatorList = new ArrayList<>();
        ObjectAnimator ivLogoAnim1 = ObjectAnimator.ofFloat(panoLayout, "translationY", DisplayUtil.screenH, 0);
        ivLogoAnim1.setInterpolator(new DecelerateInterpolator());
        ivLogoAnim1.setDuration(600);
        animatorList.add(ivLogoAnim1);
        ViewCompat.setAlpha(ivLogo, 0f);
        ObjectAnimator ivLogoAnim2 = ObjectAnimator.ofFloat(ivLogo, "alpha", 0f, 1f);
        ivLogoAnim2.setDuration(600);
        animatorList.add(ivLogoAnim2);
        ViewCompat.setAlpha(ivLogoText, 0f);
        ObjectAnimator ivLogoTextAnim = ObjectAnimator.ofFloat(ivLogoText, "alpha", 0f, 1f);
        ivLogoTextAnim.setStartDelay(600);
        ivLogoTextAnim.setDuration(400);
        animatorList.add(ivLogoTextAnim);
        ValueAnimator enterAnimator = ValueAnimator.ofFloat(0, 100);
        enterAnimator.setStartDelay(1000);
        enterAnimator.setDuration(2000);
        animatorList.add(enterAnimator);
        set = new AnimatorSet();
        set.playTogether(animatorList);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                getPresenter().onAmintionEnd();
            }
        });
        set.start();
    }

    @Override
    public void onDestroyView() {
        cancelAnim();
        dispose();
        if (panoView != null) {
            panoView.setPictureViewPreparedListener(null);
        }
        super.onDestroyView();
    }

    @OnClick(R.id.enter)
    public void onClick() {
        getPresenter().startActivity();
    }

    @Override
    public void loadLogoImage() {
        ivChannelLogo.setVisibility(View.VISIBLE);
        ImageLoader.with(this).load(getPresenter().getRepository().getLogoImageUrl())
                .medium()
                .placeholder(0)
                .error(0)
                .fitCenter()
                .into(ivChannelLogo, null, new ImageLoaderCallback() {
                    @Override
                    public void onSuccess(String s, Bitmap bitmap, File file) {

                    }

                    @Override
                    public void onFailue(Throwable throwable) {

                    }

                    @Override
                    public void onProgressChanged(double v, double v1) {

                    }
                });
    }


    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
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
