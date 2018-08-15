package com.whaley.biz.launcher.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.core.router.Router;

import java.util.List;


/**
 * Created by yangzhi on 16/8/15.
 */
public class MainFragmentAdapter extends FragmentSwitchTabAdapter {

    public final static String TAB_RECOMMEND = "recommend";
    public final static String TAB_LIVE = "live";
    public final static String TAB_DISCOVER = "discover";
    public final static String TAB_USER = "user";
    public final static String TAB_PORTAL = "portal";
    private final static String[] TAB_TAGS = new String[]{TAB_RECOMMEND, TAB_LIVE, TAB_DISCOVER, TAB_USER, TAB_PORTAL};

    private String secondaryTab;


    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public String getSecondaryTab() {
        return secondaryTab;
    }

    public void setSecondaryTab(String secondaryTab) {
        this.secondaryTab = secondaryTab;
    }

    //    @Override
    public String[] getTabTags() {
        return TAB_TAGS;
    }

    @Override
    protected Fragment createFragment(String tag) {
        Fragment f = null;
        switch (tag) {
            case TAB_RECOMMEND:
                f = Router.getInstance().buildObj("/program/ui/home").getObj();
                break;
            case TAB_LIVE:
                f = Router.getInstance().buildObj("/program/ui/liveTab").getObj();
                secondaryTab = null;
                break;
            case TAB_DISCOVER:
                f = Router.getInstance().buildObj("/program/ui/followmy").getObj();
                break;
            case TAB_USER:
                f = Router.getInstance().buildObj("/setting/ui/me").getObj();
                break;
            case TAB_PORTAL:
                f = Router.getInstance().buildObj("/portal/ui/portal").getObj();
                break;
        }
        return f;
    }

    @Override
    protected void setArguments(String tag, Fragment fragment) {
        switch (tag) {
            case TAB_LIVE:
                secondaryTab = null;
                break;
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level < android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            return;
        }
        try {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            boolean shouldCommit = false;
            for (String tab : getTabTags()) {
                if (!tab.equals(getmCurrentTag())) {
                    Fragment fragment = getFragmentManager().findFragmentByTag(tab);
                    if (fragment == null) {
                        continue;
                    }
                    ft.remove(fragment);
                    shouldCommit = true;
                }
            }
            if (!shouldCommit) {
                return;
            }
            ft.commitNowAllowingStateLoss();
        } catch (Exception e) {

        }
    }


    @Override
    protected void repeatTagClick(Fragment fragment) {
        if (fragment instanceof BaseMVPFragment) {
            BaseMVPFragment baseMVPFragment = (BaseMVPFragment) fragment;
            baseMVPFragment.doRefresh();
        }
    }
}
