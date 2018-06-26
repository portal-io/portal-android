package com.whaley.biz.setting.ui.viewmodel;

/**
 * Created by dell on 2017/10/12.
 */

public class CurrencyViewModel {

    private boolean isPrefer;
    private String name;
    private String preferName;
    private String content;
    private String preferContent;
    private int type;

    Object serverModel;

    public void convert(Object severModel) {
        this.serverModel = severModel;
    }

    public <M> M getSeverModel() {
        return (M) serverModel;
    }

    public boolean isPrefer() {
        return isPrefer;
    }

    public void setPrefer(boolean prefer) {
        isPrefer = prefer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPreferContent() {
        return preferContent;
    }

    public void setPreferContent(String preferContent) {
        this.preferContent = preferContent;
    }

    public String getPreferName() {
        return preferName;
    }

    public void setPreferName(String preferName) {
        this.preferName = preferName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
