package com.whaley.biz.playerui.component.simpleplayer.initloading;

import android.animation.Animator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.playerui.R;
import com.whaley.biz.playerui.component.common.init.InitUIAdapter;
import com.whaley.core.widget.imageview.FrameAnimImageView;

/**
 * Created by YangZhi on 2017/8/3 17:03.
 */

public class InitLoadingUIAdapter extends InitUIAdapter<InitLoadingController> {

    protected static final int[] LOGO_ANIM_RES_ARR = new int[]{
            R.mipmap.icon_logo_anim_1,
            R.mipmap.icon_logo_anim_2,
            R.mipmap.icon_logo_anim_3,
            R.mipmap.icon_logo_anim_4,
            R.mipmap.icon_logo_anim_5,
            R.mipmap.icon_logo_anim_6,
            R.mipmap.icon_logo_anim_7,
            R.mipmap.icon_logo_anim_8,
            R.mipmap.icon_logo_anim_9,
            R.mipmap.icon_logo_anim_10
    };

    protected FrameAnimImageView ivLogo;
    protected ImageView ivLine;
    protected TextView tvLoadingText;

    protected ViewStub viewStub;

    protected View inflatedView;

    @Override
    public void showInit(boolean anim) {
        startShowAnim();
    }

    @Override
    public void hideInit(boolean anim) {
        startHideAnim();
    }

    @Override
    public void changeVisibleOnError() {
        startHideAnim();
    }

    @Override
    public void changeVisibleOnComplete() {
        startHideAnim();
    }

    private void startShowAnim() {
        show();
    }

    private void startHideAnim() {
        hide();
    }

    public void updateLoadingText(String text) {
        tvLoadingText.setText(text);
    }

    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), R.layout.layout_init_loading);
        viewStub.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return viewStub;
    }

    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;
    }

    protected void checkInflatedView() {
        if (inflatedView == null) {
            inflatedView = viewStub.inflate();
            ivLogo = (FrameAnimImageView) inflatedView.findViewById(R.id.iv_logo);
            ivLine = (ImageView) inflatedView.findViewById(R.id.iv_loading_line);
            tvLoadingText = (TextView) inflatedView.findViewById(R.id.tv_loading_text);
            ivLogo.setImageRes(LOGO_ANIM_RES_ARR);
        }
    }

    protected void show() {
        checkInflatedView();
        inflatedView.setVisibility(View.VISIBLE);
        ivLine.setImageResource(R.mipmap.line_player_loading);
        ivLogo.start();
    }

    protected void hide() {
        if (inflatedView == null)
            return;
        inflatedView.setVisibility(View.GONE);
        ivLogo.stop();
        ivLogo.setImageResource(0);
        ivLine.setImageResource(0);
    }

    @Override
    public View getRootView() {
        return inflatedView == null ? viewStub : inflatedView;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (ivLogo != null) {
            ivLogo.stop();
        }
    }
}
