package com.whaley.biz.common.widget.keyboardHelper;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.utils.DisplayUtil;

public class KeyBoardHelper {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    public static KeyBoardHelper assistActivity (Activity activity,  boolean isBelowStatusBar,boolean isResize,OnKeyBoradChangeListener onKeyBoradChangeListener) {
        return new KeyBoardHelper(activity,isBelowStatusBar,isResize,onKeyBoradChangeListener);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private int usableWidthPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    private OnKeyBoradChangeListener onKeyBoradChangeListener;

    private boolean isShowed;

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    private boolean isBelowStatusBar;

    private boolean isResize;

    private KeyBoardHelper(Activity activity, boolean isBelowStatusBar,boolean isResize,OnKeyBoradChangeListener onKeyBoradChangeListener) {
        this.onKeyBoradChangeListener=onKeyBoradChangeListener;
        this.isBelowStatusBar=isBelowStatusBar;
        this.isResize=isResize;
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        destory();
        onGlobalLayoutListener=new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        };
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }


    public void setResize(boolean resize) {
        isResize = resize;
    }

    private void possiblyResizeChildOfContent() {
        int screenHeight=mChildOfContent.getRootView().getHeight()- (isBelowStatusBar? DisplayUtil.getStatusHeight(AppContextProvider.getInstance().getContext()):0);

        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = screenHeight;
//                    mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard/4)) {
                // keyboard probably just became visible
                if(isResize)
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;

                if(onKeyBoradChangeListener!=null){
                    onKeyBoradChangeListener.onShowKeyboard(screenHeight-usableHeightNow);
                    mChildOfContent.requestLayout();
                }
                isShowed=true;
            } else {
                // keyboard probably just became hidden
                if(isResize) {
                    frameLayoutParams.height = usableHeightSansKeyboard;
                    mChildOfContent.requestLayout();
                }
                if(isShowed) {
                    if (onKeyBoradChangeListener != null) {
                        onKeyBoradChangeListener.onHideKeyboard();
                    }
                }
            }
            usableHeightPrevious = usableHeightNow;
        }


    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - (isBelowStatusBar?r.top:0));
    }

    private int computeUsableWidth() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.right - r.left);
    }


    public void destory(){
        if(mChildOfContent!=null&&mChildOfContent.getViewTreeObserver()!=null)
            mChildOfContent.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
    }

}