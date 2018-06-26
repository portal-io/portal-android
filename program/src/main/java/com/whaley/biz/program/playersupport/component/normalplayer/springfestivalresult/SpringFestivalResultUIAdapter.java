package com.whaley.biz.program.playersupport.component.normalplayer.springfestivalresult;

import android.graphics.Color;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.biz.program.widget.SpringTextView;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by dell on 2018/1/24.
 */

public class SpringFestivalResultUIAdapter extends BaseUIAdapter<SpringFestivalResultController> {


    ViewStub viewStub;

    View inflatedLayout;

    boolean isOnShow;

    TextView tvTitleCenter,tvTitleRight;
    ImageView ivLight;
    SpringTextView tvName;
    TextView tvContent;
    TextView tvLeft;
    Button btnCollect;
    ImageButton btnClose;

    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), R.layout.layout_player_card_result);
        viewStub.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return viewStub;
    }

    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;
    }


    public void showResult(boolean isFinished, int leftNum, String title, String name, SpannableString content, String collect, String left) {
        isOnShow = true;
        checkViewStub();
        tvName.setText(name);
        tvContent.setText(content);
        btnCollect.setText(collect);
        tvLeft.setText(left);
        if(leftNum>0){
            if(isFinished){
                tvTitleRight.setVisibility(View.GONE);
                tvTitleRight.setText("");
            }else{
                tvTitleRight.setVisibility(View.VISIBLE);
                tvTitleRight.setText(title);
            }
            tvTitleCenter.setVisibility(View.GONE);
            tvTitleCenter.setText("");
            btnCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onBackClick();
                }
            });
        }else{
            tvTitleRight.setVisibility(View.GONE);
            tvTitleCenter.setVisibility(View.VISIBLE);
            tvTitleCenter.setText(title);
            tvTitleRight.setText("");
            btnCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onViewCardClick();
                }
            });
        }
        if(isFinished){
            tvContent.setEnabled(true);
            tvContent.setTextColor(Color.parseColor("#D60C0C"));
        }else{
            tvContent.setEnabled(false);
            tvContent.setTextColor(Color.parseColor("#666666"));
        }
        startShowAim();
    }

    public void fixResult(int leftNum, String title, String collect, String left){
        btnCollect.setText(collect);
        tvLeft.setText(left);
        if(leftNum>0){
            if(tvContent.isEnabled()){
                tvTitleRight.setVisibility(View.GONE);
                tvTitleRight.setText("");
            }else{
                tvTitleRight.setVisibility(View.VISIBLE);
                tvTitleRight.setText(title);
            }
            tvTitleCenter.setVisibility(View.GONE);
            tvTitleCenter.setText("");
            btnCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onBackClick();
                }
            });
        }else{
            tvTitleRight.setVisibility(View.GONE);
            tvTitleCenter.setVisibility(View.VISIBLE);
            tvTitleCenter.setText(title);
            tvTitleRight.setText("");
            btnCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onViewCardClick();
                }
            });
        }
    }

    @Override
    public View getRootView() {
        return inflatedLayout == null ? super.getRootView() : inflatedLayout;
    }

    private void checkViewStub() {
        if (inflatedLayout == null) {
            inflatedLayout = viewStub.inflate();
            inflatedLayout.setAlpha(1f);
            tvTitleCenter = (TextView) inflatedLayout.findViewById(R.id.tv_title_center);
            tvTitleRight = (TextView) inflatedLayout.findViewById(R.id.tv_title_right);
            ivLight = (ImageView) inflatedLayout.findViewById(R.id.iv_light);
            tvName = (SpringTextView) inflatedLayout.findViewById(R.id.tv_name);
            tvContent = (TextView) inflatedLayout.findViewById(R.id.tv_content);
            tvLeft = (TextView) inflatedLayout.findViewById(R.id.tv_left);
            btnCollect = (Button) inflatedLayout.findViewById(R.id.btn_collect);
            btnClose = (ImageButton) inflatedLayout.findViewById(R.id.btn_close);
            tvTitleRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onViewCardClick();
                }
            });
            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onViewCardClick();
                }
            });
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onBackClick();
                }
            });
            inflatedLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });
        }
    }

    public void startShowAim() {
        AdditiveAnimator.cancelAnimations(inflatedLayout);
        AdditiveAnimator.animate(inflatedLayout)
                .alpha(1f)
                .addStartAction(new Runnable() {
                    @Override
                    public void run() {
                        inflatedLayout.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }


    public void hide() {
        isOnShow = false;
        AdditiveAnimator.cancelAnimations(inflatedLayout);
        AdditiveAnimator.animate(inflatedLayout)
                .alpha(0f)
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        inflatedLayout.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    public boolean isOnShow() {
        return isOnShow;
    }

    @Override
    public void destroy() {
        if (inflatedLayout != null) {
            AdditiveAnimator.cancelAnimations(inflatedLayout);
        }
    }

}

