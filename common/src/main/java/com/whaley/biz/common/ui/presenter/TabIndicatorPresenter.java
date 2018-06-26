package com.whaley.biz.common.ui.presenter;

import android.os.Build;
import android.os.Bundle;

import com.whaley.biz.common.repository.TabIndicatorRepository;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.ui.TabIndicatorFragment;
import com.whaley.biz.common.ui.view.TabIndicatorView;
import com.whaley.core.inject.annotation.Repository;

/**
 * Created by dell on 2017/8/10.
 */

public abstract class TabIndicatorPresenter<VIEW extends TabIndicatorView> extends BasePagePresenter<VIEW> implements TabIndicatorFragment.TabAdapter {

    static final String STR_SAVE_STATE="save_state";

    @Repository
    private TabIndicatorRepository tabIndicatorRepository;

    public TabIndicatorPresenter(VIEW view) {
        super(view);
    }

    public TabIndicatorRepository getTabIndicatorRepository() {
        return tabIndicatorRepository;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (saveInstanceState != null&& !isAnroidN()) {
            TabIndicatorRepository repository = (TabIndicatorRepository) saveInstanceState.getSerializable(STR_SAVE_STATE);
            if (repository != null) {
                getTabIndicatorRepository().setTitles(repository.getTitles());
                getTabIndicatorRepository().setCode(repository.getCode());
                getTabIndicatorRepository().setCount(repository.getCount());
                getTabIndicatorRepository().setTitleText(repository.getTitleText());
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getTabIndicatorRepository() != null&& !isAnroidN()) {
            outState.putSerializable(STR_SAVE_STATE, getTabIndicatorRepository());
        }
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        if(getTabIndicatorRepository().isNoData()){
            getTitles();
        }else if(getUIView()!=null){
            getUIView().updateTabs();
        }
    }

    protected abstract void getTitles();

    public void onRetry(){
        getTitles();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(getTabIndicatorRepository().getTitles()!=null&&getTabIndicatorRepository().getTitles().size()>position)
            return getTabIndicatorRepository().getTitles().get(position).getTitle();
        return null;
    }


    @Override
    public int getCount() {
        if(getTabIndicatorRepository().getTitles()!=null)
            return getTabIndicatorRepository().getCount();
        return 0;
    }

    protected boolean isAnroidN() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return true;
        }
        return false;
    }

}
