package com.whaley.biz.program.ui.live;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.common.widget.emptylayout.EmptyDisplayLayout;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.LiveConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.live.adapter.TeamMemberAdapter;
import com.whaley.biz.program.ui.live.presenter.LiveReserveDetailPresenter;
import com.whaley.biz.program.ui.uimodel.ReserveDetailViewModel;
import com.whaley.biz.program.widget.PilgiTextView;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.uiframe.view.EmptyDisplayView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/17.
 */
@Route(path = ProgramRouterPath.LIVE_STATE_BEFORE)
public class LiveReserveDetailFragment extends BaseMVPFragment<LiveReserveDetailPresenter> implements LiveReserveDetailView, LiveConstants {

    @BindView(R2.id.iv_pic)
    ImageView ivPic;
    @BindView(R2.id.tv_amount)
    PilgiTextView tvAmount;
    @BindView(R2.id.btn_reserve)
    Button btnReserve;
    @BindView(R2.id.tv_date)
    TextView tvDate;
    @BindView(R2.id.tv_remain)
    PilgiTextView tvRemain;
    @BindView(R2.id.tv_unit)
    TextView tvUnit;
    @BindView(R2.id.tv_location)
    TextView tvLocation;
    @BindView(R2.id.tv_intro)
    TextView tvIntro;
    @BindView(R2.id.rv_team_members)
    RecyclerView rvTeamMembers;
    @BindView(R2.id.tv_team_title)
    TextView tvTeamTitle;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_buy)
    TextView tvBuy;
    @BindView(R2.id.tv_have_voucher)
    TextView tvHaveVoucher;
    @BindView(R2.id.emptyLayout)
    EmptyDisplayLayout emptyLayout;

    private ImageRequest.RequestManager requestManager;
    private TeamMemberAdapter teamMemberAdapter;

    public static void goPage(Starter starter, String code) {
        Intent intent = CommonActivity.createIntent(starter, LiveReserveDetailFragment.class.getName(), null);
        intent.putExtra(ProgramConstants.KEY_PARAM_ID, code);
        starter.startActivityForResult(intent, 0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_reserve_detail;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        StatusBarUtil.setTransparentFullStatusBar(getActivity().getWindow(), getSystemBarManager(), false);
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        requestManager = ImageLoader.with(this);
        rvTeamMembers.setLayoutManager(new GridLayoutManager(getContext(), 4));
        teamMemberAdapter = new TeamMemberAdapter(requestManager);
        rvTeamMembers.setAdapter(teamMemberAdapter);
        emptyLayout.setOnRetryListener(new EmptyDisplayView.OnRetryListener() {
            @Override
            public void onRetry() {
                getPresenter().getDetailInfo();
            }
        });
    }

    @Override
    public void updateData(ReserveDetailViewModel reserveDetailViewModel) {
        requestManager.load(reserveDetailViewModel.getPic()).big().placeholder(R.mipmap.default_8).into(ivPic);
        tvTitle.setText(reserveDetailViewModel.getName());
        tvIntro.setText(reserveDetailViewModel.getIntro());
        tvDate.setText(reserveDetailViewModel.getTvDate());
        tvRemain.setText(reserveDetailViewModel.getTvRemain());
        tvUnit.setText(reserveDetailViewModel.getTvUnit());
        tvLocation.setText(reserveDetailViewModel.getAddress());
        if (reserveDetailViewModel.getGuests() != null && reserveDetailViewModel.getGuests().size() > 0) {
            tvTeamTitle.setVisibility(View.VISIBLE);
            teamMemberAdapter.setData(reserveDetailViewModel.getGuests());
        }
    }

    @Override
    public void updateReserve(boolean isReserve) {
        if (isReserve) {
            btnReserve.setBackgroundResource(R.drawable.bg_reserve_btn_on);
            btnReserve.setText("已预约");
        } else {
            btnReserve.setBackgroundResource(R.drawable.bg_reserve_btn_off);
            btnReserve.setText("立即预约");
        }
        tvAmount.setText("" + getPresenter().getRepository().getTvAmount());
    }

    @Override
    public void showPay(boolean isBuyVisible) {
        if (isBuyVisible) {
            tvBuy.setText(getPresenter().getRepository().getPrice());
            tvBuy.setVisibility(View.VISIBLE);
        } else {
            tvBuy.setVisibility(View.GONE);
        }
        if (getPresenter().isVoucher()) {
            tvHaveVoucher.setVisibility(View.VISIBLE);
        } else {
            tvHaveVoucher.setVisibility(View.GONE);
        }
    }

    @OnClick(R2.id.btn_reserve)
    public void onReserveClick() {
        getPresenter().onReserveClick();
    }

    @OnClick(R2.id.btn_back)
    public void onBackClick() {
        finish();
    }

    @OnClick(R2.id.btn_share)
    public void onShareClick() {
        getPresenter().onShare();
    }

    @OnClick(R2.id.tv_buy)
    void tvBuy() {
        getPresenter().checklogin(true);
    }

    @Override
    public EmptyDisplayView getEmptyDisplayView() {
        return emptyLayout;
    }
}
