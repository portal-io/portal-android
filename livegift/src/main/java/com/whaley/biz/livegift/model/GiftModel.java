package com.whaley.biz.livegift.model;

/**
 * Author: qxw
 * Date:2017/10/13
 * Introduction:
 */

public class GiftModel {

    // "giftCode": "cbe394f261ad034a6ff95811ecabb09a",
//         "type": 1,
//         "title": "银河系",
//         "intro": "银河系",
//         "price": 80,
//         "priceUnit": 1,
//         "pic": "http://store.whaley-vr.com/props/gift/2017-04-10/13022730411491804096098",
//         "icon": "http://store.whaley-vr.com/props/gift/2017-04-10/13022730411491804099437"
    private String giftCode;
    private int type;
    private String title;
    private String intro;
    private int price;
    private int priceUnit;
    private String pic;
    private String icon;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getGiftCode() {
        return giftCode;
    }

    public void setGiftCode(String giftCode) {
        this.giftCode = giftCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(int priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
