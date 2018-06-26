package com.whaley.biz.playerui.component.simpleplayer.bottomcontrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.whaley.biz.playerui.R;
import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

/**
 * Created by YangZhi on 2017/8/2 13:29.
 */

public class BottomControlUIAdapter<CONTROLLER extends BottomControlController> extends ControlUIAdapter<CONTROLLER> {

    protected SeekBar sk_progress;

    protected ImageButton btn_start_pause;

    protected TextView tv_current;

    protected TextView tv_total;

    protected ImageButton btn_switch_screen;

    protected ViewStub vsRight;

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getRootViewId(), parent, false);
        return view;
    }


    protected int getRootViewId() {
        return R.layout.layout_bottom_controls;
    }

    @Override
    protected void onViewCreated(View view) {
        sk_progress = (SeekBar) view.findViewById(R.id.sk_progress);
        btn_start_pause = (ImageButton) view.findViewById(R.id.btn_start_pause);
        tv_current = (TextView) view.findViewById(R.id.tv_current);
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        vsRight = (ViewStub) view.findViewById(R.id.vs_right);

        initRight(vsRight);

        btn_start_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onStartPauseClick();
            }
        });


        sk_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                getController().onSeekChanging(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                getController().onStartSeekTouch();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                getController().onSeekChanged(seekBar.getProgress());
            }
        });
    }

    protected void initRight(ViewStub vsRight) {
        View layoutRight = vsRight.inflate();

        btn_switch_screen = (ImageButton) layoutRight.findViewById(R.id.btn_switch_screen);

        btn_switch_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onSwitchScreenClick();
            }
        });
//        Router.getInstance().buildExecutor("/launch/service/getfestival")
//                .notTransCallbackData()
//                .notTransParam()
//                .callback(new Executor.Callback<Boolean>() {
//                    @Override
//                    public void onCall(Boolean aBoolean) {
//
//                    }
//
//                    @Override
//                    public void onFail(Executor.ExecutionError executionError) {
//
//                    }
//                })
//                .excute();
    }

    public void showActivityIcon() {
        btn_switch_screen.setSelected(true);
    }

    public void hideActivityIcon() {
        btn_switch_screen.setSelected(false);
    }

    protected ImageButton onCreateSwitchScreenButton(View view) {
        return (ImageButton) view.findViewById(R.id.btn_switch_screen);
    }

    public ImageButton onCreateOtherPlayerButton(View view) {
        return (ImageButton) view.findViewById(R.id.btn_other_player);
    }

    public void changeSeekMax(long max) {
        sk_progress.setMax((int) max);
    }

    public void changeSeekProgress(long progress) {
        sk_progress.setProgress((int) progress);
    }

    public void changeSeekSecondProgress(long secondProgress) {
        sk_progress.setSecondaryProgress((int) secondProgress);
    }

    public void changeStartPauseBtn(boolean isStarted) {
        if (btn_start_pause != null) {
            int imageRes = isStarted ? R.drawable.bg_btn_player_pause_selector : R.drawable.bg_btn_player_start_selector;
            btn_start_pause.setImageResource(imageRes);
        }
    }

    public void updateCurrentTimeText(String text) {
        tv_current.setText(text);
    }

    public void updateTotalTimeText(String text) {
        tv_total.setText(text);
    }

    @Override
    public void show(boolean anim) {
        startAnim(0f, 0f, 1f);
    }

    @Override
    public void hide(boolean anim) {
        startAnim(0f, getRootView().getMeasuredHeight(), 0f);
    }


    public SeekBar getSk_progress() {
        return sk_progress;
    }

    public ImageButton getBtn_start_pause() {
        return btn_start_pause;
    }

    public TextView getTv_current() {
        return tv_current;
    }

    public TextView getTv_total() {
        return tv_total;
    }

    public ImageButton getBtn_switch_screen() {
        return btn_switch_screen;
    }


}

