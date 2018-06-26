package com.whaley.biz.program.model.response;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.program.model.SearchModel;

import java.util.List;

/**
 * Created by dell on 2017/8/25.
 */

public class SearchResponse extends CMSResponse<SearchModel> implements BaseListResponse<SearchModel, SearchModel.ProgramBean>{


    @Override
    public List< SearchModel.ProgramBean> getList() {
        return getData().getProgram();
    }

    @Override
    public boolean isLast() {
        return true;
    }

}
