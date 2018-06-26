package com.whaley.biz.program.api;


import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.response.CpDetailResponse;
import com.whaley.biz.program.model.response.CpProgramsResponse;
import com.whaley.biz.program.model.response.FollowMyResponse;
import com.whaley.biz.program.model.response.StringResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dell on 2016/12/1.
 */

@BaseUrl(BaseUrls.VR_API_AGINOMOTO)
public interface FollowApi {
    String FOLLOW_MY_SIZE = "?size=40";

    /**
     * 发布者详情接口
     *
     * @param code   必填   发布者code
     * @param userId 选填 用户id
     */
    @GET(ProgramConstants.AGINOMOTO_APPSERVICE + "cp/findByCode")
    Observable<CpDetailResponse> requestCpDetail(@Query("code") String code, @Query("userId") String userId);

    /**
     * 发布者更新节目列表接口
     *
     * @param cpCode  必填     发布者code
     * @param sortCol 选填    排序字段，可选playCount(播放次数逆序）/ publishTime(发布时间逆序)，如果不填，则默认按照发布时间逆序
     * @param page    选填       当前页数，默认0（0为第一页）
     *                size   选填   页大小，默认50
     */
    @GET(ProgramConstants.AGINOMOTO_APPSERVICE + "program/findByCpCode?size=50")
    Observable<CpProgramsResponse> requestPrograms(
            @Query("cpCode") String cpCode,
            @Query("sortCol") String sortCol,
            @Query("page") String page);

    /**
     * 我的关注接口
     *
     * @param userId 必填 用户id
     * @param page   选填   当前页（从0开始，默认为0）
     */
    @GET(ProgramConstants.AGINOMOTO_APPSERVICE + "cprel/myfollow" + FOLLOW_MY_SIZE)
    Observable<FollowMyResponse> requestMyFollow(@Query("userId") String userId, @Query("page") int page);

    /**
     * 关注/取消关注接口
     *
     * @param userId 必填   用户id
     * @param cpCode 必填   发布者code
     * @param status 必填   1：关注  0：取消关注
     */
    @FormUrlEncoded
    @POST(ProgramConstants.AGINOMOTO_APPSERVICE + "cprel/follow")
    Observable<StringResponse> changeFollowStatus(
            @Field("userId") String userId,
            @Field("cpCode") String cpCode,
            @Field("status") String status);
}
