package com.whaley.biz.program.playersupport.component.liveplayer.livetitle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.core.debug.logger.Log;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by YangZhi on 2017/8/8 20:43.
 */

public class LiveTitleUIAdapter extends ControlUIAdapter<LiveTitleController> {

    TextView tvTitle;

    TextView tvPlayCount;

    ImageButton btnShare;

    TextView tvContributionRank;

    View viewContributionRankLine;

    Button btnContribution;
    View viewLiveTitle;

    @Override
    public void show(boolean anim) {
        startAnim(0f, 0f, 1f, viewLiveTitle);
        showContribution();
    }

    private void startAnim(float finalTransX, float finalTransY, float finalAlpha, View view) {
        cancelAnim(view);
        AdditiveAnimator animator = AdditiveAnimator.animate(view)
                .translationX(finalTransX)
                .translationY(finalTransY)
                .alpha(finalAlpha);
        animator.start();
    }

    private void cancelAnim(View view) {
        if (view != null) {
            AdditiveAnimator.cancelAnimations(view);
        }
    }

    @Override
    public void hide(boolean anim) {
        startAnim(0f, -viewLiveTitle.getMeasuredHeight(), 0f, viewLiveTitle);
        hideContribution();
    }

    public void showContribution() {
        if (getController().isContributionRank && !getController().isLandScape) {
            startAnim(0f, 0f, 1f, btnContribution);
        }
    }

    public void hideContribution() {
        if (getController().isContributionRank && !getController().isLandScape) {
            startAnim(-btnContribution.getMeasuredWidth(), 0f, 0f, btnContribution);
        }
    }

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_player_title, parent, false);
        return view;
    }

    @Override
    protected void onViewCreated(View view) {
        viewLiveTitle = view.findViewById(R.id.view_live_title);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvPlayCount = (TextView) view.findViewById(R.id.tv_play_count);
        btnShare = (ImageButton) view.findViewById(R.id.btn_share);
        tvContributionRank = (TextView) view.findViewById(R.id.tv_contribution_rank);
        viewContributionRankLine = view.findViewById(R.id.view_contribution_rank_line);
        btnContribution = (Button) view.findViewById(R.id.btn_contribution);
        viewLiveTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onTitleClick();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onShareClick();
            }
        });
        tvContributionRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onContributionRankClick();
            }
        });
        btnContribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onContributionRankClick();
            }
        });

    }

    public void updateTitle(String titleName) {
        tvTitle.setText(titleName);
    }

    public void updatePlayCount(String playCountStr) {
        tvPlayCount.setText(playCountStr);
    }

    public void showOnLandScape() {
        tvContributionRank.setVisibility(View.VISIBLE);
        viewContributionRankLine.setVisibility(View.VISIBLE);
        btnContribution.setVisibility(View.GONE);
    }

    public void showOnPortrait() {
        tvContributionRank.setVisibility(View.GONE);
        viewContributionRankLine.setVisibility(View.GONE);
        btnContribution.setVisibility(View.VISIBLE);
    }
}
