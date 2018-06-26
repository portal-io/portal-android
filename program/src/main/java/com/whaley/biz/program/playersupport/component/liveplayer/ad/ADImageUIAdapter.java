package com.whaley.biz.program.playersupport.component.liveplayer.ad;

import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.biz.program.playersupport.model.ADModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageLoaderCallback;
import com.whaley.core.image.ImageRequest;

import java.io.File;
import java.util.List;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Author: qxw
 * Date:2018/3/26
 * Introduction:
 */

public class ADImageUIAdapter extends BaseUIAdapter<ADImageController> {

    ViewStub viewStub;
    LinearLayout linearLayout;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageRequest.RequestManager requestManager;
    ColorMatrixColorFilter grayColorFilter;
    @Override
    public void destroy() {
        if (imageView1 != null) {
            AdditiveAnimator.cancelAnimations(imageView1);
        }
        if (imageView2 != null) {
            AdditiveAnimator.cancelAnimations(imageView2);
        }
        if (imageView3 != null) {
            AdditiveAnimator.cancelAnimations(imageView3);
        }
        if(getRootView()!=null) {
            AdditiveAnimator.cancelAnimations(getRootView());
        }
        requestManager = null;
    }

    @Override
    public View getRootView() {
        return linearLayout == null ? super.getRootView() : linearLayout;
    }

    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), R.layout.layout_live_ad);
        viewStub.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return viewStub;
    }

    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;
    }
    private void checkInflatedView() {
        if (linearLayout == null) {
            linearLayout = (LinearLayout) viewStub.inflate();
            imageView1 = (ImageView) linearLayout.findViewById(R.id.iv_image1);
            imageView2 = (ImageView) linearLayout.findViewById(R.id.iv_image2);
            imageView3 = (ImageView) linearLayout.findViewById(R.id.iv_image3);
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onClickListener(0);
                }
            });
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onClickListener(1);
                }
            });
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onClickListener(2);
                }
            });
        }
    }

    public void updateAD(List<ADModel> date) {
        requestManager = ImageLoader.with(getActivity());
        checkInflatedView();
        int size = date.size();
        if (size > 3) {
            size = 3;
        }
        for (int i = 0; i < size; i++) {
            ADModel adModel = date.get(i);
            ImageView imageView;
            switch (i) {
                case 0:
                    imageView = imageView1;

                    break;
                case 1:
                    imageView = imageView2;
                    break;
                default:
                    imageView = imageView3;
                    break;
            }
            requestManager.load(adModel.getPic()).placeholder(0).setScaleType(ImageView.ScaleType.CENTER_CROP).into(imageView);
        }
        showAnimations(imageView1, 2000);
    }

    public void showAnimationsStart() {
        showUI();
        showAnimations(imageView1, 300);
    }

    private void showAnimations(final ImageView imageView, int startDelayTime) {
        AdditiveAnimator.cancelAnimations(imageView);
        AdditiveAnimator.animate(imageView)
                .setStartDelay(startDelayTime)
                .rotation(5)
                .setDuration(166)
                .then()
                .rotation(0)
                .setDuration(166)
                .then()
                .rotation(-5)
                .then()
                .setDuration(166)
                .then()
                .rotation(0)
                .setDuration(166)
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        if (!wasCancelled) {
                            if (imageView1 == imageView) {
                                if (getController().getDateSize() > 1) {
                                    showAnimations(imageView2, 333);
                                } else {
                                    getController().adAnimationEnd();
                                }
                            } else if (imageView2 == imageView) {
                                if (getController().getDateSize() > 2) {
                                    showAnimations(imageView3, 333);
                                } else {
                                    getController().adAnimationEnd();
                                }
                            } else {
                                getController().adAnimationEnd();
                            }
                        }
                    }
                }).start();
    }

    public void showUI() {
        AdditiveAnimator.cancelAnimations(getRootView());
        AdditiveAnimator.animate(getRootView())
                .translationX(0f)
                .alpha(1f)
                .start();
    }

    public void hideUI() {
        AdditiveAnimator.cancelAnimations(getRootView());
        AdditiveAnimator.animate(getRootView())
                .translationX(getRootView().getMeasuredWidth())
                .alpha(0f)
                .start();
    }

    public void showCamera() {
        setColorFilter(false);
    }

    public void hideCamera() {
        setColorFilter(true);
    }

    private void setColorFilter(boolean isEnabled){
        imageView1.setEnabled(isEnabled);
        imageView2.setEnabled(isEnabled);
        imageView3.setEnabled(isEnabled);
        if(!isEnabled){
            if(grayColorFilter==null){
                ColorMatrix cm = new ColorMatrix();
                cm.setSaturation(0); // 设置饱和度
                grayColorFilter = new ColorMatrixColorFilter(cm);
            }
            imageView1.setColorFilter(grayColorFilter);
            imageView2.setColorFilter(grayColorFilter);
            imageView3.setColorFilter(grayColorFilter);
        }else {
            imageView1.setColorFilter(null);
            imageView2.setColorFilter(null);
            imageView3.setColorFilter(null);
        }



    }
}
