package com.whaley.biz.program.playersupport.component.normalplayer.normalbottomui;

import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.whaley.biz.playerui.component.simpleplayer.bottomcontrol.BottomControlUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

/**
 * Created by yangzhi on 2017/8/5.
 */

public class NormalBottomUIAdapter extends BottomControlUIAdapter<NormalBottomController> {

    protected ImageButton btn_switch_screen;

    protected Button btn_definition;

    protected ImageButton btn_camera;

    protected ImageButton btnOtherPlayer;

    @Override
    protected void initRight(ViewStub vsRight) {
        vsRight.setLayoutResource(R.layout.layout_player_bottom_right);
        super.initRight(vsRight);
    }

    @Override
    protected void onViewCreated(View view) {
        super.onViewCreated(view);
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
        btnOtherPlayer = (ImageButton) view.findViewById(R.id.btn_other_player);
        btnOtherPlayer.setVisibility(View.GONE);

        btnOtherPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onSplitClick();
            }
        });
//        Router.getInstance().buildExecutor("/launch/service/getfestival")
//                .notTransCallbackData()
//                .notTransParam()
//                .callback(new Executor.Callback<Boolean>() {
//                    @Override
//                    public void onCall(Boolean aBoolean) {
//                        if (aBoolean)
//                            btn_switch_screen.setBackgroundResource(R.mipmap.ic_activity_switchscreen);
//                    }
//
//                    @Override
//                    public void onFail(Executor.ExecutionError executionError) {
//
//                    }
//                })
//                .excute();
    }

    public void showSwitchScreen() {
        btn_switch_screen.setVisibility(View.VISIBLE);
        btn_definition.setVisibility(View.GONE);
    }

    public void showSwitchDefinition() {
        btn_switch_screen.setVisibility(View.GONE);
        btn_definition.setVisibility(View.VISIBLE);
    }

    public void showCameraButton() {
        btn_camera.setVisibility(View.VISIBLE);
    }

    public void hideCameraButton() {
        btn_camera.setVisibility(View.GONE);
    }

    public void changeOtherPlayerButtonVisible(boolean isVisible) {
//        btnOtherPlayer.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void changeSwitchDefinitionEnable(boolean enable) {
        btn_definition.setEnabled(enable);
    }

    public void updateDefinitionText(String text) {
        btn_definition.setText(text);
    }

    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

}
