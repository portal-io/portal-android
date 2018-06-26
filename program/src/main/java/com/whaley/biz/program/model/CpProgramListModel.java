package com.whaley.biz.program.model;


import java.util.List;

/**
 * Created by LiuZhixiang on 3/15/17.
 */

public class CpProgramListModel {
    private int pageNumber;
    private int totalCount;
    private int totalPages;
    private List<CpProgramModel> programs;


    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
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

    public List<CpProgramModel> getPrograms() {
        return programs;
    }

    public void setPrograms(List<CpProgramModel> programs) {
        this.programs = programs;
    }
}
