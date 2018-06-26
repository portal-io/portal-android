package com.whaley.biz.common.interactor;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.ui.viewmodel.LoadListData;
import com.whaley.core.repository.IRepositoryManager;

import java.util.List;

import io.reactivex.Scheduler;

/**
 * Created by YangZhi on 2017/7/25 19:16.
 */

public class Refresh<RESPONSE extends BaseListResponse> extends LoaderUseCase<RESPONSE> {

    public Refresh(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    protected void handleListData(LoadService service, List dataList) throws Exception {
        LoadListData loadListData = service.getLoadListData();
        if(loadListData == null){
            loadListData = new LoadListData();
            service.saveLoadListData(loadListData);
        }
        if (dataList == null || dataList.size() <= 0){
            List listData = loadListData.getListData();
            if(listData!=null) {
                listData.clear();
            }
            List viewModelList = loadListData.getViewDatas();
            if(viewModelList!=null) {
                viewModelList.clear();
            }
            return;
        }
        loadListData.setListData(dataList);
        loadListData.setViewDatas(dataList);
        loadListData.setPage(0);
    }
}
