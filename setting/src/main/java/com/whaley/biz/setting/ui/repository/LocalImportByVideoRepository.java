package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.setting.ui.viewmodel.LocalVideoImportViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/7.
 */

public class LocalImportByVideoRepository extends MemoryRepository {

    private List<LocalVideoImportViewModel> localVideoBeanList = new ArrayList<>();

    public List<LocalVideoImportViewModel> getLocalVideoBeanList() {
        return localVideoBeanList;
    }

    public void setLocalVideoBeanList(List<LocalVideoImportViewModel> localVideoBeanList) {
        this.localVideoBeanList = localVideoBeanList;
    }

}
