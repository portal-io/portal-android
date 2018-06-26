package com.whaley.biz.program.playersupport.component.cameralist;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.R;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by YangZhi on 2017/9/19 18:26.
 */

public class CameraGuideUIAdapter extends BaseUIAdapter<CameraGuideController> {

    ViewStub viewStub;

    View inflatedView;

    final int layoutResouceId;

    public CameraGuideUIAdapter(int layoutResouceId){
        this.layoutResouceId = layoutResouceId;
    }

    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), layoutResouceId);
        viewStub.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return viewStub;
    }

    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;
    }

    private void checkInflatedView() {
        if (inflatedView == null) {
            inflatedView = viewStub.inflate();
            inflatedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onGuideClick();
                }
            });
            inflatedView.setVisibility(View.GONE);
        }
    }

    @Override
    public View getRootView() {
        return inflatedView == null ? super.getRootView() : inflatedView;
    }

    public void show() {
        checkInflatedView();
        AdditiveAnimator.cancelAnimations(inflatedView);
        AdditiveAnimator.animate(inflatedView)
                .alpha(1f)
                .addStartAction(new Runnable() {
                    @Override
                    public void run() {
                        if (inflatedView != null) {
                            inflatedView.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .start();
    }

    public void hide() {
        if (inflatedView == null) {
            return;
        }
        AdditiveAnimator.cancelAnimations(inflatedView);
        AdditiveAnimator.animate(inflatedView)
                .alpha(0f)
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        if (inflatedView != null) {
                            inflatedView.setVisibility(View.GONE);
                        }
                    }
                })
                .start();
    }

    @Override
    public void destroy() {
        if(inflatedView!=null) {
            AdditiveAnimator.cancelAnimations(inflatedView);
        }
    }
}
