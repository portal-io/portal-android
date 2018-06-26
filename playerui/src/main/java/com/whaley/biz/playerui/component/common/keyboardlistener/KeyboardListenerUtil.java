package com.whaley.biz.playerui.component.common.keyboardlistener;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by YangZhi on 2017/9/6 19:45.
 */

public class KeyboardListenerUtil {

    private Activity activity;

    private View decorView;

    private IKeyBoardVisibleListener listener;

    private boolean isVisiableForLast = false;

    private int heightDifference = 0;

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            //计算出可见屏幕的高度
            int displayHight = rect.bottom - rect.top;
            //获得屏幕整体的高度
            int hight = decorView.getHeight();
            //获得键盘高度
            int keyboardHeight = hight - displayHight;
            boolean visible = (double) displayHight / hight < 0.8;
            if (visible != isVisiableForLast) {
                listener.onSoftKeyBoardVisible(visible, displayHight);
            }
            if (heightDifference == 0 && !visible) {
                heightDifference = keyboardHeight;
            }
            isVisiableForLast = visible;
        }
    };


    public KeyboardListenerUtil(Activity activity, IKeyBoardVisibleListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.decorView = activity.getWindow().getDecorView();
        addOnSoftKeyBoardVisibleListener();
    }

    public static KeyboardListenerUtil bind(Activity activity, IKeyBoardVisibleListener listener){
        return new KeyboardListenerUtil(activity,listener);
    }

    private void addOnSoftKeyBoardVisibleListener() {
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public void unbind(){
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public interface IKeyBoardVisibleListener {
        void onSoftKeyBoardVisible(boolean visible, int visibleHeight);
    }
}
