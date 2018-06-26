package com.whaley.biz.program.playersupport.component.liveplayer.initerror;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;

/**
 * Created by YangZhi on 2017/8/8 19:45.
 */

public class InitErrorUIAdapter extends com.whaley.biz.playerui.component.simpleplayer.initError.InitErrorUIAdapter {

    TextView tvError;

    ImageView iv_img;

    View inflatedView;

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_live_player_init_error;
    }

    @Override
    protected void checkInflatedView() {
        if (inflatedView == null) {
            inflatedView = getViewStub().inflate();
            tvError = (TextView) inflatedView.findViewById(R.id.tv_error);
            iv_img = (ImageView) inflatedView.findViewById(R.id.iv_img);
        }
    }


    @Override
    public void updateErrorText(String text) {
        tvError.setText(text);
    }

    @Override
    protected void show() {
        checkInflatedView();
        inflatedView.setVisibility(View.VISIBLE);
        iv_img.setImageResource(R.mipmap.icon_liveplayer_fail);
    }

    @Override
    protected void hide() {
        if (inflatedView == null)
            return;
        inflatedView.setVisibility(View.GONE);
        iv_img.setImageResource(0);
    }

    @Override
    public View getRootView() {
        return inflatedView == null ? super.getRootView() : inflatedView;
    }


}
