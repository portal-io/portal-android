package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.setting.ui.viewmodel.LocalVideoViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dell on 2017/8/4.
 */

public class LocalVideoRepository extends MemoryRepository{

    private int type;
    private boolean onCheck = false;
    private boolean showEdit = false;
    private List<LocalVideoViewModel> localVideoBeanList = new ArrayList<>();
    private List<String> checkedIDs = new ArrayList<>();

    public void check(String itemid, boolean checkOrNot) {
        for (LocalVideoViewModel bean : localVideoBeanList) {
            if (bean.getVideoBean().getId().equals(itemid)) {
                bean.setOnCheck(checkOrNot);
                break;
            }
        }
    }

    public void checkAll() {
        if (localVideoBeanList == null || localVideoBeanList.size() <= 0) {
            return;
        } else {
            Iterator<LocalVideoViewModel> iter = localVideoBeanList.iterator();
            while (iter.hasNext()) {
                iter.next().setOnCheck(true);
            }
        }
    }

    public void unCheckAll() {
        if (localVideoBeanList == null || localVideoBeanList.size() <= 0) {
            return;
        } else {
            Iterator<LocalVideoViewModel> iter = localVideoBeanList.iterator();
            while (iter.hasNext()) {
                iter.next().setOnCheck(false);
            }
        }
    }

    public List<LocalVideoViewModel> getLocalVideoBeanList() {
        return localVideoBeanList;
    }

    public void setLocalVideoBeanList(List<LocalVideoViewModel> localVideoBeanList) {
        this.localVideoBeanList = localVideoBeanList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isOnCheck() {
        return onCheck;
    }

    public void setOnCheck(boolean onCheck) {
        this.onCheck = onCheck;
    }

    public boolean isShowEdit() {
        return showEdit;
    }

    public void setShowEdit(boolean showEdit) {
        this.showEdit = showEdit;
    }

    public List<String> getCheckedIDs() {
        return checkedIDs;
    }

    public void setCheckedIDs(List<String> checkedIDs) {
        this.checkedIDs = checkedIDs;
    }
}
