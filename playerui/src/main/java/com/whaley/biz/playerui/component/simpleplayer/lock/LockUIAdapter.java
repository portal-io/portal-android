package com.whaley.biz.playerui.component.simpleplayer.lock;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.whaley.biz.playerui.R;
import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;

/**
 * Created by YangZhi on 2017/8/2 16:02.
 */

public class LockUIAdapter extends ControlUIAdapter<LockController> {

    ViewStub viewStub;

    protected View inflatedView;

    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), R.layout.layout_control_lock);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        viewStub.setLayoutParams(layoutParams);
        return viewStub;
    }

    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;

    }

    protected void checkInflatedView() {
        if (inflatedView == null) {
            inflatedView = viewStub.inflate();
            inflatedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onLockClick();
                }
            });
            inflatedView.setVisibility(View.GONE);
        }
    }


    public void changeLockBtn(boolean isLocked) {
        getRootView().setSelected(isLocked);
    }

    @Override
    public View getRootView() {
        return inflatedView == null ? viewStub : inflatedView;
    }

    @Override
    public void show(boolean anim) {
        checkInflatedView();
        if (getRootView().getVisibility() != View.VISIBLE)
            return;
        startAnim(0, 0, 1f);
    }

    @Override
    public void hide(boolean anim) {
        if (inflatedView == null)
            return;
        if (getRootView().getVisibility() != View.VISIBLE)
            return;
        startAnim(-getRootView().getMeasuredWidth(), 0, 0f);
    }

    public void show() {
        checkInflatedView();
        getRootView().setVisibility(View.VISIBLE);
    }

    public void hide() {
        if (inflatedView == null)
            return;
        getRootView().setVisibility(View.GONE);
    }
}
