package com.whaley.biz.program.playersupport.component.splitplayer.loading;

import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.playerui.component.common.loading.LoadingUIAdapter;
import com.whaley.biz.program.R;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitLoadingUIAdapter extends LoadingUIAdapter {

    TextView tvNormalLoadingText2;
    ImageView iv_normal_loading2;
    View  viewBlocking2;

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_split_loading, parent, false);
        return view;
    }

    protected void startAnim() {
        super.startAnim();
        AdditiveAnimator.animate(iv_normal_loading2)
                .rotationBy(360)
                .setDuration(700)
                .setRepeatCount(ValueAnimator.INFINITE)
                .start();
    }

    protected void cancelAnim() {
        super.cancelAnim();
        AdditiveAnimator.cancelAnimations(iv_normal_loading2);
    }

    public void updateLoadingText(String text) {
        super.updateLoadingText(text);
        tvNormalLoadingText2.setText(text);
    }

    @Override
    protected void onViewCreated(View view) {
        iv_normal_loading = (ImageView) view.findViewById(R.id.iv_normal_loading);
        tvNormalLoadingText = (TextView) view.findViewById(R.id.tv_normal_loading_text);
        iv_normal_loading2 = (ImageView) view.findViewById(R.id.iv_normal_loading2);
        tvNormalLoadingText2 = (TextView) view.findViewById(R.id.tv_normal_loading_text2);
        viewBlocking= view.findViewById(R.id.view_blocking);
        viewBlocking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewBlocking2= view.findViewById(R.id.view_blocking2);
        viewBlocking2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
