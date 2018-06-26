package com.whaley.biz.playerui.component.simpleplayer.resetscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.biz.playerui.R;
import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;

/**
 * Created by YangZhi on 2017/8/2 16:02.
 */

public class ResetUIAdapter extends ControlUIAdapter<ResetController>{

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_control_reset,parent,false);
        return view;
    }

    @Override
    protected void onViewCreated(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onResetClick();
            }
        });
        view.setVisibility(View.GONE);
    }

    @Override
    public void show(boolean anim) {
        if(getRootView().getVisibility() != View.VISIBLE)
            return;
        startAnim(0,0,1f);
    }

    @Override
    public void hide(boolean anim) {
        if(getRootView().getVisibility() != View.VISIBLE)
            return;
        startAnim(getRootView().getMeasuredWidth(),0,0f);
    }

    public void hide(){
        getRootView().setVisibility(View.GONE);
    }

    public void show(){
        getRootView().setVisibility(View.VISIBLE);
    }

}
