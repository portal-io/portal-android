package com.whaley.biz.launcher.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.whaley.biz.playerui.PlayerView;

/**
 * Created by yangzhi on 2017/8/31.
 */

public class PlayerContainer extends FrameLayout {

    private PlayerView playerView;

    private Rect rect = new Rect();

    public PlayerContainer(@NonNull Context context) {
        super(context);
    }

    public PlayerContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerContainer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setPlayerView(PlayerView playerView) {
        this.playerView = playerView;
        addView(playerView);
    }

    public void destroy() {
        if (playerView != null) {
            removeView(playerView);
        }
        playerView = null;
    }

    public void setRect(Rect rect) {
        if (playerView == null)
            return;
        this.rect.set(rect);
//        scrollTo(rect.left,rect.top);
        if (playerView.getTranslationX() != rect.left) {
            playerView.setTranslationX(rect.left);
        }
        if (playerView.getTranslationY() != rect.top) {
            playerView.setTranslationY(rect.top);
        }
//        playerView.requestLayout();

    }

    public void resetPosition() {
        if (playerView == null)
            return;
        this.rect.set(rect);
        playerView.setTranslationX(0);
        playerView.setTranslationY(0);
    }
}
