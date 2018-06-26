package com.whaley.biz.program.ui.user.repository;

import com.whaley.biz.common.repository.MemoryRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/1.
 */

public class CollectRepository extends MemoryRepository{

    private List<String> selectedList;

    private boolean isEdit = false;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public List<String> getSelectedList() {
        if(selectedList==null)
            selectedList = new ArrayList<>();
        return selectedList;
    }

    public void setSelectedList(List<String> selectedList) {
        this.selectedList = selectedList;
    }
}
