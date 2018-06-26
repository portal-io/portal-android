package com.whaley.biz.common.interactor;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.ui.viewmodel.LoadListData;
import com.whaley.core.repository.IRepositoryManager;

import java.util.List;

import io.reactivex.Scheduler;

/**
 * Created by YangZhi on 2017/7/31 11:44.
 */

public class LoadMore<RESPONSE extends BaseListResponse> extends LoaderUseCase<RESPONSE> {

    public LoadMore(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    protected void handleListData(LoadService service, List dataList) throws Exception {
        LoadListData loadListData = service.getLoadListData();
        if (dataList == null || dataList.size() <= 0){
            return;
        }
        if(loadListData == null || loadListData.getListData() == null){
            return;
        }
        loadListData.getListData().addAll(dataList);
        loadListData.setPage(loadListData.getPage()+1);
    }
}
