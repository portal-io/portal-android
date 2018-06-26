package com.whaley.biz.common.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.core.inject.InjectUtil;
import com.whaley.core.inject.annotation.Presenter;
import com.whaley.core.widget.titlebar.ITitleBar;

/**
 * Created by dell on 2017/8/21.
 */

public abstract class BaseMVPDialogFragment<PRESENTER extends BasePagePresenter> extends BaseDialogFragment implements BasePageView {

    @Presenter
    private PRESENTER presenter;
    private boolean isInjected;

    public BaseMVPDialogFragment() {
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.checkInject();
        this.getPresenter().onCreate(this.getArguments(), savedInstanceState);
    }

    public void onDestroy() {
        this.getPresenter().onDestroy();
        super.onDestroy();
    }

    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getPresenter().onViewCreated(this.getArguments(), savedInstanceState);
    }

    public void onDestroyView() {
        this.getPresenter().onViewDestroyed();
        super.onDestroyView();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.checkInject();
        this.getPresenter().onAttached();
    }

    public void onDetach() {
        super.onDetach();
        this.getPresenter().onDetached();
    }

    public void onResume() {
        super.onResume();
        this.getPresenter().onResume();
    }

    public void onPause() {
        super.onPause();
        this.getPresenter().onPause();
    }

    public void onStart() {
        super.onStart();
        this.getPresenter().onStart();
    }

    public void onStop() {
        super.onStop();
        this.getPresenter().onStop();
    }

    public boolean onNewIntent(Intent intent) {
        this.getPresenter().onNewIntent(intent);
        return true;
    }

    public boolean onBackPressed() {
        return this.getPresenter().onBackPressed();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.getPresenter().onActivityResult(requestCode, resultCode, data);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.getPresenter().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public final PRESENTER getPresenter() {
        if (this.presenter == null) {
            this.presenter = this.onCreatePresenter();
            InjectUtil.getInstance().inject(this.presenter);
        }

        return this.presenter;
    }

    public void setPresenter(PRESENTER presenter) {
        this.presenter = presenter;
        InjectUtil.getInstance().inject(presenter);
    }

    protected PRESENTER onCreatePresenter() {
        return null;
    }

    private void checkInject() {
        if (!this.isInjected) {
            InjectUtil.getInstance().inject(this);
            this.isInjected = true;
        }
    }

    @Override
    public ITitleBar getTitleBar() {
        return null;
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public SystemBarTintManager getSystemBarManager() {
        return null;
    }
}

