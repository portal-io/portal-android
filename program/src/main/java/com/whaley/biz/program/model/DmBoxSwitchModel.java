package com.whaley.biz.program.model;

/**
 * Created by YangZhi on 2016/12/16 15:10.
 */

public class DmBoxSwitchModel {


    /**
     * 说明：1 打开 0：关闭
     */
    private int lottery;

    /**
     * 说明：1 打开 0：关闭
     */
    private int danmu;

    public int getLottery() {
        return lottery;
    }

    public void setLottery(int lottery) {
        this.lottery = lottery;
    }

    public int getDanmu() {
        return danmu;
    }

    public void setDanmu(int danmu) {
        this.danmu = danmu;
    }
}
