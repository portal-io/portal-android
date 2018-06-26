package com.whaley.biz.program.model;


import java.util.List;

/**
 * Author: qxw
 * Date: 2017/3/22
 */

public class FollowMyModel {
    private List<CpInfoModel> cpList;
    private List<CpFollowInfoModel> latestUpdated;
    private int pageNumber;
    private int totalCount;
    private int totalPages;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<CpInfoModel> getCpList() {
        return cpList;
    }

    public void setCpList(List<CpInfoModel> cpList) {
        this.cpList = cpList;
    }

    public List<CpFollowInfoModel> getLatestUpdated() {
        return latestUpdated;
    }

    public void setLatestUpdated(List<CpFollowInfoModel> latestUpdated) {
        this.latestUpdated = latestUpdated;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}
