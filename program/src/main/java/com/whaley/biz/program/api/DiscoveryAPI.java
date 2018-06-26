package com.whaley.biz.program.api;


import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.response.PackageResponse;
import com.whaley.biz.program.model.response.TopicListResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by yangzhi on 16/8/7.
 */
@BaseUrl(BaseUrls.VR_API_AGINOMOTO)
public interface DiscoveryAPI {
    String VIDEO_SITE_TREES_VERSION = "1.0";
    String AGINOMOTO_APPSERVICE = "newVR-service/appservice/";
//    @GET(ProgramConstants.AGINOMOTO_APPSERVICE + "arrangeTree/findAll/{code}/android/" + VIDEO_SITE_TREES_VERSION)
//    Call<SiteTreeResponse> requestVideoSiteTrees(@Path("code") String code);
//
//    @GET(ProgramConstants.AGINOMOTO_APPSERVICE + "arrangeTree/elements")
//    Call<VideoListResponse> requestVideoList(@Query("code") String code, @Query("page") int page, @Query("size") int size);


    @GET(AGINOMOTO_APPSERVICE + "pack/findByCode?size=2000&page=0")
    Observable<PackageResponse> requestProgramList(@Query("code") String code, @Query("uid") String uid);
//    @GET(ProgramConstants.AGINOMOTO_APPSERVICE + "pack/findByCode?size=2000&page=0")
//    Call<PackageResponse> requestProgramList(@Query("code") String code, @Query("uid") String uid);
//
//    @GET(ProgramConstants.AGINOMOTO_APPSERVICE + "search/recommend")
//    Call<DetailRelatedResponse> requestDetailRelated(@Query("keyWord") String keyWord, @Query("type") int contentType, @Query("code") String code);
//
//    @GET(ProgramConstants.AGINOMOTO_APPSERVICE + "program/findByCode")
//    Call<ProgramDetailResponse> requestVideoDetail(@Query("code") String code, @Query("s") int s);
//
//    @GET(ProgramConstants.AGINOMOTO_APPSERVICE + "program/findByCode")
//    Call<ProgramDetailResponse> requestVideoDetail(@Query("code") String code, @Query("s") int s, @Query("uid") String uid);
}
