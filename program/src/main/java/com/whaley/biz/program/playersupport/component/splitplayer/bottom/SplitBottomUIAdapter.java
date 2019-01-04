package com.whaley.biz.program.playersupport.component.splitplayer.bottom;

import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.playersupport.component.normalplayer.normalbottomui.NormalBottomUIAdapter;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitBottomUIAdapter extends NormalBottomUIAdapter {

    boolean isLive;

    public SplitBottomUIAdapter(boolean isLive){
        this.isLive = isLive;
    }

    protected int getRootViewId(){
        return R.layout.layout_bottom_split_controls;
    }

    @Override
    protected void initRight(ViewStub vsRight) {
        vsRight.setLayoutResource(R.layout.layout_player_split_bottom_right);
        View layoutRight = vsRight.inflate();
        btn_switch_screen = (ImageButton) layoutRight.findViewById(R.id.btn_switch_screen);
        btn_switch_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onSwitchScreenClick();
            }
        });
    }

    @Override
    protected void onViewCreated(View view) {
        sk_progress = (SeekBar) view.findViewById(R.id.sk_progress);
        tv_current = (TextView) view.findViewById(R.id.tv_current);
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        vsRight = (ViewStub) view.findViewById(R.id.vs_right);

        initRight(vsRight);

        btn_switch_screen = (ImageButton) view.findViewById(R.id.btn_switch_screen);
        btn_definition = (Button) view.findViewById(R.id.btn_definition);
        btn_camera = (ImageButton) view.findViewById(R.id.btn_camera);
        btn_definition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onSwitchDefinitionClick();
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onCameraClick();
            }
        });
        btnOtherPlayer = (ImageButton)view.findViewById(R.id.btn_other_player);;
        btnOtherPlayer.setVisibility(View.GONE);
        btnOtherPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onSplitClick();
            }
        });
        if (isLive) {
            sk_progress.setVisibility(View.GONE);
            tv_current.setVisibility(View.GONE);
            tv_total.setVisibility(View.GONE);
        }else {
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
    }

}
