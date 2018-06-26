package com.whaley.biz.pay.support;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.whaley.biz.pay.R;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/17 20:20.
 */

public class PayUIAdapter extends BaseUIAdapter<PayController> implements PayController.IPayUIAdapter<PayController>{

    ViewStub viewStub;
    View inflatedLayout;
    View needPayLayout;
    TextView tvPrice;
    Button btnPay;
    TextView btnReturn;
    TextView tvPrompt;

    boolean isLandScape;

    String promptText;


    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), R.layout.layout_player_need_pay);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewStub.setLayoutParams(layoutParams);
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

    public void changeNeedPayOnLandScape() {
        isLandScape = true;
        if (btnPay == null)
            return;
        btnPay.setVisibility(View.VISIBLE);
    }

    public void changeNeedPayOnPortrait() {
        isLandScape = false;
        if (btnPay == null)
            return;
        btnPay.setVisibility(View.GONE);
    }

    public void showNeedPay() {
        checkInflate();
        hidePrompt();
        needPayLayout.setVisibility(View.VISIBLE);
    }

    public void hideNeedPay() {
        checkInflate();
        needPayLayout.setVisibility(View.GONE);
    }

    public void showPrompt() {
        checkInflate();
        tvPrompt.setVisibility(View.VISIBLE);
    }

    public void hidePrompt() {
        if (tvPrompt == null)
            return;
        tvPrompt.setVisibility(View.GONE);
    }

    public void changePromptText(String promptText) {
        this.promptText = promptText;
        if (tvPrompt == null)
            return;
        tvPrompt.setText(promptText);
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

    private void checkInflate() {
        if (inflatedLayout == null) {
            inflatedLayout = viewStub.inflate();
            needPayLayout = inflatedLayout.findViewById(R.id.needpay);
            tvPrice = (TextView) inflatedLayout.findViewById(R.id.tv_price);
            tvPrice.setText("试看已结束，请购买观看券观看完整视频");
            btnPay = (Button) inflatedLayout.findViewById(R.id.btn_pay);
            btnReturn = (TextView) inflatedLayout.findViewById(R.id.btn_return);
            btnPay.setVisibility(isLandScape ? View.VISIBLE : View.GONE);
            tvPrompt = (TextView) inflatedLayout.findViewById(R.id.tv_prompt);
            tvPrompt.setText(promptText);

            btnReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onRetryWatchClick();
                }
            });
            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onPayClick();
                }
            });
            tvPrompt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onPayClick();
                }
            });
        }
    }

    @Override
    public void destroy() {

    }
}
