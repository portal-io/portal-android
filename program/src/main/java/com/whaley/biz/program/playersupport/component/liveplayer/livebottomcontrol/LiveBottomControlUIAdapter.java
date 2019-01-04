package com.whaley.biz.program.playersupport.component.liveplayer.livebottomcontrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;
import com.whaley.biz.program.R;

/**
 * Created by YangZhi on 2017/8/8 20:16.
 */

public class LiveBottomControlUIAdapter extends ControlUIAdapter<LiveBottomControlController> {

    Button btnEdit;

    Button btnDefinition;

    ImageButton btnChangeCamera;

    ImageButton btnOtherPlayer;

    ImageButton btnGift;


    @Override
    public void show(boolean anim) {
        startAnim(0f, 0f, 1f);
    }

    @Override
    public void hide(boolean anim) {
        startAnim(0f, getRootView().getMeasuredHeight(), 0f);
    }

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_player_bottom_control, parent, false);
        return view;
    }

    @Override
    protected void onViewCreated(View view) {
        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnDefinition = (Button) view.findViewById(R.id.btn_definition);
        btnChangeCamera = (ImageButton) view.findViewById(R.id.btn_change_camera);
        btnOtherPlayer = (ImageButton) view.findViewById(R.id.btn_other_player);
        btnOtherPlayer.setVisibility(View.GONE);
        btnGift = (ImageButton) view.findViewById(R.id.btn_gift);
        btnEdit.setVisibility(View.INVISIBLE);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onDanmuButtonClick();
            }
        });

        btnDefinition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onSwitchDefinitionClick();
            }
        });

        btnChangeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onCameraClick();
            }
        });

        btnOtherPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onSplitClick();
            }
        });
        btnGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onGiftClick();
            }
        });
    }

    public void enableOtherPlayer(boolean enable) {
        btnOtherPlayer.setEnabled(enable);
    }

    public void updateDefinitionText(String text) {
        btnDefinition.setText(text);
    }

    public void changeSwitchDefinitionEnable(boolean isEnable) {
        btnDefinition.setEnabled(isEnable);
    }

    public void showCameraButton() {
        btnChangeCamera.setVisibility(View.VISIBLE);
    }

    public void showGiftButton() {
        btnGift.setVisibility(View.VISIBLE);
    }

    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void changeBtnDanmuVisible(boolean isVisible) {
        btnEdit.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

}
