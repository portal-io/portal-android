package com.whaley.biz.setting.ui.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.ui.BaseMVPDialogFragment;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.constant.SettingConstants;
import com.whaley.biz.setting.event.RedemptionSuccessEvent;
import com.whaley.biz.setting.model.RedemptionCodeModel;
import com.whaley.biz.setting.ui.presenter.RedemptionSuccessPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/9/4.
 */

public class RedemptionSuccessFragment extends BaseMVPDialogFragment<RedemptionSuccessPresenter> implements RedemptionSuccessView{

    @BindView(R2.id.tv_price)
    TextView tvPrice;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_code)
    TextView tvCode;
    @BindView(R2.id.tv_look)
    TextView tvLook;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.view_redemption_success)
    FrameLayout viewRedemptionSuccess;

    @OnClick(R2.id.view_redemption_success)
    void onRedemptionSuccessClick() {
        dismiss();
    }

    @OnClick(R2.id.view_redemption_voucher)
    void onRedemptionVoucherClick() {
        getPresenter().onRedemptionVoucherClick();
    }

    public static RedemptionSuccessFragment showDialog(RedemptionCodeModel data, FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag("RedemptionSuccess");
        if (null != fragment) {
            ft.remove(fragment);
        }
        RedemptionSuccessFragment dialogFragment = RedemptionSuccessFragment.newInstance(data);
        ft.add(dialogFragment, "RedemptionSuccess");
        ft.commitAllowingStateLoss();
        return dialogFragment;
    }

    public static RedemptionSuccessFragment newInstance(RedemptionCodeModel data) {
        RedemptionSuccessFragment redemptionSuccessFragment = new RedemptionSuccessFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RedemptionSuccessPresenter.STR_PARAM_REDEMPTION, data);
        redemptionSuccessFragment.setArguments(bundle);
        return redemptionSuccessFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        StatusBarUtil.setTransparentFullStatusBar(getActivity().getWindow(), new SystemBarTintManager(getActivity()), false);
        super.setViews(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.color2_alpha_50);
        tvPrice.setText(getPresenter().getRedemptionSuccessRepository().getPrice());
        tvCode.setText(getPresenter().getRedemptionSuccessRepository().getCode().toUpperCase());
        tvName.setText(getPresenter().getRedemptionSuccessRepository().getName());
        tvContent.setText(getPresenter().getRedemptionSuccessRepository().getContent());
        tvLook.setText(getPresenter().getRedemptionSuccessRepository().isLook() ? R.string.tv_redemption_look : R.string.tv_redemption_no_player);
        tvCode.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_redemption_success;
    }

}
