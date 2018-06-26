package com.whaley.biz.setting.widget.address;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.whaley.biz.setting.R;
import com.whaley.biz.setting.widget.address.utils.Dev;

public class BottomDialog extends Dialog  implements AddressSelector.CloseDialogClickListener{
    public AddressSelector selector;

    public BottomDialog(Context context) {
        super(context, R.style.bottom_dialog);
        init(context);
    }

    public BottomDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public BottomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        selector = new AddressSelector(context);
        setContentView(selector.getView());
        selector.setCloseDialogClick(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = Dev.dp2px(context, 300);
        params.height = Dev.dp2px(context, 400);
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
    }

    public void setOnAddressSelectedListener(OnAddressSelectedListener listener) {
        this.selector.setOnAddressSelectedListener(listener);
    }

    public static BottomDialog show(Context context) {
        return show(context, null);
    }

    public static BottomDialog show(Context context, OnAddressSelectedListener listener) {
        BottomDialog dialog = new BottomDialog(context, R.style.bottom_dialog);
        dialog.selector.setOnAddressSelectedListener(listener);
        dialog.show();

        return dialog;
    }

    @Override
    public void dismiss() {

        super.dismiss();
    }
    public void closeDB(){
        if (selector != null) {
            selector.closeDB();
        }
    }

    @Override
    public void CloseDialog() {
        this.dismiss();
    }
}
