package com.whaley.biz.pay.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPDialogFragment;
import com.whaley.biz.pay.R;
import com.whaley.biz.pay.R2;
import com.whaley.biz.pay.model.CouponModel;
import com.whaley.biz.pay.model.ThirdPayModel;
import com.whaley.biz.pay.ui.adapter.ThirdPayAdapter;
import com.whaley.biz.pay.ui.adapter.ThirdPayView;
import com.whaley.biz.pay.ui.presenter.ThirdPayPresenter;
import com.whaley.core.utils.DateUtils;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/21.
 */

@Route(path = "/pay/ui/paydialog")
public class ThirdPayFragment extends BaseMVPDialogFragment<ThirdPayPresenter> implements ThirdPayView {

    @BindView(R2.id.view_recyclerView)
    RecyclerView recyclerView;
    //    @BindView(R2.id.tv_price_tips)
//    TextView tvPriceTips;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.tv_whaley_currency_over)
    TextView whaleyCurrencyOver;

    @BindView(R2.id.view_crude_line)
    View viewCrudeLine;
    @BindView(R2.id.tv_close)
    TextView tvClose;
    @BindView(R2.id.tv_reappear)
    TextView tvReappear;
    @BindView(R2.id.view_pay_btn)
    LinearLayout viewPayBtn;
    @BindView(R2.id.rl_redeem)
    FrameLayout rlRedeem;
    @BindView(R2.id.view_bg)
    FrameLayout viewBg;
    @BindView(R2.id.layout_redeem_select_box)
    View layout_redeem_select_box;

    @BindView(R2.id.vs_pay_view)
    ViewStub vsPayView;
    @BindView(R2.id.vs_three_kinds_pay_view)
    ViewStub vsThreeKindsPayView;

    View layoutAlipay;
    View layoutWechat;
    View layoutWhaleyCurrency;
    View payView;

    private ThirdPayAdapter thirdPayAdapter;
    private AnimatorSet set;

    public static ThirdPayFragment showDialog(String code, List<CouponModel> packsCoupons, String content, FragmentManager fm, int displayMode, boolean isUnity, String type) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag("thirdPay");
        if (null != fragment) {
            ft.remove(fragment);
        }
        ThirdPayFragment dialogFragment = ThirdPayFragment.newInstance(code, packsCoupons, content, displayMode, isUnity, type);
        ft.add(dialogFragment, "thirdPay");
        ft.commitAllowingStateLoss();
        return dialogFragment;
    }

    public static ThirdPayFragment newInstance(String code, List<CouponModel> packsCoupons, String content, int displayMode, boolean isUnity, String type) {
        ThirdPayFragment thirdPayFragment = new ThirdPayFragment();
        Bundle bundle = new Bundle();
        ThirdPayModel thirdPayModel = new ThirdPayModel();
        thirdPayModel.setCode(code);
        thirdPayModel.setPacksCoupons(packsCoupons);
        thirdPayModel.setContent(content);
        thirdPayModel.setDisplayMode(displayMode);
        thirdPayModel.setUnity(isUnity);
        thirdPayModel.setType(type);
        bundle.putString(ThirdPayPresenter.STR_PARAM_OBJECT, GsonUtil.getGson().toJson(thirdPayModel));
        thirdPayFragment.setArguments(bundle);
        return thirdPayFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        getPresenter().setDisplayMode(getActivity().getResources().getConfiguration().orientation);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setPayMethod();
        List<CouponModel> couponModels = getPresenter().getRepository().getCouponModels();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        thirdPayAdapter = new ThirdPayAdapter();
        recyclerView.setAdapter(thirdPayAdapter);
        thirdPayAdapter.setUnity(getPresenter().getRepository().isUnity());
        thirdPayAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder iViewHolder, int position) {
                if (thirdPayAdapter.isOnlyOne()) {
                    return;
                }
                getPresenter().onItemClick(position);
            }
        });
        thirdPayAdapter.setPayPackgeClickListener(new ThirdPayAdapter.PayPackgeClickListener() {
            @Override
            public void onPackgeClick(CouponModel couponModel) {
                getPresenter().onPackgeClick(couponModel);
                dismissAllowingStateLoss();
            }
        });
        if (getPresenter().getRepository().isWhaleyCurrency()) {
            thirdPayAdapter.setHaveWhaleyPay(true);
            whaleyCurrencyOver.setText(String.format(getContext().getString(R.string.whaley_currency_over), getPresenter().getRepository().getWhaleyMoney()));
        }
        setData(couponModels);
        String content = getPresenter().getRepository().getContent();
        int contentSize;
        if (StrUtil.isEmpty(content)) {
            contentSize=8;
            content = "订单日期:" + DateUtils.foramteToDate(System.currentTimeMillis(), DateUtils.YYYYMMDD_NYR);
        }else {
            contentSize=content.length();
        }
        tvContent.setText(content);
        if(whaleyCurrencyOver.getText().length()+contentSize>25){
            whaleyCurrencyOver.setTextSize(10);
            tvContent.setTextSize(10);
        }
//        tvPriceTips.setText("待支付");
//        tvPriceTips.setSelected(true);
        ViewTreeObserver vto = rlRedeem.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (rlRedeem == null)
                    return;
                rlRedeem.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startAnim();
            }
        });
    }

    private void setData(List<CouponModel> couponModels) {
        if (couponModels != null && couponModels.size() > 0) {
            if (couponModels.size() == 1) {
                thirdPayAdapter.setOnlyOne(true);
            } else {
                thirdPayAdapter.setOnlyOne(false);
            }
            thirdPayAdapter.setData(couponModels);
        }
        updateContentViewVisible(!"content_packge".equals(couponModels.get(0).getRelatedType()));
    }

    private void setPayMethod() {
        if (payView != null) {
            payView.setVisibility(View.VISIBLE);
            return;
        }
        boolean isHaveWhaleyCurrency=false;
        if (getPresenter().getRepository().isWhaleyCurrency()) {
            isHaveWhaleyCurrency = getPresenter().getRepository().getCouponModel().getJingbiPrice() <= getPresenter().getRepository().getWhaleyMoney();
        }
        if (getPresenter().getRepository().isThreeKindsPay()&&isHaveWhaleyCurrency) {
            if (payView == null) {
                payView = vsThreeKindsPayView.inflate();
                initializeView();
            }
            layoutAlipay.setVisibility(View.VISIBLE);
            layoutWechat.setVisibility(View.VISIBLE);
            layoutWhaleyCurrency.setVisibility(View.VISIBLE);
            viewCrudeLine.setVisibility(View.VISIBLE);
        } else {
            if (payView == null) {
                payView = vsPayView.inflate();
                initializeView();
            }
            if (getPresenter().getRepository().isAlipay()) {
                layoutAlipay.setVisibility(View.VISIBLE);
            }
            if (getPresenter().getRepository().isWechat()) {
                layoutWechat.setVisibility(View.VISIBLE);
            }
            if (getPresenter().getRepository().isWhaleyCurrency()&&isHaveWhaleyCurrency) {
                layoutWhaleyCurrency.setVisibility(View.VISIBLE);
            }
            if (getPresenter().getRepository().isAlipay() || getPresenter().getRepository().isWechat() || getPresenter().getRepository().isWhaleyCurrency()) {
                viewCrudeLine.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initializeView() {
        layoutAlipay = payView.findViewById(R.id.layout_alipay);
        layoutWechat = payView.findViewById(R.id.layout_wechat);
        layoutWhaleyCurrency = payView.findViewById(R.id.layout_jingbi);
        layoutAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().alipay();
            }
        });
        layoutWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().wechat();
            }
        });
        layoutWhaleyCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().jinbi();
            }
        });

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        getPresenter().onDismiss();
        if (getPresenter().getRepository().getDisplayMode() == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onDismiss(dialog);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dialog_pay;
    }

//    @OnClick(R2.id.layout_wechat)
//    void onWechat() {
//
//    }
//
//    @OnClick(R2.id.layout_alipay)
//    void onAlipay() {
//
//    }
//
//    @OnClick(R2.id.layout_jingbi)
//    void onJingbi() {
//
//    }

    @OnClick(R2.id.tv_close)
    void onClose() {
        startCloseAnim();
    }

    @OnClick(R2.id.tv_reappear)
    void onReappear() {
        getPresenter().getRepository().getCouponModel().showPayType = CouponModel.NOT_PAY;
        setData(getPresenter().getRepository().getCouponModels());
        tvReappear.setVisibility(View.GONE);
//        tvPriceTips.setText("待支付");
        setPayMethod();
    }

    @Override
    public void onPayfinish() {
        CouponModel couponModel = getPresenter().getRepository().getCouponModel();
        if (getPresenter().getRepository().getPayEventModel().isPayed()) {
//            tvPriceTips.setText("支付成功");
//            tvPriceTips.setSelected(false);
            if (getPresenter().getRepository().isWhaleyCurrency()) {
                whaleyCurrencyOver.setText(String.format(getContext().getString(R.string.whaley_currency_over), getPresenter().getRepository().getWhaleyMoney()));
            }
            payView.setVisibility(View.GONE);
            viewCrudeLine.setVisibility(View.GONE);
            tvClose.setText("关闭");
            couponModel.showPayType = CouponModel.PAY_SUCCESS;
        } else {
            tvReappear.setVisibility(View.VISIBLE);
            payView.setVisibility(View.GONE);
            couponModel.showPayType = CouponModel.PAY_FAIL;
//            tvPriceTips.setText("支付失败");
        }
        List<CouponModel> couponModels = new ArrayList<>();
        couponModels.add(couponModel);
        setData(couponModels);
    }


    private void initAnimPosition() {
        viewBg.setVisibility(View.INVISIBLE);
        layout_redeem_select_box.setVisibility(View.INVISIBLE);
        viewPayBtn.setVisibility(View.INVISIBLE);
        viewBg.setAlpha(0f);
        layout_redeem_select_box.setAlpha(0f);
        layout_redeem_select_box.setTranslationY(100f);
        viewPayBtn.setTranslationY(viewPayBtn.getMeasuredHeight());
    }


    private void startCloseAnim() {
        cancelAnim();
        List<Animator> animatorList = new ArrayList<>();
        ObjectAnimator bgAnim = ObjectAnimator.ofFloat(viewBg, "alpha", viewBg.getAlpha(), 0f);
        bgAnim.setDuration(100);
        bgAnim.setStartDelay(250);
        animatorList.add(bgAnim);

        ObjectAnimator rlRedeemAlphaAnim = ObjectAnimator.ofFloat(layout_redeem_select_box, "alpha", layout_redeem_select_box.getAlpha(), 0f);
        rlRedeemAlphaAnim.setDuration(100);
        animatorList.add(rlRedeemAlphaAnim);

        ObjectAnimator rlRedeemTransYAnim = ObjectAnimator.ofFloat(layout_redeem_select_box, "translationY", layout_redeem_select_box.getTranslationY(), 100f);
        rlRedeemTransYAnim.setDuration(100);
        animatorList.add(rlRedeemTransYAnim);

        ObjectAnimator layoutPayBtnsTransYAnim = ObjectAnimator.ofFloat(viewPayBtn, "translationY", viewPayBtn.getTranslationY(), viewPayBtn.getMeasuredHeight());
        layoutPayBtnsTransYAnim.setDuration(250);

        animatorList.add(layoutPayBtnsTransYAnim);

        set = new AnimatorSet();
        set.playTogether(animatorList);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {
                animation.removeAllListeners();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animation.removeAllListeners();
                dismissAllowingStateLoss();
            }
        });
        set.start();
    }

    private void startAnim() {
        initAnimPosition();
        cancelAnim();
        List<Animator> animatorList = new ArrayList<>();
        ObjectAnimator bgAnim = ObjectAnimator.ofFloat(viewBg, "alpha", 0f, 1f);
        bgAnim.setDuration(100);
        animatorList.add(bgAnim);

        ObjectAnimator rlRedeemAlphaAnim = ObjectAnimator.ofFloat(layout_redeem_select_box, "alpha", 0f, 1f);
        rlRedeemAlphaAnim.setDuration(100);
        animatorList.add(rlRedeemAlphaAnim);

        ObjectAnimator rlRedeemTransYAnim = ObjectAnimator.ofFloat(layout_redeem_select_box, "translationY", 100f, 0f);
        rlRedeemTransYAnim.setDuration(250);
        animatorList.add(rlRedeemTransYAnim);

        ObjectAnimator layoutPayBtnsTransYAnim = ObjectAnimator.ofFloat(viewPayBtn, "translationY", viewPayBtn.getMeasuredHeight(), 0f);
        layoutPayBtnsTransYAnim.setDuration(250);
        layoutPayBtnsTransYAnim.setStartDelay(100);
        animatorList.add(layoutPayBtnsTransYAnim);

        set = new AnimatorSet();
        set.playTogether(animatorList);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                viewBg.setVisibility(View.VISIBLE);
                layout_redeem_select_box.setVisibility(View.VISIBLE);
                viewPayBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animation.removeAllListeners();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animation.removeAllListeners();
            }
        });
        set.start();
    }

    private void cancelAnim() {
        if (set != null && set.isStarted()) {
            set.removeAllListeners();
            set.cancel();
        }
    }

    @Override
    public void updataView() {
        thirdPayAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateContentViewVisible(boolean visible) {
//        if (visible) {
//            tvContent.setVisibility(View.VISIBLE);
//        } else {
//            tvContent.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancelAnim();
    }

}
