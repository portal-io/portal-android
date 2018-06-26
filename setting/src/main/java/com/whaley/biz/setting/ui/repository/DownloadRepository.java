package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.setting.model.download.DownloadBean;
import com.whaley.biz.setting.ui.viewmodel.DownloadViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dell on 2017/8/4.
 */

public class DownloadRepository extends MemoryRepository{

    private List<DownloadViewModel> itemDatas = new ArrayList<>();
    private Object mLockObject = new Object();
    private int type;
    private boolean onCheck = false;
    private boolean showEdit = false;
    private List<String> checkList = new ArrayList<>();

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

    public void check(DownloadViewModel downloadViewModel, boolean checkOrNot) {
        if (checkOrNot) {
            checkList.add(downloadViewModel.itemData.getItemid());
            downloadViewModel.isSelect = true;
        } else {
            checkList.remove(downloadViewModel.itemData.getItemid());
            downloadViewModel.isSelect = false;
        }
    }

    public List<String> getCheck() {
        return checkList;
    }

    public int getCheckNum() {
        return checkList.size();
    }

    public int getTotalNum() {
        if (itemDatas == null) {
            return 0;
        } else {
            return itemDatas.size();
        }
    }

    public boolean isAllCheck() {
        if (itemDatas == null) {
            return false;
        } else {
            return checkList.size() == itemDatas.size();
        }
    }

    public boolean isAllNotCheck() {
        return checkList.size() == 0;
    }

    public void checkAll() {
        if (itemDatas == null || itemDatas.size() <= 0) {
            return;
        } else {
            checkList.clear();
            for (DownloadViewModel downloadViewModel : itemDatas) {
                downloadViewModel.isSelect = true;
                checkList.add(downloadViewModel.itemData.getItemid());
            }
        }
    }

    public void unheckAll() {
        checkList.clear();
        for (DownloadViewModel downloadViewModel : itemDatas) {
            downloadViewModel.isSelect = false;
        }
    }

    public boolean isShowEdit() {
        return showEdit;
    }

    public void setShowEdit(boolean showEdit) {
        this.showEdit = showEdit;
    }

    public List<DownloadViewModel> getItemDatas() {
        return itemDatas;
    }

    public void setItemDatas(List<DownloadBean> itemList) {
        synchronized (mLockObject) {
            itemDatas.clear();
            Iterator<DownloadBean> iter = itemList.iterator();
            while (iter.hasNext()) {
                DownloadBean bean = iter.next();
                if (type == bean.type) {
                    itemDatas.add(new DownloadViewModel(bean));
                }
            }
        }
    }

    public void updateItemDownloadStatus(int position, DownloadViewModel downloadViewModel) {
        synchronized (mLockObject) {
            getItemDatas().set(position, downloadViewModel);
        }
    }

}
