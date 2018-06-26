package com.whaley.biz.playerui.component.common.loading;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.playerui.R;
import com.whaley.biz.playerui.component.BaseUIAdapter;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by YangZhi on 2017/8/3 17:27.
 */

public class LoadingUIAdapter extends BaseUIAdapter<LoadingController> {

    protected TextView tvNormalLoadingText;
    protected ImageView iv_normal_loading;
    protected View  viewBlocking;

    private ObjectAnimator animator;

    public void show() {
        getRootView().setVisibility(View.VISIBLE);
        startAnim();
    }

    public void hide() {
        getRootView().setVisibility(View.GONE);
        cancelAnim();
    }

    protected void startAnim() {
        cancelAnim();
        AdditiveAnimator.animate(iv_normal_loading)
                .rotationBy(360)
                .setDuration(700)
                .setRepeatCount(ValueAnimator.INFINITE)
                .start();
    }

    protected void cancelAnim() {
        AdditiveAnimator.cancelAnimations(iv_normal_loading);
    }

    public void updateLoadingText(String text) {
        tvNormalLoadingText.setText(text);
    }

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading, parent, false);
        return view;
    }

    @Override
    protected void onViewCreated(View view) {
        iv_normal_loading = (ImageView) view.findViewById(R.id.iv_normal_loading);
        tvNormalLoadingText = (TextView) view.findViewById(R.id.tv_normal_loading_text);
        viewBlocking= view.findViewById(R.id.view_blocking);
        viewBlocking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void destroy() {
        hide();
    }


}
