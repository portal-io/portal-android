package com.whaley.biz.program.playersupport.component.liveplayer.liveclose;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.whaley.biz.playerui.component.common.init.InitUIAdapter;
import com.whaley.biz.program.R;

/**
 * Created by yangzhi on 2017/8/9.
 */

public class LiveCloseUIAdapter extends InitUIAdapter<LiveCloseController>{

    ImageButton btnClose;

    @Override
    public void show(boolean anim) {
        super.show(anim);
        startAnim(0f,0f,1f);
    }

    @Override
    public void hide(boolean anim) {
        super.hide(anim);
        startAnim(0f,-getRootView().getMeasuredHeight(),0f);
    }

    @Override
    public void showInit(boolean anim) {
        show(anim);
    }

    @Override
    public void hideInit(boolean anim) {

    }


    @Override
    public void changeVisibleOnComplete() {
        show(true);
    }

    @Override
    public void changeVisibleOnError() {
        show(true);
    }

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_player_close,parent,false);
        return view;
    }

    @Override
    protected void onViewCreated(View view) {
        btnClose = (ImageButton) view;

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onCloseClick();
            }
        });
    }
}
