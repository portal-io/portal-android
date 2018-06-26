package com.whaley.biz.common.repository;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.ui.viewmodel.LoadListData;

/**
 * Created by YangZhi on 2017/7/31 17:43.
 */

public class LoaderRepository extends MemoryRepository implements LoaderUseCase.LoadService{
    LoadListData loadListData;

    @Override
    public void saveLoadListData(LoadListData data) {
        this.loadListData = data;
    }

    @Override
    public LoadListData getLoadListData() {
        return loadListData;
    }
}
