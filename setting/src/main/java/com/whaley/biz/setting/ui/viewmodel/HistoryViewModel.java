package com.whaley.biz.setting.ui.viewmodel;

import com.whaley.biz.setting.model.UserHistoryModel;
import com.whaley.biz.setting.router.FormatPageModel;
import com.whaley.biz.setting.router.PageModel;

/**
 * Created by dell on 2017/8/24.
 */

public class HistoryViewModel {

    public int id;
    public String code;
    public String name;
    public String duration;
    public String headDate;
    public int headType;
    public boolean isSelect;
    public String pic;
    public boolean isDrama;


    UserHistoryModel userHistoryModel;


    public boolean isDrama() {
        return isDrama;
    }

    public void setDrama(boolean drama) {
        isDrama = drama;
    }

    public void convert(UserHistoryModel userHistoryModel) {
        this.userHistoryModel = userHistoryModel;
    }

    public UserHistoryModel getSeverModel() {
        return userHistoryModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getHeadDate() {
        return headDate;
    }

    public void setHeadDate(String headDate) {
        this.headDate = headDate;
    }

    public int getHeadType() {
        return headType;
    }

    public void setHeadType(int headType) {
        this.headType = headType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
