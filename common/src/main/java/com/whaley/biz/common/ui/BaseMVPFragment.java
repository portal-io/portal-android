package com.whaley.biz.common.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.biz.common.ui.view.SystemBarView;
import com.whaley.biz.common.utils.LeakCanaryUtil;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.uiframe.MVPFragment;
import com.whaley.core.widget.titlebar.ITitleBar;

import java.util.Iterator;
import java.util.List;


/**
 * Created by YangZhi on 2017/7/25 17:06.
 */

public abstract class BaseMVPFragment<PRESENTER extends BasePagePresenter> extends MVPFragment<PRESENTER> implements BasePageView {

    private boolean mIsVisible;

    private boolean isPaused;

    private ITitleBar titlebar;

    private boolean isUserVisibleHint = true;

    public void setTitlebar(ITitleBar titlebar) {
        this.titlebar = titlebar;
    }

    @Override
    public ITitleBar getTitleBar() {
        return titlebar;
    }

    @Override
    public void finishView() {
        getActivity().finish();
    }


    public void doRefresh() {
    }

    @Override
    public SystemBarTintManager getSystemBarManager() {
        if (getActivity() != null && getActivity() instanceof SystemBarView) {
            return ((SystemBarView) getActivity()).getSystemBarManager();
        }
        return null;
    }

    public boolean isFragmentVisible() {
        Fragment parentFragment = getParentFragment();
        boolean parentVisible = true;
        if (parentFragment != null) {
            parentVisible = parentFragment.isVisible() && parentFragment.getUserVisibleHint();
        }
        return parentVisible && super.isVisible() && isUserVisibleHint() && !isPaused();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        checkVisible();
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            Iterator<Fragment> fragmentIterator = getChildFragmentManager().getFragments().iterator();
            while (fragmentIterator.hasNext()) {
                Fragment fragment = fragmentIterator.next();
                if (fragment != null && fragment.isAdded()) {
                    fragment.onHiddenChanged(hidden);
                }
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isUserVisibleHint = isVisibleToUser;
        Log.d("BannerPlayerContainer", "fragment isVisibleToUser=" + isVisibleToUser);
        checkVisible();

    }

    public boolean isUserVisibleHint() {
        return isUserVisibleHint;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        checkVisible();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        checkVisible();
    }

    @Override
    public void onResume() {
        super.onResume();
        isPaused = false;
        checkVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        isPaused = true;
        checkVisible();
    }

    public boolean isPaused() {
        return isPaused;
    }

    private void checkVisible() {
        boolean isVisible = isFragmentVisible();
        if (mIsVisible != isVisible) {
            mIsVisible = isVisible;
            Log.d("BannerPlayerContainer", "onFragmentVisibleChanged mIsVisible=" + mIsVisible);
            onFragmentVisibleChanged(mIsVisible);
        }
    }

    /**
     * 当 Fragment 的显示状态改变
     *
     * @param isVisible
     */
    protected void onFragmentVisibleChanged(boolean isVisible) {
//        if(isVisible){
//            ImageLoader.resumeRequests(this);
//        }else {
//            ImageLoader.pauseRequests(this);
//        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LeakCanaryUtil.getRefWatcher().watch(this);
    }
}
