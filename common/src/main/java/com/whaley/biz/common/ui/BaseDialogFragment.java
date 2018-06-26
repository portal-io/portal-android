package com.whaley.biz.common.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.uiframe.utils.UIUtil;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.uiframe.view.LoadingView;
import com.whaley.core.uiframe.view.PageView;
import com.whaley.core.uiframe.view.ToastView;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dell on 2017/8/21.
 */

public abstract class BaseDialogFragment extends DialogFragment implements PageView, Starter {

    protected static String TAG;
    private View rootView;
    private Unbinder unbinder;

    public BaseDialogFragment() {
        TAG = this.getClass().getSimpleName();
        this.setRetainInstance();
    }

    protected void setRetainInstance() {
        if(this.getParentFragment() == null) {
            this.setRetainInstance(true);
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(this.getLayoutId(), container, false);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.bindViews(view);
        this.setViews(view, savedInstanceState);
    }

    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
    }

    protected abstract int getLayoutId();

    protected void bindViews(View view) {
        this.rootView = view;
        this.unBindViews();
        this.unbinder = ButterKnife.bind(this, view);
    }

    protected void unBindViews() {
        if(this.unbinder != null) {
            this.unbinder.unbind();
            this.rootView = null;
            this.unbinder = null;
        }
    }

    public View getRootView() {
        return this.rootView;
    }

    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, (Object)null);
        } catch (NoSuchFieldException var2) {
            //
        } catch (IllegalAccessException var3) {
            //
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.unBindViews();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void showLoading(String text) {
        LoadingView loadingView = (LoadingView) UIUtil.getView(this.getActivity());
        if(loadingView != null) {
            loadingView.showLoading(text);
        }
    }

    public void removeLoading() {
        LoadingView loadingView = (LoadingView)UIUtil.getView(this.getActivity());
        if(loadingView != null) {
            loadingView.removeLoading();
        }
    }

    public void showToast(String text) {
        ToastView toastView = (ToastView)UIUtil.getView(this.getActivity());
        if(toastView != null) {
            toastView.showToast(text);
        }
    }

    public boolean onBackPressed() {
        return false;
    }

    public boolean onNewIntent(Intent intent) {
        return false;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void finish() {
        this.getActivity().finish();
    }

    public Activity getAttachActivity() {
        return this.getActivity();
    }

    public void transitionAnim(int i, int i1) {
        this.getActivity().overridePendingTransition(i, i1);
    }

    public Context getAttatchContext() {
        return (Context)(this.getActivity() == null? AppContextProvider.getInstance().getContext():this.getActivity());
    }

    public EmptyDisplayView getEmptyDisplayView() {
        return null;
    }

}

