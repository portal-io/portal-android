package com.whaley.biz.user.api;

//import com.whaley.core.http.annotation.BaseUrl;
//import com.whaley.vr.common.http.BaseUrls;
//import com.whaley.vr.model.com.whaley.biz.common.response.CMSResponse;
//import com.whaley.vr.model.com.whaley.biz.common.response.UserHistoryResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Author: qxw
 * Date: 2017/3/14
 */
//@BaseUrl(BaseUrls.VR_API_AGINOMOTO + "userHistory/")
//public interface UserHistoryApi {
//
//    String DATA_SOURCE = "app";
//
//    @FormUrlEncoded
//    @POST("pageSearch")
//    Call<UserHistoryResponse> pageSearch(@Field("uid") String uid, @Field("devideId") String devideId, @Field("page") String page, @Field("size") String size, @Field("dataSource") String dataSource, @Field("sign") String sign);
//
//
//    @FormUrlEncoded
//    @POST("del")
//    Call<CMSResponse> del(@Field("uid") String uid, @Field("devideId") String devideId, @Field("dataSource") String dataSource, @Field("sign") String sign);
//
//    @FormUrlEncoded
//    @POST("delByIds")
//    Call<CMSResponse> delByIds(@Field("dataSource") String dataSource, @Field("uid") String uid, @Field("devideId") String devideId, @Field("delIds") String delIds, @Field("sign") String sign);
//
//}
