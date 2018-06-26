package com.whaley.biz.common.widget.emptylayout;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whaley.biz.common.R;
import com.whaley.biz.common.exception.ResponseErrorException;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.widget.titlebar.ITitleBar;
import com.whaleyvr.core.network.http.exception.NetworkErrorException;

/**
 * Created by yangzhi on 16/8/8.
 */
public class EmptyDisplayLayout extends FrameLayout implements EmptyDisplayView {

    private View emptyView;

    private View loadingView;

    private View errorView;

    private Editor editor;

    private Starter backCaller;

    private ITitleBar titleBar;

    private Drawable bgGround;

    private Drawable drawableBack;

    private int visibleRight = -1;

    public int getTitleBarHeight() {
        return titleBarHeight;
    }

    public void setTitleBarHeight(int titleBarHeight) {
        this.titleBarHeight = titleBarHeight;
    }

    private int titleBarHeight;

    private OnRetryListener onRetryListener;

    private Animation anim;

    private boolean hideImage = false;

    ObjectAnimator loadingAnimator;

    public EmptyDisplayLayout(Context context) {
        this(context, null);
    }

    public EmptyDisplayLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyDisplayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        initEmpty();
//        initError();
//        initLoading();
    }

    private <R extends View> R getView(View parent, @IdRes int id) {
        return (R) parent.findViewById(id);
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public void setStarter(Starter starter) {
        this.backCaller = starter;
    }

    public void setTitleBar(ITitleBar titleBar) {
        this.titleBar = titleBar;
    }

    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }

    private void initEditor() {
        if (this.editor == null) {
            editor = new Editor() {
                @Override
                public void changeLoadingShow(View loadingView, String text) {
//                    getView(loadingView,R.id.iv_img);
                }

                @Override
                public void changeErrorShow(View errorView, Throwable error) {
                    TextView tv_error = getView(errorView, R.id.tv_error);
                    String text;
                    if(tv_error==null)
                        return;
                    if (error != null && error.getMessage() != null) {
                        if (error instanceof ResponseErrorException) {
                            text = "数据返回错误";
                        } else if (error instanceof NetworkErrorException) {
                            text = error.getMessage();
                        } else {
                            text = error.getMessage();
                        }
                    } else {
                        if(hideImage) {
                            text = "未获取到内容，点击页面重新加载";
                        }else {
                            text = "未知错误";
                        }
                    }
                    tv_error.setText(text);
                }
            };
        }
    }


    private void initEmpty() {
        initError();
    }

    public void hideImage(){
        hideImage = true;
        if(hideImage&&errorView!=null){
            getView(errorView, R.id.iv_img).setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)getView(errorView, R.id.tv_error).getLayoutParams();
            lp.topMargin = 0;
            getView(errorView, R.id.tv_error).setLayoutParams(lp);
        }
    }

    private void initLoading() {
        if (loadingView == null) {
            loadingView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_loading, this, false);
            addView(loadingView);
            loadingView.setVisibility(GONE);
        }
    }

    private void initAnim() {
        if(loadingAnimator!=null){
            loadingAnimator.cancel();
        }
        View iv_loading = loadingView.findViewById(R.id.iv_loading);
        loadingAnimator=ObjectAnimator.ofFloat(iv_loading,View.ROTATION,0,359);
        loadingAnimator.setDuration(1000);
        loadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnimator.start();
    }

    private void initError() {
        if (errorView == null) {
            errorView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_error, this, false);
            errorView.setTop(titleBarHeight);
            getView(errorView, R.id.reset_layout).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRetryListener != null)
                        onRetryListener.onRetry();
                }
            });
            addView(errorView);
            errorView.setVisibility(GONE);
            if(hideImage)
                hideImage();
            emptyView=errorView;
        }
    }


    public void showView(View view) {
        if(emptyView!=view)
            hideEmpty();
        if(errorView!=view)
            hideError();
        if(loadingView!=view)
            hideLoading();
        else {
            initAnim();
        }
        if(view.getParent()==null){
            addView(view);
        }
        view.setVisibility(VISIBLE);
    }


    @Override
    public void showEmpty() {
        if(emptyView!=null&&emptyView!=errorView) {
            showView(emptyView);
            return;
        }
        initEmpty();
        showError(new Throwable("未获取到内容，点击页面重新加载"));
    }


    private void hideEmpty(){
        if(emptyView==null)
            return;
        if(emptyView==errorView) {
            hideError();
        }else {
            emptyView.setVisibility(GONE);
        }
    }

    @Override
    public void showLoading(String loadtext) {
        initLoading();
        initEditor();
        editor.changeLoadingShow(loadingView, loadtext);
        showView(loadingView);
    }

    private void hideLoading(){
        if(loadingAnimator!=null&&loadingAnimator.isStarted()) {
            loadingAnimator.cancel();
            loadingAnimator=null;
        }
        if(loadingView==null)
            return;
        loadingView.setVisibility(GONE);
    }

    @Override
    public void showError(Throwable error) {
        Log.d("showError error="+error);
        initError();
        initEditor();
        editor.changeErrorShow(errorView, error);
        ImageView imageView=getView(errorView,R.id.iv_img);
        imageView.setImageResource(R.mipmap.fail);
        showView(errorView);
    }

    private void hideError(){
        if(errorView==null)
            return;
        errorView.setVisibility(GONE);
        ImageView imageView=getView(errorView,R.id.iv_img);
        imageView.setImageResource(0);
    }

    @Override
    public void showContent() {
        hideLoading();
        hideError();
        hideEmpty();
    }

    private void removeParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(view);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(loadingAnimator!=null&&loadingAnimator.isStarted()){
            loadingAnimator.cancel();
        }
    }

    public View getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }

    public View getErrorView() {
        return errorView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public interface Editor {
        void changeLoadingShow(View loadingView, String text);

        void changeErrorShow(View errorView, Throwable error);
    }


    public interface OnBackListener {
        public void onBackPressed();
    }

}
