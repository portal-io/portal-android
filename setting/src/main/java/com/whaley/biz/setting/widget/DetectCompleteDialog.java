package com.whaley.biz.setting.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.uuzuche.lib_zxing.DisplayUtil;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.biz.setting.R;

public class DetectCompleteDialog {

	Dialog confirmDlg;
	private Context mContext;
	ConfirmListener confirmListener;
	private boolean cancelOutSide = false;
	private int clearLevel;

	public interface ConfirmListener{
	    void onConfirm();
		void onCancel();
	}

	public DetectCompleteDialog(Context context, int clearLevel, ConfirmListener confirmListener) {
		this(context, confirmListener);
        this.clearLevel = clearLevel;
		SharedPreferencesUtil.setDefinitionLevel(clearLevel);
    }
	
	public DetectCompleteDialog(Context context, ConfirmListener confirmListener) {
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
		final View viewDialog = LayoutInflater.from(mContext).inflate(R.layout.dialog_recomend_clear_level, null);
		TextView iv_confirm = (TextView) viewDialog.findViewById(R.id.iv_confirm);
		TextView tv_level = (TextView) viewDialog.findViewById(R.id.tv_level);
		final CheckBox checkBox = (CheckBox) viewDialog.findViewById(R.id.cb_checkbox);
		View layoutCheckBox = viewDialog.findViewById(R.id.layout_checkbox);
		layoutCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				checkBox.setChecked( ! checkBox.isChecked());
			}
		});
		switch (clearLevel){
			case SharedPreferencesUtil.DEFINITION_LEVEL_ST:
				tv_level.setText("推荐清晰度：" + mContext.getResources().getString(R.string.text_level_1));
				break;
			case SharedPreferencesUtil.DEFINITION_LEVEL_SD:
				tv_level.setText("推荐清晰度：" + mContext.getResources().getString(R.string.text_level_2));
				break;
		}
		final int oriLevel = SharedPreferencesUtil.getDefinitionLevel();
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					SharedPreferencesUtil.setDefinitionLevel(clearLevel);
				} else {
					SharedPreferencesUtil.setDefinitionLevel(oriLevel);
				}
			}
		});
		iv_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				confirmListener.onConfirm();
				confirmDlg.dismiss();
			}
		});
		confirmDlg.setContentView(viewDialog);
		confirmDlg.setCanceledOnTouchOutside(cancelOutSide);
		confirmDlg.show();
	}

	

}
