package com.whaley.biz.program.playersupport.component.localplayer.renderbox;

import android.animation.Animator;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.biz.program.playersupport.widget.renderbox.RenderTypeCheckBox;
import com.whaley.core.utils.DisplayUtil;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by yangzhi on 2017/8/23.
 */

public class RenderBoxUIAdapter extends ControlUIAdapter<RenderBoxController>{

    ViewStub viewStub;

    RenderTypeCheckBox renderTypeCheckBox;

    boolean isRenderBoxOnShow;

    @Override
    public void show(boolean anim) {
        if (renderTypeCheckBox == null) {
            return;
        }
        if(!isRenderBoxOnShow){
            return;
        }
        AdditiveAnimator.cancelAnimations(renderTypeCheckBox);
        AdditiveAnimator.animate(renderTypeCheckBox)
                .alpha(1f)
                .addStartAction(new Runnable() {
                    @Override
                    public void run() {
                        renderTypeCheckBox.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    @Override
    public void hide(boolean anim) {
        if (renderTypeCheckBox == null)
            return;
        AdditiveAnimator.cancelAnimations(renderTypeCheckBox);
        AdditiveAnimator.animate(renderTypeCheckBox)
                .alpha(0f)
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        renderTypeCheckBox.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    @Override
    public View getRootView() {
        return renderTypeCheckBox == null ? super.getRootView() : renderTypeCheckBox;
    }

    @Override
    protected View initView(ViewGroup parent) {
        viewStub = new ViewStub(parent.getContext(), R.layout.layout_player_local_renderbox);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = DisplayUtil.convertDIP2PX(45);
        layoutParams.rightMargin = DisplayUtil.convertDIP2PX(86);
        layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        viewStub.setLayoutParams(layoutParams);
        return viewStub;
    }

    @Override
    protected void onViewCreated(View view) {

    }

    public void toggleRenderBox(){
        if(isRenderBoxOnShow){
            hideRenderBox();
        }else {
            showRenderBox();
        }
    }

    public void showRenderBox(){
        isRenderBoxOnShow = true;
        if (renderTypeCheckBox == null) {
            return;
        }
        show(true);


    }

    public void hideRenderBox(){
        isRenderBoxOnShow = false;
        if(renderTypeCheckBox == null)
            return;
        hide(true);
    }

    public void setRenderBoxVisible(){
        if(renderTypeCheckBox == null){
            renderTypeCheckBox = (RenderTypeCheckBox) viewStub.inflate();
            renderTypeCheckBox.setListener(new RenderTypeCheckBox.OnRenderTypeSelectListener() {
                @Override
                public void onSelected(String renderType, String formatRenderType) {
                    getController().onRenderTypeSelected(renderType,formatRenderType);
                }
            });
            renderTypeCheckBox.setVisibility(View.GONE);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if(renderTypeCheckBox!=null) {
            AdditiveAnimator.cancelAnimations(renderTypeCheckBox);
        }
    }
}
