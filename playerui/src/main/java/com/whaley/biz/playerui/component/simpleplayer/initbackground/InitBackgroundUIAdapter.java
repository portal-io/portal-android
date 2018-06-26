package com.whaley.biz.playerui.component.simpleplayer.initbackground;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.whaley.biz.playerui.R;
import com.whaley.biz.playerui.component.common.init.InitUIAdapter;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageLoaderCallback;
import com.whaley.core.image.ImageRequest;

import java.io.File;

/**
 * Created by YangZhi on 2017/8/2 20:02.
 */

public class InitBackgroundUIAdapter<CONTROLLER extends InitBackgroundController> extends InitUIAdapter<CONTROLLER> {


    protected ViewStub viewStub;

    protected ImageView inflatedView;

    @Override
    public void hideInit(boolean anim) {
        hide();
    }

    @Override
    public void showInit(boolean anim) {
        show();
    }

    @Override
    public void changeVisibleOnError() {
        showInit(true);
    }

    @Override
    public void changeVisibleOnComplete() {
        showInit(true);
    }

    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), getLayoutResourceId());
        viewStub.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return viewStub;
    }


    protected int getLayoutResourceId() {
        return R.layout.layout_init_background;
    }

    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;
    }

    protected void checkInflatedView() {
        if (inflatedView == null) {
            inflatedView = (ImageView) viewStub.inflate();
            inflatedView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    @Override
    public View getRootView() {
        return inflatedView == null ? viewStub : inflatedView;
    }

    protected void show() {
        checkInflatedView();
        inflatedView.setVisibility(View.VISIBLE);
        inflatedView.setAlpha(1f);
//        ImageLoader.with(getContext())
//                .load(R.mipmap.bg_player_loading_fullscreen)
//                .argb8888()
//                .notInitBefore()
//                .centerCrop()
//                .skipMemoryCache(true)
//                .diskCacheStrategy(ImageRequest.DISK_NULL)
//                .into(inflatedView);
        inflatedView.setImageResource(R.mipmap.bg_player_loading_fullscreen);

    }

    protected void hide() {
        if (inflatedView == null)
            return;
//        ImageLoader.clearView(inflatedView);
        inflatedView.setImageResource(0);
        inflatedView.setVisibility(View.GONE);
    }

    @Override
    public void destroy() {
        super.destroy();
//        if (inflatedView != null) {
//            inflatedView.setImageResource(0);
//            ImageLoader.clearView(inflatedView);
//        }
    }
}
