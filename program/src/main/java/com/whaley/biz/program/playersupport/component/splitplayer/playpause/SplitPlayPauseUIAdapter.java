package com.whaley.biz.program.playersupport.component.splitplayer.playpause;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;
import com.whaley.biz.program.R;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitPlayPauseUIAdapter extends ControlUIAdapter<SplitPlayPauseController> {

    ImageButton btn_playpause;

    boolean isLocal;

    public SplitPlayPauseUIAdapter(boolean isLocal) {
        this.isLocal = isLocal;
    }

    @Override
    public void show(boolean anim) {
//        startAnim(0,0,1f);
    }

    @Override
    public void hide(boolean anim) {
//        startAnim(0,-getRootView().getMeasuredHeight()-((ViewGroup.MarginLayoutParams)getRootView().getLayoutParams()).topMargin,0f);
    }

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_split_playpause, parent, false);
        return view;
    }

    @Override
    protected void onViewCreated(View view) {
        btn_playpause = (ImageButton) view.findViewById(R.id.btn_start_pause);
        btn_playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onStartPauseClick();
            }
        });
        if (isLocal) {
            btn_playpause.setVisibility(View.GONE);
        }
    }

    public void changeStartPauseBtn(boolean isStarted) {
        int imageRes = isStarted ? R.drawable.bg_btn_player_split_pause_selector : R.drawable.bg_btn_player_split_start_selector;
        btn_playpause.setImageResource(imageRes);
    }

    public void hidePause() {
        btn_playpause.setVisibility(View.GONE);
    }

    public void showPause() {
        btn_playpause.setVisibility(View.VISIBLE);
    }
}
