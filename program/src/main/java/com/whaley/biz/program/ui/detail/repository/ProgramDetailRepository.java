package com.whaley.biz.program.ui.detail.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.program.model.ProgramDetailModel;

/**
 * Created by YangZhi on 2017/8/23 15:34.
 */

public class ProgramDetailRepository extends MemoryRepository{

    private String code;

    private int type;

    private String downloadPath;

    private String downloadRenderType;

    private String downloadResolvedPath;

    private ProgramDetailModel programDetailModel;

//    private int watchCount;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getDownloadRenderType() {
        return downloadRenderType;
    }

    public void setDownloadRenderType(String downloadRenderType) {
        this.downloadRenderType = downloadRenderType;
    }

    public ProgramDetailModel getProgramDetailModel() {
        return programDetailModel;
    }

    public void setProgramDetailModel(ProgramDetailModel programDetailModel) {
        this.programDetailModel = programDetailModel;
    }

    public String getDownloadResolvedPath() {
        return downloadResolvedPath;
    }

    public void setDownloadResolvedPath(String downloadResolvedPath) {
        this.downloadResolvedPath = downloadResolvedPath;
    }
}
