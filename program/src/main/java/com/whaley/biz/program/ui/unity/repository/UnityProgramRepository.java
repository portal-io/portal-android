package com.whaley.biz.program.ui.unity.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.program.model.CouponPackModel;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.utils.CouponPackUtil;
import com.whaley.biz.program.utils.StringUtil;

/**
 * Created by dell on 2017/8/30.
 */

public class UnityProgramRepository extends MemoryRepository {

    private ProgramDetailModel programDetailModel;
    private String code;
    private String amountStr;
    private boolean isPayed;
    private boolean isHasBeenPaid;
    private CouponPackModel couponPackModel;
    private String programType;

    public ProgramDetailModel getProgramDetailModel() {
        return programDetailModel;
    }

    public void setProgramDetailModel(ProgramDetailModel programDetailModel) {
        this.programDetailModel = programDetailModel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAmountStr() {
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public boolean isPayed() {
        return isPayed;
    }

    public void setPayed(boolean payed) {
        isPayed = payed;
    }

    public boolean isHasBeenPaid() {
        return isHasBeenPaid;
    }

    public void setHasBeenPaid(boolean hasBeenPaid) {
        isHasBeenPaid = hasBeenPaid;
    }

    public CouponPackModel getCouponPackModel() {
        return couponPackModel;
    }

    public void setCouponPackModel(CouponPackModel couponPackModel) {
        this.couponPackModel = couponPackModel;
    }

    /**
     * 设置播放次数
     */
    public void setPlayNum() {
        int count = 1000;
        if (programDetailModel.getStat() != null) {
            count = programDetailModel.getStat().getPlayCount();
        }
        setAmountStr(StringUtil.getCuttingCount(count) + "次播放");
    }

    public void setCouponModel() {
        setCouponPackModel(CouponPackUtil.getCouponPackModel(programDetailModel.getCouponDto(),
                programDetailModel.getContentPackageQueryDtos(), false));
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }
}
