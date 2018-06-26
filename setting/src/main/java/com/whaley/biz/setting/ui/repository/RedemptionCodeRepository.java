package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.setting.model.RedemptionCodeModel;
import com.whaley.biz.setting.ui.viewmodel.RedemptionCodeViewModel;

import java.util.List;

/**
 * Created by dell on 2017/8/3.
 */

public class RedemptionCodeRepository extends MemoryRepository{

    private boolean isUnity;
    private RedemptionCodeViewModel redemptionCodeItemBox;
    private List<RedemptionCodeViewModel> redemptionList;
    private RedemptionCodeModel redemptionCodeModel;

    public RedemptionCodeViewModel getRedemptionCodeItemBox() {
        return redemptionCodeItemBox;
    }

    public void setRedemptionCodeItemBox(RedemptionCodeViewModel redemptionCodeItemBox) {
        this.redemptionCodeItemBox = redemptionCodeItemBox;
    }

    public List<RedemptionCodeViewModel> getRedemptionList() {
        return redemptionList;
    }

    public void setRedemptionList(List<RedemptionCodeViewModel> redemptionList) {
        this.redemptionList = redemptionList;
    }

    public boolean isUnity() {
        return isUnity;
    }

    public void setUnity(boolean unity) {
        isUnity = unity;
    }

    public RedemptionCodeModel getRedemptionCodeModel() {
        return redemptionCodeModel;
    }

    public void setRedemptionCodeModel(RedemptionCodeModel redemptionCodeModel) {
        this.redemptionCodeModel = redemptionCodeModel;
    }
}
