package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.program.model.response.RecommendListResponse;
import com.whaley.biz.program.model.response.RecommendResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Author: qxw
 * Date: 2016/10/24
 */
@BaseUrl(BaseUrls.VR_API_AGINOMOTO + "newVR-service/appservice/recommendPage/")
public interface RecommendAPI {
    String HOME_WELCOME_AD = "WelcomePage";
    String HOME_RECOMMEND = "shouyetuijian";
    String HOME_MOVICE = "moviefaxianye";
    String HOME_VR = "vrfaxianye";
    String HOME_LOOK_LIVE = "livereview";
    String RECOMMEND_HOME_PAGE = "RecommendPage";
    String RECOMMEND_HOME_LIVE = "live_home";
    String TV_FLAG = "?v=1";
    int SIZE_RECOMMEND = 9;
    String TAB_LIVE = "live_tab";
    String HOME_PAGE = "Page_Recommend";

    @GET("findElementsByCode/{pageCode}/{bitCode}/android/" + "{businessVersion}" + "/{pagenum}/{size}" + TV_FLAG)
    Observable<RecommendListResponse> requestRecommend(@Path("pageCode") String pageCode, @Path("bitCode") String bitCode, @Path("pagenum") int pagenum, @Path("size") int size, @Path("businessVersion") String businessVersion);

    @GET("findPageByCode/{code}/android/" + "{businessVersion}" + TV_FLAG)
    Observable<RecommendResponse> requestAllRecommend(@Path("code") String code, @Path("businessVersion") String businessVersion);

    @GET("findPageByCode/{code}/android/" + "{businessVersion}" + TV_FLAG)
    Observable<RecommendResponse> requestAllRecommend(@Path("code") String code, @Path("businessVersion") String businessVersion, @Query("uid") String uid);

}
