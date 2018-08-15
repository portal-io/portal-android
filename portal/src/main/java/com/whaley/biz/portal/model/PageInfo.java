package com.whaley.biz.portal.model;

/**
 * Created by dell on 2018/8/10.
 */

public class PageInfo {
    /**
     * number : 0
     * size : 100
     * totalPages : 1
     */

    private int number;
    private int size;
    private int totalPages;

    public void setNumber(int number) {
        this.number = number;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
