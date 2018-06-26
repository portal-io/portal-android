package com.whaley.biz.program.playersupport.component.splitplayer.initpause;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.R;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by dell on 2017/11/9.
 */

public class SplitInitPauseUIAdapter extends BaseUIAdapter<SplitInitPauseController> {

    private boolean isLock;

    public SplitInitPauseUIAdapter(boolean isLock) {
        this.isLock = isLock;
    }

    public void show() {
        getRootView().setVisibility(View.VISIBLE);
    }

    public void hide() {
        getRootView().setVisibility(View.GONE);
    }

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_split_pause, parent, false);
        return view;
    }

    @Override
    protected void onViewCreated(View view) {
        if (isLock) {
            hidePause();
        }
    }

    @Override
    public void destroy() {
        hide();
    }

    public void hidePause() {
        AdditiveAnimator.cancelAnimations(getRootView());
        AdditiveAnimator.animate(getRootView()).alpha(0f).setDuration(0).start();
    }

    public void showPause() {
        AdditiveAnimator.cancelAnimations(getRootView());
        AdditiveAnimator.animate(getRootView()).alpha(1f).setDuration(0).start();
    }
}
