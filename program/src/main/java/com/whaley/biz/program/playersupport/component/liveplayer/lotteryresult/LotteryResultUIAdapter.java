package com.whaley.biz.program.playersupport.component.liveplayer.lotteryresult;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.core.image.ImageLoader;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by yangzhi on 2017/8/14.
 */

public class LotteryResultUIAdapter extends BaseUIAdapter<LotteryResultController> {


    ViewStub viewStub;

    View inflatedLayout;

    View layoutLotteryResult;

    TextView tvTitleName;

    TextView tvLotteryResult;

    ImageView ivLotteryResult;

    TextView tvNextLotteryTime;

    Button btnToAcceptPrize;

    boolean isOnShow;


    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), R.layout.layout_live_player_lottery_result);
        viewStub.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return viewStub;
    }

    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;
    }


    public void showLotterySuccess(String time, String name, String picUrl) {
        isOnShow = true;
        checkViewStub();
        tvTitleName.setText("恭喜你抽中了");
        tvLotteryResult.setVisibility(View.VISIBLE);
        tvLotteryResult.setText(name);
        btnToAcceptPrize.setText("前往领奖");
        btnToAcceptPrize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onLotterySuccesButtonClick();
            }
        });
        tvNextLotteryTime.setText(time);
        ImageLoader.with(getActivity()).load(picUrl).small().circle().into(ivLotteryResult);
        startShowAim();
    }

    public void showLotteryFail(String time) {
        isOnShow = true;
        checkViewStub();
        tvTitleName.setText("真可惜！什么都没有！");
        tvLotteryResult.setVisibility(View.GONE);
        btnToAcceptPrize.setText("等会再来");
        btnToAcceptPrize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onLotteryFailButtonClick();
            }
        });
        tvNextLotteryTime.setText(time);
        ImageLoader.with(getActivity()).load(R.mipmap.ic_not_winning).circle().into(ivLotteryResult);
        startShowAim();
    }

    @Override
    public View getRootView() {
        return inflatedLayout == null ? super.getRootView() : inflatedLayout;
    }

    private void checkViewStub() {
        if (inflatedLayout == null) {
            inflatedLayout = viewStub.inflate();
            inflatedLayout.setAlpha(1f);
            layoutLotteryResult = inflatedLayout.findViewById(R.id.layout_lottery_result);
            tvTitleName = (TextView) inflatedLayout.findViewById(R.id.tv_title_name);
            tvLotteryResult = (TextView) inflatedLayout.findViewById(R.id.tv_lottery_result);
            ivLotteryResult = (ImageView) inflatedLayout.findViewById(R.id.iv_lottery_result);
            tvNextLotteryTime = (TextView) inflatedLayout.findViewById(R.id.tv_lottery_next_time);
            btnToAcceptPrize = (Button) inflatedLayout.findViewById(R.id.btn_to_accept_prize);
            inflatedLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
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
                        layoutLotteryResult.setBackgroundResource(R.mipmap.dialog_bg);
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
                        layoutLotteryResult.setBackgroundResource(0);
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
        if (ivLotteryResult != null) {
            ImageLoader.clearView(ivLotteryResult);
        }
    }
}
