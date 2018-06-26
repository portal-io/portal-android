package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.program.model.response.CollectListResponse;
import com.whaley.biz.program.model.response.CollectionResponse;
import com.whaley.biz.program.model.response.StringResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Author: qxw
 * Date:2017/9/1
 * Introduction:
 */
@BaseUrl(BaseUrls.VR_API_AGINOMOTO)
public interface CollectionAPI {
    String COLLECTION_URL = "newVR-service/userFavorite/";


    @GET(COLLECTION_URL + "sec/findByCriteria")
    Observable<CollectListResponse> requestCollectionList(@Query("page") int page, @Query("size") int size, @Query("userLoginId") String userLoginId);

    @GET(COLLECTION_URL + "pri/save")
    Observable<StringResponse> saveCollection(@Query("userLoginId") String userLoginId,
                                              @Query("userName") String userName,
                                              @Query("programCode") String programCode,
                                              @Query("programName") String programName,
                                              @Query("videoType") String videoType,
                                              @Query("programType") String programType,
                                              @Query("status") int status,
                                              @Query("duration") int duration,
                                              @Query("picUrl") String picUrl);

    @GET(COLLECTION_URL + "pri/deletes")
    Observable<StringResponse> deleCollection(@Query("userLoginId") String userLoginId, @Query("programCodes") String programCodes);

    @GET(COLLECTION_URL + "sec/one/{userLoginId}/{programCodes}")
    Observable<CollectionResponse> getCollection(@Path("userLoginId") String userLoginId, @Path("programCodes") String programCodes);
}
