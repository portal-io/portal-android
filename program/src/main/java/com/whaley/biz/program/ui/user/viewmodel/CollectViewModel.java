package com.whaley.biz.program.ui.user.viewmodel;

/**
 * Created by dell on 2017/8/1.
 */

public class CollectViewModel {

    private String code;
    private String name;
    private boolean isTv;
    private String duration;
    private String pic;
    private String programType;
    private boolean isSelected;

    Object serverModel;


    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public void convert(Object severModel) {
        this.serverModel = severModel;
    }

    public <M> M getSeverModel() {
        return (M) serverModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTv() {
        return isTv;
    }

    public void setTv(boolean tv) {
        isTv = tv;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
