package com.whaley.biz.program.playersupport.component.splitplayer.initloading;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.playerui.component.simpleplayer.initloading.InitLoadingUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.core.widget.imageview.FrameAnimImageView;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitInitLoadingUIAdapter extends InitLoadingUIAdapter {

    protected FrameAnimImageView ivLogo2;
    protected ImageView ivLine2;
    protected TextView tvLoadingText2;

    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), R.layout.layout_split_init_loading);
        viewStub.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return viewStub;
    }

    protected void checkInflatedView() {
        if (inflatedView == null) {
            inflatedView = viewStub.inflate();
            ivLogo = (FrameAnimImageView) inflatedView.findViewById(R.id.iv_logo);
            ivLine = (ImageView) inflatedView.findViewById(R.id.iv_loading_line);
            tvLoadingText = (TextView) inflatedView.findViewById(R.id.tv_loading_text);
            ivLogo2 = (FrameAnimImageView) inflatedView.findViewById(R.id.iv_logo2);
            ivLine2 = (ImageView) inflatedView.findViewById(R.id.iv_loading_line2);
            tvLoadingText2 = (TextView) inflatedView.findViewById(R.id.tv_loading_text2);
            ivLogo.setImageRes(LOGO_ANIM_RES_ARR);
            ivLogo2.setImageRes(LOGO_ANIM_RES_ARR);
        }
    }

    public void updateLoadingText(String text) {
        super.updateLoadingText(text);
        tvLoadingText2.setText(text);
    }

    protected void show() {
        super.show();
        ivLine2.setImageResource(com.whaley.biz.playerui.R.mipmap.line_player_loading);
        ivLogo2.start();
    }

    protected void hide() {
        super.hide();
        if (inflatedView == null)
            return;
        inflatedView.setVisibility(View.GONE);
        ivLogo2.stop();
        ivLogo2.setImageResource(0);
        ivLine2.setImageResource(0);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (ivLogo2 != null) {
            ivLogo2.stop();
        }
    }

}
