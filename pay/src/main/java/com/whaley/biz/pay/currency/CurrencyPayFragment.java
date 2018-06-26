package com.whaley.biz.pay.currency;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPDialogFragment;
import com.whaley.biz.pay.PayUtil;
import com.whaley.biz.pay.R;
import com.whaley.biz.pay.R2;
import com.whaley.biz.pay.currency.model.CurrencyModel;
import com.whaley.biz.pay.currency.presenter.CurrencyPayPresenter;
import com.whaley.core.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/21.
 */

@Route(path = "/pay/ui/currencypaydialog")
public class CurrencyPayFragment extends BaseMVPDialogFragment<CurrencyPayPresenter> implements CurrencyPayView {

    @BindView(R2.id.tv_price_tips)
    TextView tvPriceTips;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.layout_alipay)
    FrameLayout layoutAlipay;
    @BindView(R2.id.view_fine_line)
    View viewFineLine;
    @BindView(R2.id.layout_wechat)
    FrameLayout layoutWechat;
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
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_prefer)
    TextView tvPrefer;
    @BindView(R2.id.tv_preferName)
    TextView tvPreferName;
    @BindView(R2.id.tv_price)
    TextView tvPrice;
    @BindView(R2.id.tv_preferPrice)
    TextView tvPreferPrice;

    private AnimatorSet set;

    public static CurrencyPayFragment showDialog(CurrencyModel currencyModel, FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag("currencyPay");
        if (null != fragment) {
            ft.remove(fragment);
        }
        CurrencyPayFragment dialogFragment = CurrencyPayFragment.newInstance(currencyModel);
        ft.add(dialogFragment, "currencyPay");
        ft.commitAllowingStateLoss();
        return dialogFragment;
    }

    public static CurrencyPayFragment newInstance(CurrencyModel currencyModel) {
        CurrencyPayFragment thirdPayFragment = new CurrencyPayFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CurrencyPayPresenter.STR_PARAM_OBJECT, GsonUtil.getGson().toJson(currencyModel));
        thirdPayFragment.setArguments(bundle);
        return thirdPayFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setPayMethod();
        CurrencyModel currencyModel = getPresenter().getRepository().getCurrencyModel();
        tvName.setText(currencyModel.getWhaleyCurrencyNumber()+ "鲸币充值");
        tvPrice.setText("¥" + PayUtil.fromFenToYuan(getPresenter().getRepository().getRealPrice()));
        if(currencyModel.getWhaleyCurrencyGiveNumber()>0){
            tvPreferName.setText("(赠送" + currencyModel.getWhaleyCurrencyGiveNumber() + "金币)");
            tvPreferName.setVisibility(View.VISIBLE);
        }else{
            tvPreferName.setVisibility(View.GONE);
        }
        if(currencyModel.getPreferPrice() > 0){
            tvPrefer.setVisibility(View.VISIBLE);
            tvPreferPrice.setText("¥" + PayUtil.fromFenToYuan(""+currencyModel.getPrice()));
            tvPreferPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvPreferPrice.setVisibility(View.VISIBLE);
        }else{
            tvPrefer.setVisibility(View.GONE);
            tvPreferPrice.setVisibility(View.GONE);
        }
        tvContent.setText("订单日期：" + PayUtil.getDateFromMileSeconds(System.currentTimeMillis()));
        tvPriceTips.setText("待支付");
        tvPriceTips.setSelected(true);
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

    private void setPayMethod() {
        if (getPresenter().getRepository().isAlipay()
                && getPresenter().getRepository().isWechat()) {
            layoutAlipay.setVisibility(View.VISIBLE);
            viewFineLine.setVisibility(View.VISIBLE);
            layoutWechat.setVisibility(View.VISIBLE);
            viewCrudeLine.setVisibility(View.VISIBLE);
            return;
        }
        if (getPresenter().getRepository().isAlipay()
                && !getPresenter().getRepository().isWechat()) {
            layoutAlipay.setVisibility(View.VISIBLE);
            viewFineLine.setVisibility(View.GONE);
            layoutWechat.setVisibility(View.GONE);
            viewCrudeLine.setVisibility(View.VISIBLE);
            return;
        }
        if (!getPresenter().getRepository().isAlipay()
                && getPresenter().getRepository().isWechat()) {
            layoutAlipay.setVisibility(View.GONE);
            viewFineLine.setVisibility(View.GONE);
            layoutWechat.setVisibility(View.VISIBLE);
            viewCrudeLine.setVisibility(View.VISIBLE);
            return;
        }
        if (!getPresenter().getRepository().isAlipay()
                && !getPresenter().getRepository().isWechat()) {
            layoutAlipay.setVisibility(View.GONE);
            viewFineLine.setVisibility(View.GONE);
            layoutWechat.setVisibility(View.GONE);
            viewCrudeLine.setVisibility(View.GONE);
            return;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        getPresenter().onDismiss();
        super.onDismiss(dialog);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dialog_currency;
    }

    @OnClick(R2.id.layout_wechat)
    void onWechat() {
        getPresenter().wechat();
    }

    @OnClick(R2.id.layout_alipay)
    void onAlipay() {
        getPresenter().alipay();
    }

    @OnClick(R2.id.tv_close)
    void onClose() {
        startCloseAnim();
    }

    @OnClick(R2.id.tv_reappear)
    void onReappear() {
        tvReappear.setVisibility(View.GONE);
        tvPriceTips.setText("待支付");
        setPayMethod();
    }

    @Override
    public void onPayfinish() {
        if (getPresenter().getRepository().isPay()) {
            tvPriceTips.setText("支付成功");
            tvPriceTips.setSelected(false);
            tvReappear.setVisibility(View.GONE);
            layoutAlipay.setVisibility(View.GONE);
            viewFineLine.setVisibility(View.GONE);
            layoutWechat.setVisibility(View.GONE);
            viewCrudeLine.setVisibility(View.GONE);
            tvClose.setText("关闭");
        } else {
            tvReappear.setVisibility(View.VISIBLE);
            layoutAlipay.setVisibility(View.GONE);
            viewFineLine.setVisibility(View.GONE);
            layoutWechat.setVisibility(View.GONE);
            tvPriceTips.setText("支付失败");
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        cancelAnim();
    }

}
