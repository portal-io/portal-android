package com.whaley.biz.setting.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.setting.LocalType;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.event.ChangeEditEvent;
import com.whaley.biz.setting.event.ExitEditEvent;
import com.whaley.biz.setting.event.MainBackEvent;
import com.whaley.biz.setting.ui.presenter.LocalTabPresenter;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.uiframe.ContainerActivity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by dell on 2017/8/4.
 */

@Route(path = SettingRouterPath.LOCAL)
public class LocalTabFragment  extends BaseMVPFragment<LocalTabPresenter> implements LocalTabView {

    @BindView(R2.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R2.id.viewpager)
    ViewPager viewpager;
    FragmentAdapter adapter;

    public static final String STR_POSITION = "str_position";
    private int currentPostion = -1;

    public static void goPage(Starter starter) {
        Intent intent = ContainerActivity.createIntent(starter, LocalTabFragment.class.getName(), null);
        starter.startActivityForResult(intent, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ("取消".equals(getTitleBar().getRightText())) {
                EventController.postEvent(new MainBackEvent());
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        adapter = new FragmentAdapter(getChildFragmentManager(), getPresenter());
        getTitleBar().setTitleText(getPresenter().getLocalTabRepository().getTitleText());
        getTitleBar().setRightText(" ");
        getTitleBar().setTitleBarListener(new SimpleTitleBarListener(){
            @Override
            public void onLeftClick(View view) {
                if ("取消".equals(getTitleBar().getRightText())) {
                    EventController.postEvent(new MainBackEvent());
                } else {
                    super.onLeftClick(view);
                }
            }
            @Override
            public void onRightClick(View view) {
                super.onRightClick(view);
                EventController.postEvent(new ChangeEditEvent(getType()));
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPostion = position;
                getTitleBar().setLeftViewVisibility(View.VISIBLE);
                EventController.postEvent(new ExitEditEvent(getType()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        if (currentPostion < 0) {
            currentPostion = 0;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment != null)
                    fragment.onHiddenChanged(hidden);
            }
        }
    }

    public int getType() {
        if (viewpager.getCurrentItem() == 0) {
            return LocalType.DOWNLOAD_TYPE_VIDEO;
        } else {
            return LocalType.DOWNLOAD_TYPE_LOCAL_IMPORT;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_tab;
    }

    @Override
    public void updateTabs() {
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(currentPostion);
    }

    public int getCurrentPostion() {
        return currentPostion;
    }

    static class FragmentAdapter extends FragmentStatePagerAdapter {


        private TabAdapter tabAdapter;


        public FragmentAdapter(FragmentManager fm, TabAdapter tabAdapter) {
            super(fm);
            this.tabAdapter = tabAdapter;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = tabAdapter.getItem(position);
            if (fragment != null) {
                Bundle arguments = fragment.getArguments();
                if (arguments == null) {
                    arguments = new Bundle();
                }
                arguments.putInt(STR_POSITION, position);
                fragment.setArguments(arguments);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return tabAdapter.getCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabAdapter.getPageTitle(position);
        }

    }

    public interface TabAdapter {

        Fragment getItem(int position);

        int getCount();

        CharSequence getPageTitle(int position);
    }

}
