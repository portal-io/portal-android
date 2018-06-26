package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.LoaderRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/24.
 */

public class HistoryRepository extends LoaderRepository {

    private boolean isCheck;
    private List<String> checkList = new ArrayList<>();

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public List<String> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<String> checkList) {
        this.checkList = checkList;
    }

}
