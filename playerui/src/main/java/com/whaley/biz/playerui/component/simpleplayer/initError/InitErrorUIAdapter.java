package com.whaley.biz.playerui.component.simpleplayer.initError;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.playerui.R;
import com.whaley.biz.playerui.component.common.init.InitUIAdapter;

/**
 * Created by YangZhi on 2017/8/7 17:27.
 */

public class InitErrorUIAdapter extends InitUIAdapter<InitErrorController> {

    protected Button btnRetry;

    protected ImageView ivLogo;

    protected TextView tvErrorText;

    protected ViewStub viewStub;

    protected View inflatedView;

    @Override
    public void hideInit(boolean anim) {

    }

    @Override
    public void showInit(boolean anim) {
        hide();
    }

    @Override
    public void changeVisibleOnError() {
        showError();
    }

    @Override
    public void changeVisibleOnComplete() {
        hideError();
    }

    private void showError() {
        show();
    }

    private void hideError() {
        hide();
    }


    protected void show() {
        checkInflatedView();
        inflatedView.setVisibility(View.VISIBLE);
        ivLogo.setImageResource(R.mipmap.icon_logo_anim_1);
    }

    protected void hide() {
        if (inflatedView == null)
            return;
        inflatedView.setVisibility(View.GONE);
        ivLogo.setImageResource(0);
    }

    public void updateErrorText(String text) {
        tvErrorText.setText(text);
    }

    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), getLayoutResource());
        viewStub.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return viewStub;
    }

    protected int getLayoutResource(){
        return R.layout.layout_init_error;
    }

    protected void checkInflatedView() {
        if (inflatedView == null) {
            inflatedView = viewStub.inflate();
            btnRetry = (Button) inflatedView.findViewById(R.id.btn_retry);
            ivLogo = (ImageView) inflatedView.findViewById(R.id.iv_logo);
            tvErrorText = (TextView) inflatedView.findViewById(R.id.tv_error_text);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retry();
                }
            });
        }
    }

    @Override
    public View getRootView() {
        return inflatedView == null ? viewStub : inflatedView;
    }

    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;
    }

    protected void retry() {
        getController().onRetryClick();
    }

    public ViewStub getViewStub() {
        return viewStub;
    }
}
