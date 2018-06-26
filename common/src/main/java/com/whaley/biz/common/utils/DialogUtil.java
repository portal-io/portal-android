package com.whaley.biz.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.whaley.biz.common.R;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.StrUtil;

/**
 * Created by YangZhi on 2016/8/18 12:31.
 */
public class DialogUtil {

    public static Dialog showWifiDialog(Context context, final View.OnClickListener confirmListener, final View.OnClickListener cancelListener) {
        return showDialog(context, context.getResources().getString(R.string.prompt), null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmListener != null) {
                    confirmListener.onClick(view);
                }

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancelListener != null) {
                    cancelListener.onClick(view);
                }
            }
        });
    }

    public static Dialog showDialog(Context context, String name, View.OnClickListener confirmListener) {
        return showDialog(context, name, null, confirmListener, null);
    }

    public static Dialog showDialog(Context context, String name, View.OnClickListener confirmListener, View.OnClickListener cancelListener) {
        return showDialog(context, name, null, confirmListener, cancelListener);
    }

    public static Dialog showDialog(Context context, String name, String content, View.OnClickListener confirmListener) {
        return showDialogCustomConfirm(context, name, content, null, confirmListener, null);
    }

    public static Dialog showDialog(Context context, String name, String content, View.OnClickListener confirmListener, View.OnClickListener cancelListener) {
        return showDialogCustomConfirm(context, name, content, null, confirmListener, cancelListener);
    }

    public static Dialog showDialogCustomConfirm(Context context, String name, String content, String confirmLabel, final View.OnClickListener confirmListener, final View.OnClickListener cancelListener) {
        return showDialogCustomConfirm(context, name, content, confirmLabel, null, confirmListener, cancelListener);
    }


    /**
     * 默认双按钮
     *
     * @param context
     * @param name            标题
     * @param content         内容
     * @param confirmLabel    按钮1文字
     * @param cancelLabel     按钮2文字
     * @param confirmListener 按钮1事件监听
     * @param cancelListener  按扭2事件监听
     * @return
     */

    public static Dialog showDialogCustomConfirm(Context context, String name, String content, String confirmLabel, String cancelLabel, final View.OnClickListener confirmListener, final View.OnClickListener cancelListener) {
        return showDialogCustomConfirm(context, name, content, confirmLabel, cancelLabel, confirmListener, cancelListener, false, false);
    }


    /**
     * 单按钮单标题栏
     *
     * @param context
     * @param name
     * @return
     */
    public static Dialog showDialog(Context context, String name) {
        return showDialogCustomConfirm(context, name, "", null, null, null, null, true, false);
    }

    public static Dialog showDialogCustomConfirm(Context context, String name, String content, String confirmLabel, String cancelLabel, final View.OnClickListener confirmListener, final View.OnClickListener cancelListener, boolean oneButton){
        return showDialogCustomConfirm(context, name, content, confirmLabel, cancelLabel, confirmListener, cancelListener, oneButton, false);
    }

    /**
     * @param context
     * @param name            标题
     * @param content         内容
     * @param confirmLabel    按钮1文字
     * @param cancelLabel     按钮2文字
     * @param confirmListener 按钮1事件监听
     * @param cancelListener  按扭2事件监听
     * @param oneButton       是否只有一个按钮
     * @return
     */
    public static Dialog showDialogCustomConfirm(Context context, String name, String content, String confirmLabel, String cancelLabel, final View.OnClickListener confirmListener, final View.OnClickListener cancelListener, boolean oneButton, final boolean isReverse) {
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_text_true_and_false, null);
        final Dialog dialog = new Dialog(context, R.style.transparentDialog);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (DisplayUtil.screenW * 960.0f / 1080); // 宽度
        dialog.getWindow().setAttributes(lp);
        TextView nameTextView = (TextView) view.findViewById(R.id.dialog_textview_name);
        TextView contentTextView = (TextView) view.findViewById(R.id.dialog_textview_content);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView ok = (TextView) view.findViewById(R.id.tv_ok);
        View buttons = view.findViewById(R.id.layout_buttons);
        if (oneButton) {
            buttons.setVisibility(View.GONE);
            ok.setVisibility(View.VISIBLE);
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmListener != null) {
                    confirmListener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        if (!TextUtils.isEmpty(confirmLabel)) {
            confirm.setText(confirmLabel);
        }
        if (!TextUtils.isEmpty(cancelLabel)) {
            cancel.setText(cancelLabel);
        }
        nameTextView.setText(name);
        if (!StrUtil.isEmpty(content)) {
            contentTextView.setVisibility(View.VISIBLE);
            contentTextView.setText(content);
        }
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmListener != null) {
                    confirmListener.onClick(view);
                }
                dialog.dismiss();
            }
        });
        cancel.
                setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View arg0) {
                                           if (cancelListener != null)
                                               cancelListener.onClick(view);
                                           dialog.dismiss();
                                       }
                                   }
                );
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(isReverse){
                    if (confirmListener != null) {
                        confirmListener.onClick(view);
                    }
                }else {
                    if (cancelListener != null)
                        cancelListener.onClick(view);
                }
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        if(context instanceof Activity && ((Activity) context).isFinishing()){
            //
        }else{
            dialog.show();
        }
        return dialog;
    }


    public interface AvatarDialogListener {
        void onClickCamera();

        void onClickGallery();

        void onClickCancel();
    }

    public static void showFetchAvatarDialog(final Activity context, final AvatarDialogListener listener) {
        final View viewDialog = context.getLayoutInflater().inflate(R.layout.dialog_fetch_avatar, null);
        final TextView btnFromCamera = (TextView) viewDialog.findViewById(R.id.tv_from_camera);
        final TextView btnFromGallery = (TextView) viewDialog.findViewById(R.id.tv_from_gallery);
        final TextView btnFromCance = (TextView) viewDialog.findViewById(R.id.tv_cancel);
        final Dialog builder = new Dialog(context, R.style.dialog);
        builder.setContentView(viewDialog);
        btnFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickCamera();
                builder.cancel();
            }
        });
        btnFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickGallery();
                builder.cancel();
            }
        });
        btnFromCance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickCancel();
                builder.cancel();
            }
        });
        builder.show();
    }
}

