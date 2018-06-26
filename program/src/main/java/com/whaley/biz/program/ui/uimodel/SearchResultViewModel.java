package com.whaley.biz.program.ui.uimodel;

/**
 * Created by dell on 2017/8/25.
 */

public class SearchResultViewModel {

    private String name;
    private String subTitle;
    private String pic;
    private boolean isDrama;

    Object serverModel;

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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public boolean isDrama() {
        return isDrama;
    }

    public void setDrama(boolean drama) {
        isDrama = drama;
    }

}
