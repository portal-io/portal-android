package com.whaley.biz.program.ui.live.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.program.model.CouponPackModel;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.ui.uimodel.ReserveDetailViewModel;
import com.whaley.core.share.model.ShareParam;

import java.util.List;

/**
 * Created by dell on 2017/8/17.
 */

public class LiveReserveDetailRepository extends MemoryRepository {

    private String code;
    private boolean hasOrder;
    private int liveStatus;
    private boolean isBuy;
    private CouponPackModel couponPackModel;
    private String price;
    private int tvAmount;
    private ReserveDetailViewModel reserveDetailViewModel;
    private boolean isCharge;
    private ShareParam.Builder builder;

    LiveDetailsModel serverModel;

    public ShareParam.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(ShareParam.Builder builder) {
        this.builder = builder;
    }

    public void convert(LiveDetailsModel severModel) {
        this.serverModel = severModel;
    }

    public LiveDetailsModel getSeverModel() {
        return serverModel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isHasOrder() {
        return hasOrder;
    }

    public void setHasOrder(boolean hasOrder) {
        this.hasOrder = hasOrder;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public CouponPackModel getCouponPackModel() {
        return couponPackModel;
    }

    public void setCouponPackModel(CouponPackModel couponPackModel) {
        this.couponPackModel = couponPackModel;
    }

    public int getTvAmount() {
        return tvAmount;
    }

    public void setTvAmount(int tvAmount) {
        this.tvAmount = tvAmount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ReserveDetailViewModel getReserveDetailViewModel() {
        return reserveDetailViewModel;
    }

    public void setReserveDetailViewModel(ReserveDetailViewModel reserveDetailViewModel) {
        this.reserveDetailViewModel = reserveDetailViewModel;
    }

    public boolean isCharge() {
        return isCharge;
    }

    public void setCharge(boolean charge) {
        isCharge = charge;
    }

}
