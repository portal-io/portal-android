package com.whaley.biz.program.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2017/11/14.
 */

public class NodeModel implements Serializable {

    private String childrenCode;
    private String code;
    private String cCode;
    private int defaultVisible;
    private int tipTime;
    private String tip;
    private int position;
    private String smallPic;
    private String defaultItem;
    private String title;
    private List<MediaModel> mediaDtos;
    private String referCode;
    private String referPosition;
    private XyzModel xyz;

    public NodeModel(){

    }

    public NodeModel(String code){
        this.code = code;
    }

    public String getChildrenCode() {
        return childrenCode;
    }

    public void setChildrenCode(String childrenCode) {
        this.childrenCode = childrenCode;
    }

    public int getDefaultVisible() {
        return defaultVisible;
    }

    public void setDefaultVisible(int defaultVisible) {
        this.defaultVisible = defaultVisible;
    }

    public int getTipTime() {
        return tipTime;
    }

    public void setTipTime(int tipTime) {
        this.tipTime = tipTime;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public String getDefaultItem() {
        return defaultItem;
    }

    public void setDefaultItem(String defaultItem) {
        this.defaultItem = defaultItem;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MediaModel> getMediaDtos() {
        return mediaDtos;
    }

    public void setMediaDtos(List<MediaModel> mediaDtos) {
        this.mediaDtos = mediaDtos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NodeModel) {
            NodeModel nodeModel= (NodeModel) obj;
            return this.code.equals(nodeModel.getCode());
        }else if(obj instanceof String){
            String code = (String)obj;
            return this.code.equals(code);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if(code!=null)
            return code.hashCode();
        else
            return super.hashCode();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getReferPosition() {
        return referPosition;
    }

    public void setReferPosition(String referPosition) {
        this.referPosition = referPosition;
    }

    public XyzModel getXyz() {
        return xyz;
    }

    public void setXyz(XyzModel xyz) {
        this.xyz = xyz;
    }
}
