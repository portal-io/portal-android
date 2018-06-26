package com.whaley.biz.program.playersupport.component.liveplayer.liveinfoui;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.biz.program.playersupport.component.liveplayer.liveinfo.LiveInfoController;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by yangzhi on 2017/9/8.
 */

public class LiveInfoUIAdapter extends BaseUIAdapter<LiveInfoUIController> {

    ViewStub viewStub;

    View inflatedLayout;

    TextView tvTitleName;
    TextView tvPlayCount;
    TextView tvPlayerTime;
    TextView tvLocation;
    TextView tvLiveContent;
    View layoutInfo;

    boolean isOnShow;

    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), R.layout.layout_live_player_info);
        viewStub.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return viewStub;
    }

    @Override
    public View getRootView() {
        return inflatedLayout == null ? super.getRootView() : inflatedLayout;
    }

    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;
    }

    public void show(LiveInfoViewModel liveInfoViewModel) {
        isOnShow = true;
        if (inflatedLayout == null) {
            inflatedLayout = viewStub.inflate();
            inflatedLayout.setAlpha(0f);
            layoutInfo = inflatedLayout.findViewById(R.id.layout_info);
            tvTitleName = (TextView) inflatedLayout.findViewById(R.id.tv_title_name);
            tvPlayCount = (TextView) inflatedLayout.findViewById(R.id.tv_play_count);
            tvPlayerTime = (TextView) inflatedLayout.findViewById(R.id.tv_player_time);
            tvLocation = (TextView) inflatedLayout.findViewById(R.id.tv_location);
            tvLiveContent = (TextView) inflatedLayout.findViewById(R.id.tv_live_content);
            tvLiveContent.setMovementMethod(ScrollingMovementMethod.getInstance());
            layoutInfo.setClickable(true);
            inflatedLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
        }
        tvTitleName.setText(liveInfoViewModel.getTvTitle());
        tvLiveContent.setText(liveInfoViewModel.getTvContent());
        tvPlayCount.setText(liveInfoViewModel.getPlayCount());
        tvLocation.setText(liveInfoViewModel.getTvLocation());
        tvPlayerTime.setText(liveInfoViewModel.getTvPlayerTime());
        AdditiveAnimator.cancelAnimations(inflatedLayout);
        AdditiveAnimator.animate(inflatedLayout)
                .alpha(1f)
                .addStartAction(new Runnable() {
                    @Override
                    public void run() {
                        inflatedLayout.setVisibility(View.VISIBLE);
                        layoutInfo.setBackgroundResource(R.mipmap.dialog_bg);
                    }
                })
                .start();
    }

    public void hide() {
        if(inflatedLayout==null)
            return;
        isOnShow = false;
        AdditiveAnimator.cancelAnimations(inflatedLayout);
        AdditiveAnimator.animate(inflatedLayout)
                .alpha(0f)
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        inflatedLayout.setVisibility(View.GONE);
                        layoutInfo.setBackgroundResource(0);
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
