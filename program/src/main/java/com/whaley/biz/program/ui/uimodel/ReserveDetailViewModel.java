package com.whaley.biz.program.ui.uimodel;

import com.whaley.biz.program.model.LiveGuestModel;

import java.util.List;

/**
 * Created by dell on 2017/8/28.
 */

public class ReserveDetailViewModel {

    private String tvRemain, tvUnit, tvDate;
    private String pic;
    private String name;
    private String intro;
    private String address;
    private List<LiveGuestModel> guests;

    public List<LiveGuestModel> getGuests() {
        return guests;
    }

    public void setGuests(List<LiveGuestModel> guests) {
        this.guests = guests;
    }

    public String getTvRemain() {
        return tvRemain;
    }

    public void setTvRemain(String tvRemain) {
        this.tvRemain = tvRemain;
    }

    public String getTvUnit() {
        return tvUnit;
    }

    public void setTvUnit(String tvUnit) {
        this.tvUnit = tvUnit;
    }

    public String getTvDate() {
        return tvDate;
    }

    public void setTvDate(String tvDate) {
        this.tvDate = tvDate;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
