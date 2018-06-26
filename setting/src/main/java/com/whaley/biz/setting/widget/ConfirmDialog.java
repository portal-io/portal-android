package com.whaley.biz.setting.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uuzuche.lib_zxing.DisplayUtil;
import com.whaley.biz.setting.R;

public class ConfirmDialog {

    Dialog confirmDlg;
    private Context mContext;
    ConfirmListener confirmListener;
    private String confirmTipLabel = null;
    private String viceTitle;
    private String lableConfirm;
    private boolean showViceTitle = false;
    private boolean showButtons = true;
    private boolean cancelOutSide = false;

    public interface ConfirmListener{
        void onConfirm();
        void onCancel();
    }
    public ConfirmDialog(Context context, String confrimLable, String viceTitle, String lableConfirm, boolean showButtons, boolean cancelOutSide, ConfirmListener confirmListener) {
        this(context, confrimLable, viceTitle, lableConfirm, confirmListener);
        this.showButtons = showButtons;
        this.cancelOutSide = cancelOutSide;
    }
    public ConfirmDialog(Context context, String confrimLable,  String viceTitle, String lableConfirm,ConfirmListener confirmListener) {
        this(context, confrimLable, viceTitle, confirmListener);
        this.lableConfirm = lableConfirm;
    }
    public ConfirmDialog(Context context, String confrimLable, String viceTitle, ConfirmListener confirmListener) {
        this(context, confrimLable, confirmListener);
        this.viceTitle = viceTitle;
        if( ! TextUtils.isEmpty(viceTitle)){
            showViceTitle = true;
        }
    }
    public ConfirmDialog(Context context, String confrimLable, ConfirmListener confirmListener) {
        this(context, confirmListener);
        this.confirmTipLabel = confrimLable;
    }

    public ConfirmDialog(Context context, ConfirmListener confirmListener) {
        this.mContext = context;
        this.confirmListener = confirmListener;
    }

    public void show() {
        if (confirmDlg != null && confirmDlg.isShowing()) {
            return;
        }
        try {

            confirmDlg = new Dialog(mContext, R.style.dialog);
//			confirmDlg = new AlertDialog.Builder(mContext).create();
        } catch (Exception e) {
            return;
        }
        Window window = confirmDlg.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DisplayUtil.dip2px(mContext, WindowManager.LayoutParams.MATCH_PARENT);
//		params.height = Util.dip2px(mContext, 233);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        final View viewDialog = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm, null);
        TextView text = (TextView) viewDialog.findViewById(R.id.text);
        TextView viceTitle = (TextView) viewDialog.findViewById(R.id.tv_vice_title);
        TextView iv_confirm = (TextView) viewDialog.findViewById(R.id.iv_confirm);
        TextView iv_cancel = (TextView) viewDialog.findViewById(R.id.iv_cancle);
        View line = viewDialog.findViewById(R.id.view_line);
        View buttons = viewDialog.findViewById(R.id.confirm_layout);
        if(showButtons){
            line.setVisibility(View.VISIBLE);
            buttons.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.INVISIBLE);// hold the width : )
            buttons.setVisibility(View.GONE);
        }
        if(! TextUtils.isEmpty(lableConfirm)){
            iv_confirm.setText(lableConfirm);
        }
        if(! TextUtils.isEmpty(confirmTipLabel)){
            text.setText(confirmTipLabel);
        }
        if(showViceTitle){
            viceTitle.setVisibility(View.VISIBLE);
            viceTitle.setText(this.viceTitle);
        } else {
            viceTitle.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)text.getLayoutParams();
            lp.topMargin = 0;
            text.setLayoutParams(lp);

        }
        iv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmListener.onCancel();
                confirmDlg.dismiss();
            }
        });
        iv_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmListener.onConfirm();
                confirmDlg.dismiss();
            }
        });
        confirmDlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                confirmListener.onCancel();
                confirmDlg.dismiss();
            }
        });
        confirmDlg.setContentView(viewDialog);
        confirmDlg.setCanceledOnTouchOutside(cancelOutSide);
        confirmDlg.show();
    }



}

