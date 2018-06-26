package com.whaley.biz.program.model;

/**
 * Author: qxw
 * Date: 2016/12/16
 */

public class PrizeDataModel {

    //    actid	活动ID
//    action	活动代码
//    actiontxt	活动名称
//    uid	用户Uid
//    whaleyuid	微鲸通行证Uid
//    nickname	用户姓名
//    status	状态
//    goodsid	奖品ID
//    name	奖品名称
//    sortid	抽奖活动来源资源ID
//    picture	奖品图片
//    info	中奖奖品的相关信息
//    dateline	创建时间
//    goodstype	奖品类型 0:实物奖品 1:微鲸兑换码 2:虚拟兑换码

    private String actid;
    private String action;
    private String actiontxt;
    private String uid;
    private String whaleyuid;
    private String nickname;
    private String status;
    private String goodsid;
    private String name;
    private String picture;
    private String info;
    private String dateline;
    private String goodstype;

    public String getActid() {
        return actid;
    }

    public void setActid(String actid) {
        this.actid = actid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActiontxt() {
        return actiontxt;
    }

    public void setActiontxt(String actiontxt) {
        this.actiontxt = actiontxt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWhaleyuid() {
        return whaleyuid;
    }

    public void setWhaleyuid(String whaleyuid) {
        this.whaleyuid = whaleyuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getGoodstype() {
        return goodstype;
    }

    public void setGoodstype(String goodstype) {
        this.goodstype = goodstype;
    }
}
