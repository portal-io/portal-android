package com.whaley.biz.setting.ui.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.setting.LocalType;
import com.whaley.biz.setting.ui.repository.LocalTabRepository;
import com.whaley.biz.setting.ui.view.DownloadFragment;
import com.whaley.biz.setting.ui.view.LocalTabFragment;
import com.whaley.biz.setting.ui.view.LocalTabView;
import com.whaley.biz.setting.ui.view.LocalVideoFragment;
import com.whaley.biz.setting.ui.viewmodel.LocalTabViewModel;
import com.whaley.core.inject.annotation.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/4.
 */

public class LocalTabPresenter extends BasePagePresenter<LocalTabView> implements LocalTabFragment.TabAdapter{

    public static final String CURRENT_POSITION = "currentPostion";
    public static final String STR_PARAM_TYPE = "str_type";

    @Repository
    LocalTabRepository repository;

    public LocalTabPresenter(LocalTabView view) {
        super(view);
    }

    public LocalTabRepository getLocalTabRepository(){
        return repository;
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        getTitles();
    }

    protected void getTitles() {
        List<LocalTabViewModel> itemViewDatas = new ArrayList<>();
        LocalTabViewModel itemData_0 = new LocalTabViewModel("离线缓存", "");
        LocalTabViewModel itemData_1 = new LocalTabViewModel("本地视频", "");
        itemViewDatas.add(itemData_0);
        itemViewDatas.add(itemData_1);
        getLocalTabRepository().setTitles(itemViewDatas);
        if (getUIView() != null) {
            getUIView().updateTabs();
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = DownloadFragment.newInstance(LocalType.DOWNLOAD_TYPE_VIDEO);
        } else if (position == 1) {
            fragment = LocalVideoFragment.newInstance(LocalType.DOWNLOAD_TYPE_LOCAL_IMPORT);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        if (getLocalTabRepository().getTitles() != null)
            return getLocalTabRepository().getTitles().size();
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (getLocalTabRepository().getTitles() != null && getLocalTabRepository().getTitles().size() > position)
            return getLocalTabRepository().getTitles().get(position).getTitle();
        return null;
    }

}
