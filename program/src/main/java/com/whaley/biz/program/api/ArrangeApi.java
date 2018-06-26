package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.program.model.response.ArrangeResponse;
import com.whaley.biz.program.model.response.PackageResponse;
import com.whaley.biz.program.model.response.TopicListResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Author: qxw
 * Date:2017/8/17
 * Introduction:
 */
@BaseUrl(BaseUrls.VR_API_AGINOMOTO)
public interface ArrangeApi {
    String ARRANGE = "newVR-service/appservice/arrangeTree/";
    String PAGE_SIZE = "?size=30";

    @GET(ARRANGE + "elements" + PAGE_SIZE)
    Observable<ArrangeResponse> requestVideoList(@Query("code") String code, @Query("page") int page);

    @GET(ARRANGE + "findTreeNodeCode/{code}?v=1&containArrange=true")
    Observable<TopicListResponse> requestTopicList(@Path("code") String code);


}
