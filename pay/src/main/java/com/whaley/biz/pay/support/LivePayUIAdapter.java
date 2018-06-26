package com.whaley.biz.pay.support;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.whaley.biz.pay.R;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.core.utils.DisplayUtil;

/**
 * Created by YangZhi on 2017/9/7 19:52.
 */

public class LivePayUIAdapter extends BaseUIAdapter<LivePayController> implements PayController.IPayUIAdapter<LivePayController>{

    ViewStub viewStub;

    View inflatedLayout;

    View needPayLayout;

    View promptLayout;

    TextView tvLivePrompt;

    String promptText;

    TextView tvNeedPayName;

    TextView tvNeedPayBuy;

    TextView tvNeedPayTip;


    @Override
    public View getRootView() {
        return inflatedLayout == null ? super.getRootView() : inflatedLayout;
    }

    @Override
    public void changeNeedPayOnLandScape() {

    }

    @Override
    public void changeNeedPayOnPortrait() {

    }

    @Override
    public void showNeedPay() {
        checkInflate();
        hidePrompt();
        needPayLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNeedPay() {
        checkInflate();
        needPayLayout.setVisibility(View.GONE);
    }

    @Override
    public void showPrompt() {
        checkInflate();
        promptLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePrompt() {
        if (promptLayout == null)
            return;
        promptLayout.setVisibility(View.GONE);
    }

    @Override
    public void changePromptText(String promptText) {
        this.promptText = promptText;
        if (tvLivePrompt == null)
            return;
        tvLivePrompt.setText(promptText);
    }

    @Override
    public void showLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("该账号已在其他设备上登录，您已被迫登出");
        builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getController().onBackClick();
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    getController().onBackClick();
                    return true;
                } else {
                    return false;
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void updateNeedPayText(String needPayNameStr,String needPayBuyStr){
        tvNeedPayName.setText(needPayNameStr);
        tvNeedPayBuy.setText(needPayBuyStr);
    }


    private void checkInflate() {
        if (inflatedLayout == null) {
            inflatedLayout = viewStub.inflate();
            needPayLayout = inflatedLayout.findViewById(R.id.layout_need_pay);
            tvNeedPayTip = (TextView) needPayLayout.findViewById(R.id.tv_need_pay_tip);
            tvNeedPayBuy = (TextView) needPayLayout.findViewById(R.id.tv_need_pay_buy);
            tvNeedPayName = (TextView) needPayLayout.findViewById(R.id.tv_need_pay_name);

            promptLayout = inflatedLayout.findViewById(R.id.layout_live_prompt);
            tvLivePrompt = (TextView) promptLayout.findViewById(R.id.tv_live_prompt);
            tvLivePrompt.setText(promptText);
            promptLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onPayClick();
                }
            });

            tvNeedPayBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onPayClick();
                }
            });

            tvNeedPayTip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onNeedPayTipClick();
                }
            });
        }
    }

    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), R.layout.layout_live_player_need_pay);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewStub.setLayoutParams(layoutParams);
        return viewStub;
    }

    public void updatePromptMarginBottom(int height){
        if(promptLayout==null)
            return;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) promptLayout.getLayoutParams();
        layoutParams.bottomMargin = height+ DisplayUtil.convertDIP2PX(52);
        promptLayout.requestLayout();
    }

    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;
    }

    @Override
    public void destroy() {

    }
}
