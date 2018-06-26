package com.whaley.biz.program.playersupport.component.bannerplayer.bottomui;

import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.Toast;

import com.whaley.biz.playerui.component.simpleplayer.bottomcontrol.BottomControlUIAdapter;
import com.whaley.biz.program.R;

/**
 * Created by YangZhi on 2017/8/28 16:57.
 */

public class BannerBottomUIAdapter extends BottomControlUIAdapter<BannerBottomUIController>{

    ImageButton btn_switch_screen;
    ImageButton btn_other_player;

    @Override
    protected void initRight(ViewStub vsRight) {
        vsRight.setLayoutResource(R.layout.layout_player_banner_bottom_right);
        super.initRight(vsRight);

    }

    @Override
    protected void onViewCreated(View view) {
        super.onViewCreated(view);
        btn_switch_screen = (ImageButton) view.findViewById(R.id.btn_switch_screen);
        btn_other_player = (ImageButton) view.findViewById(R.id.btn_other_player);
        btn_other_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onSplitClick();
            }
        });
    }


    private void changeToGone(View view){
        if(view.getVisibility()==View.VISIBLE) {
            view.setVisibility(View.GONE);
            view.setEnabled(false);
        }
    }

    private void changeToVisible(View view){
        if(view.getVisibility()!=View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
            view.setEnabled(true);
        }
    }

    public void changeToVR(){
        changeToVisible(getSk_progress());
        changeToVisible(getBtn_start_pause());
        changeToVisible(getTv_current());
        changeToVisible(getTv_total());
        changeToVisible(btn_other_player);
    }

    public void changeToMovie(){
        changeToVisible(getSk_progress());
        changeToVisible(getBtn_start_pause());
        changeToVisible(getTv_current());
        changeToVisible(getTv_total());
        changeToVisible(btn_other_player);
    }

    public void changeToLive(){
        changeToGone(getSk_progress());
        changeToGone(getBtn_start_pause());
        changeToGone(getTv_current());
        changeToGone(getTv_total());
        changeToVisible(btn_other_player);
    }

    public void showToast(String text){
        Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
    }

    public void changeOtherPlayerButtonVisible(boolean isVisible){
        btn_other_player.setVisibility(isVisible?View.VISIBLE:View.GONE);
    }

}
