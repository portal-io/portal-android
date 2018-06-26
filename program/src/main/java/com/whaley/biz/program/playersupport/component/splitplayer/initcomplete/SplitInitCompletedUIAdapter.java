package com.whaley.biz.program.playersupport.component.splitplayer.initcomplete;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.playerui.component.simpleplayer.initCompleted.InitCompletedUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.core.widget.imageview.FrameAnimImageView;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitInitCompletedUIAdapter extends InitCompletedUIAdapter {

    protected FrameAnimImageView ivLogo2;
    protected ImageView ivLoadingLine2;
    protected TextView tvCompleteText2;
    protected TextView tvCompleteTime1;
    protected TextView tvCompleteTime2;


    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), R.layout.layout_split_init_completed);
        viewStub.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return viewStub;
    }

    @Override
    public View getRootView() {
        return inflatedView == null ? super.getRootView() : inflatedView;
    }

    protected void checkInflatedView() {
        if (inflatedView == null) {
            inflatedView = viewStub.inflate();
            ivLogo = (FrameAnimImageView) inflatedView.findViewById(R.id.iv_logo);
            ivLoadingLine = (ImageView) inflatedView.findViewById(R.id.iv_loading_line);
            tvCompleteText = (TextView) inflatedView.findViewById(R.id.tv_complete_text);
            tvCompleteTime1 = (TextView) inflatedView.findViewById(R.id.tv_complete_time);
            ivLogo2 = (FrameAnimImageView) inflatedView.findViewById(R.id.iv_logo2);
            ivLoadingLine2 = (ImageView) inflatedView.findViewById(R.id.iv_loading_line2);
            tvCompleteText2 = (TextView) inflatedView.findViewById(R.id.tv_complete_text2);
            tvCompleteTime2 = (TextView) inflatedView.findViewById(R.id.tv_complete_time2);
            ivLogo.setImageRes(LOGO_ANIM_RES_ARR);
            ivLogo2.setImageRes(LOGO_ANIM_RES_ARR);
        }
    }

    public void updateCompleteText(String time, String text) {
        super.updateCompleteText(text);
        tvCompleteText2.setText(text);
        tvCompleteTime1.setText(time);
        tvCompleteTime2.setText(time);
    }

    public void showOrHideLayoutNext(boolean isShow) {
        //
    }

    protected void show() {
        super.show();
        ivLoadingLine2.setImageResource(R.mipmap.line_player_loading);
        ivLogo2.start();
    }

    protected void hide() {
        super.hide();
        ivLogo2.stop();
        ivLogo2.setImageResource(0);
        ivLoadingLine2.setImageResource(0);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (ivLogo2 != null) {
            ivLogo2.stop();
        }
    }

}
