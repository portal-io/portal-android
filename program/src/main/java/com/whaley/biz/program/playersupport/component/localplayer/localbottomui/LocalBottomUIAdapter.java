package com.whaley.biz.program.playersupport.component.localplayer.localbottomui;

import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;

import com.whaley.biz.playerui.component.simpleplayer.bottomcontrol.BottomControlUIAdapter;
import com.whaley.biz.program.R;

/**
 * Created by yangzhi on 2017/8/23.
 */

public class LocalBottomUIAdapter extends BottomControlUIAdapter<LocalBottomUIController>{

    ImageButton btnOtherPlayer;

    Button btnChangeRender;

    @Override
    protected void initRight(ViewStub vsRight) {
        vsRight.setLayoutResource(R.layout.layout_player_local_bottom_right);
        View layoutRight = vsRight.inflate();
        btnChangeRender = (Button)layoutRight.findViewById(R.id.btn_change_render);
        btnOtherPlayer = (ImageButton)layoutRight.findViewById(R.id.btn_other_player);;
        btnOtherPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onSplitClick();
            }
        });
        btnChangeRender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onChangeRenderClick();
            }
        });
    }

    public void updateChangeRenderBtnText(String text){
        btnChangeRender.setText(text);
    }

    public void setChangeRenderVisible(){
        btnChangeRender.setVisibility(View.VISIBLE);
    }

}

