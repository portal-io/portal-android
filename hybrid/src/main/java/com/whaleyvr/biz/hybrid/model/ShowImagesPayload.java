package com.whaleyvr.biz.hybrid.model;

import java.util.List;

/**
 * Created by YangZhi on 2016/10/12 23:17.
 */
public class ShowImagesPayload {

    private int index;

    private List<String> imgs;


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
}
