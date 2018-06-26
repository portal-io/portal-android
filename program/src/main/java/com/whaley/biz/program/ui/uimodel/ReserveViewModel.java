package com.whaley.biz.program.ui.uimodel;

/**
 * Created by dell on 2017/8/21.
 */

public class ReserveViewModel {

    private String code;
    private String pic;
    private String name;
    private String intro;
    private int liveStatus;
    private boolean isChargeable;
    private boolean isBuy;

    Object serverModel;

    public void convert(Object severModel) {
        this.serverModel = severModel;
    }

    public <M> M getSeverModel() {
        return (M) serverModel;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public boolean isChargeable() {
        return isChargeable;
    }

    public void setChargeable(boolean chargeable) {
        isChargeable = chargeable;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
