package com.whaley.biz.common.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.orhanobut.logger.Logger;
import com.whaley.biz.common.R;
import com.whaley.biz.common.R2;
import com.whaley.biz.common.ui.presenter.TabIndicatorPresenter;
import com.whaley.biz.common.ui.view.TabIndicatorView;
import com.whaley.biz.common.widget.emptylayout.EmptyDisplayLayout;
import com.whaley.core.uiframe.BaseFragment;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.utils.DisplayUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.lang.reflect.Constructor;
import java.util.List;

import butterknife.BindView;

/**
 * Created by dell on 2017/8/10.
 */

public class TabIndicatorFragment extends BaseMVPFragment<TabIndicatorPresenter> implements TabIndicatorView {

    public static final String STR_TAB_PRESENTER = "str_tab_presenter";

    public static final String STR_TAB_VIEW = "str_tab_view";

    public static final String STR_POSITION = "str_position";

    public static TabIndicatorFragment newInstance(Class presenterClass, Class viewClass) {
        TabIndicatorFragment fragment = new TabIndicatorFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STR_TAB_PRESENTER, presenterClass.getName());
        bundle.putString(STR_TAB_VIEW, viewClass.getName());
        fragment.setArguments(bundle);
        return fragment;
    }

    protected MagicIndicator tabLayout;
    protected ViewPager viewpager;
    protected EmptyDisplayLayout emptyDisplayLayout;
    protected FragmentAdapter adapter;
    protected CommonNavigator commonNavigator;

    protected MagicIndicator onCreateMagicIndicator(View rootView){
        return (MagicIndicator) rootView.findViewById(R.id.tabLayout);
    }

    protected ViewPager onCreateViewPager(View rootView){
        return (ViewPager) rootView.findViewById(R.id.viewpager);
    }

    protected EmptyDisplayLayout onCreateEmptyDisplayLayout(View rootView){
        return (EmptyDisplayLayout) rootView.findViewById(R.id.emptyLayout);
    }

    @Override
    protected TabIndicatorPresenter onCreatePresenter() {
        if(getArguments()!=null) {
            String str_presenter = getArguments().getString(STR_TAB_PRESENTER);
            String str_view = getArguments().getString(STR_TAB_VIEW);
            return createPresenterInstance(str_presenter, str_view);
        }
        return null;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        this.tabLayout = onCreateMagicIndicator(view);
        this.viewpager = onCreateViewPager(view);
        this.emptyDisplayLayout = onCreateEmptyDisplayLayout(view);
        super.setViews(view, savedInstanceState);
        adapter = new FragmentAdapter(getChildFragmentManager(), getPresenter());
        viewpager.setAdapter(adapter);
        initIndicator();
        tabLayout.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabLayout, viewpager);
        emptyDisplayLayout.setStarter(getPresenter().getStater());
        emptyDisplayLayout.setOnRetryListener(new EmptyDisplayLayout.OnRetryListener() {
            @Override
            public void onRetry() {
                getPresenter().onRetry();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_pager;
    }

    @Override
    public void updateTabs() {
        adapter.notifyDataSetChanged();
        commonNavigator.getAdapter().notifyDataSetChanged();
    }

    protected void initIndicator(){
        commonNavigator = new CommonNavigator(tabLayout.getContext());
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                if(adapter!=null)
                    return adapter.getCount();
                return 0;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView simplePagerTitleView = new ClipPagerTitleView(context);
                simplePagerTitleView.setText((String) adapter.getPageTitle(index));
                simplePagerTitleView.setTextSize(DisplayUtil.convertDIP2PX(16));
                simplePagerTitleView.setPadding(DisplayUtil.convertDIP2PX(10),0,DisplayUtil.convertDIP2PX(10),0);
                simplePagerTitleView.setTextColor(getResources().getColor(R.color.color4));
                simplePagerTitleView.setClipColor(getResources().getColor(R.color.color1));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(viewpager!=null)
                            viewpager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 2.7));
                indicator.setLineWidth(UIUtil.dip2px(context, 14));
                indicator.setRoundRadius(UIUtil.dip2px(context, 1.3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(getResources().getColor(R.color.color1));
                indicator.setYOffset(DisplayUtil.convertDIP2PX(6));
                return indicator;
            }
        });

    }

    @Override
    public void updateTitleText() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        List<Fragment> fragments = getChildFragmentManager().getFragments();
//        if (fragments != null && fragments.size() > 0) {
//            for (Fragment fragment : fragments) {
//                if (fragment != null)
//                    fragment.onHiddenChanged(hidden);
//            }
//        }
    }

    @Override
    public EmptyDisplayView getEmptyDisplayView() {
        return emptyDisplayLayout;
    }

    protected TabIndicatorPresenter createPresenterInstance(String clazzName, String viewClassName) {
        try {
            Class clazz = Class.forName(clazzName);
            Constructor constructor = clazz.getConstructor(Class.forName(viewClassName));
            TabIndicatorPresenter presenter = (TabIndicatorPresenter)constructor.newInstance(this);
            return presenter;
        } catch (Exception e) {
            Logger.e(e, BaseFragment.TAG+"createPresenterInstance");
        }
        return null;
    }

    public static class FragmentAdapter extends FragmentStatePagerAdapter {


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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public Fragment getCurrentFragment(int position){
            return tabAdapter.getItem(position);
        }

    }

    public interface TabAdapter {

        Fragment getItem(int position);

        int getCount();

        CharSequence getPageTitle(int position);

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(viewpager==null||adapter==null)
//            return;
//        int position = viewpager.getCurrentItem();
//        if(position>=0){
//            Fragment fragment = adapter.getCurrentFragment(position);
//            if(fragment!=null){
//                fragment.onActivityResult(requestCode, resultCode, data);
//            }
//        }
//    }
}
