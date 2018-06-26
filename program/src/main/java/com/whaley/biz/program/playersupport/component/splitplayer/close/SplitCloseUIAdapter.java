package com.whaley.biz.program.playersupport.component.splitplayer.close;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.core.utils.DisplayUtil;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by dell on 2017/11/1.
 */

public class SplitCloseUIAdapter extends ControlUIAdapter<SplitCloseController>{

    boolean isLive;

    private ImageButton btn_back;

    private boolean isAnimFinish;

    public SplitCloseUIAdapter(boolean isLive){
        this.isLive = isLive;
    }

    @Override
    public void show(boolean anim) {
        startAnim(0f,0f,1f);
    }

    @Override
    public void hide(boolean anim) {
        startAnim(0f,getRootView().getMeasuredHeight(),0f);
    }

    @Override
    protected View initView(ViewGroup parent) {
        if(isLive){
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_split_live_close,parent,false);
        }else{
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_split_close,parent,false);
        }
    }

    @Override
    protected void onViewCreated(View view) {
        btn_back = (ImageButton) view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onBackClick();
            }
        });
        if (getPlayData() != null && getPlayData().getType() == ProgramConstants.TYPE_PLAY_LIVE) {
            btn_back.setImageResource(R.drawable.btn_live_title_close_selector);
        }else {
           doMeasure();
        }
    }

    private PlayData getPlayData() {
        if (getController()!=null&&getController().getPlayerController() != null)
            return getController().getPlayerController().getRepository().getCurrentPlayData();
        return null;
    }

    private void doMeasure(){
        if(isAnimFinish)
            return;
        ViewTreeObserver vto = btn_back.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (btn_back == null)
                    return;
                btn_back.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                doAnim();
            }
        });
    }

    private void doAnim(){
        int screenH = DisplayUtil.getScreenHeight();
        int height = btn_back.getMeasuredHeight();
        final int top = btn_back.getTop();
        final int translationY = screenH - height - top;
        AdditiveAnimator.cancelAnimations(btn_back);
        AdditiveAnimator.animate(btn_back).topMargin(top+translationY).setDuration(500).addEndAction(new AdditiveAnimator.AnimationEndListener() {
            @Override
            public void onAnimationEnd(boolean wasCancelled) {
                isAnimFinish = true;
                if(wasCancelled){
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)btn_back.getLayoutParams();
                    layoutParams.topMargin = top+translationY;
                    btn_back.setLayoutParams(layoutParams);
                }
            }
        }).start();
    }

}
