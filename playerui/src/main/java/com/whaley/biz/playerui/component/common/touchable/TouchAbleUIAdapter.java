package com.whaley.biz.playerui.component.common.touchable;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;

/**
 * Created by YangZhi on 2017/8/2 15:23.
 */

public class TouchAbleUIAdapter extends ControlUIAdapter<TouchAbleController>{

    @Override
    protected View initView(ViewGroup parent) {
        View view = new View(parent.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    protected void onViewCreated(final View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            boolean isMoved;
            float lastX;
            float lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getController().dispatchTouchToPlayerView(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isMoved = false;
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float diffX = Math.abs(event.getRawX() - lastX);
                        float diffY = Math.abs(event.getRawY() - lastY);
                        if (diffX > 30 || diffY > 30) {
                            isMoved = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }

                return isMoved;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onLayoutClick();
            }
        });
    }

    @Override
    public void show(boolean anim) {

    }

    @Override
    public void hide(boolean anim) {

    }
}
