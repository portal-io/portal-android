package com.whaley.biz.program.playersupport.component.dramaplayer.bottomui;

import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.playersupport.component.normalplayer.normalbottomui.NormalBottomUIAdapter;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

/**
 * Created by dell on 2017/11/14.
 */

public class DramaBottomUIAdapter extends NormalBottomUIAdapter {

    @Override
    protected void initRight(ViewStub vsRight) {
        vsRight.setLayoutResource(R.layout.layout_player_drama_bottom_right);
        View layoutRight = vsRight.inflate();
        btn_switch_screen = (ImageButton) layoutRight.findViewById(com.whaley.biz.playerui.R.id.btn_switch_screen);
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
//                        btn_switch_screen.setSelected(aBoolean);
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

    @Override
    protected void onViewCreated(View view) {
        sk_progress = (SeekBar) view.findViewById(R.id.sk_progress);
        btn_start_pause = (ImageButton) view.findViewById(com.whaley.biz.playerui.R.id.btn_start_pause);
        tv_current = (TextView) view.findViewById(com.whaley.biz.playerui.R.id.tv_current);
        tv_total = (TextView) view.findViewById(com.whaley.biz.playerui.R.id.tv_total);
        vsRight = (ViewStub) view.findViewById(com.whaley.biz.playerui.R.id.vs_right);
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
        btn_switch_screen = (ImageButton) view.findViewById(R.id.btn_switch_screen);
        btn_definition = (Button) view.findViewById(R.id.btn_definition);
        btn_definition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onSwitchDefinitionClick();
            }
        });
        btnOtherPlayer = (ImageButton) view.findViewById(R.id.btn_other_player);
        ;
        btnOtherPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onSplitClick();
            }
        });
    }

    @Override
    public void showCameraButton() {
        //
    }

    @Override
    public void hideCameraButton() {
        //
    }

    @Override
    public void changeOtherPlayerButtonVisible(boolean isVisible) {
        //
    }


}
