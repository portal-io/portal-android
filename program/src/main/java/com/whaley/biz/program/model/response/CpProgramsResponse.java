package com.whaley.biz.program.model.response;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.program.model.CpProgramListModel;
import com.whaley.biz.program.model.CpProgramModel;

import java.util.List;


/**
 * Created by LiuZhixiang on 3/27/17.
 */

public class CpProgramsResponse extends CMSResponse<CpProgramListModel> implements BaseListResponse<CpProgramListModel, CpProgramModel> {

    @Override
    public List<CpProgramModel> getList() {
        return getData().getPrograms();
    }

    @Override
    public boolean isLast() {
        return getData().getPageNumber() + 1 < getData().getTotalPages() ? false : true;
    }
}
