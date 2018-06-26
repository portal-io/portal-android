package com.whaley.biz.user.api;

import com.whaley.biz.user.BaseUrls;
import com.whaley.biz.user.model.response.SendSMSResponse;
import com.whaley.biz.user.model.response.WhaleyStringResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by YangZhi on 2016/8/30 14:37.
 */
@BaseUrl(BaseUrls.WHALEY_ACCOUNT)
public interface UserInfoApi {

    /**
     * 修改个人信息接口
     *
     * @param device_id   设备id
     * @param accesstoken 登录token
     * @param nickname    用戶昵稱
     */
    @FormUrlEncoded
    @POST("unify/user/update-user-nickname.do")
    Observable<WhaleyStringResponse> updateNickname(@Field("device_id") String device_id, @Field("accesstoken") String accesstoken, @Field("nickname") String nickname);


    /**
     * 修改密码接口
     *
     * @param device_id   设备id
     * @param accesstoken 登录token
     * @param old_pwd     旧密码
     * @param password    新密码
     * @return
     */
    @FormUrlEncoded
    @POST("unify/user/update-pwd.do")
    Observable<WhaleyStringResponse> updatePWD(@Field("device_id") String device_id, @Field("accesstoken") String accesstoken, @Field("old_pwd") String old_pwd, @Field("password") String password);


    /**
     * 修改手机 发送验证码后下一步接口(验证验证码接口)
     *
     * @return
     */
    @FormUrlEncoded
    @POST("unify/user/update-phone.do")
    Observable<WhaleyStringResponse> updateMobileNext(@Field("device_id") String device_id, @Field("accesstoken") String accesstoken, @Field("phone") String phone, @Field("ncode") String ncode, @Field("code") String code);


    /**
     * 修改手机发送新手机验证码
     *
     * @param newMobile 新手机号
     * @param ncode     国际编码
     * @return
     */
    @FormUrlEncoded
    @POST("unify/sms/update-phone-code.do")
    Observable<SendSMSResponse> changeNewMobileSms(@Field("device_id") String device_id, @Field("sms_token") String Token, @Field("mobile") String newMobile, @Field("ncode") String ncode, @Field("captcha") String Captcha);


    @FormUrlEncoded
    @POST("unify/user/validate-phone.do")
    Observable<WhaleyStringResponse> validateNewMobile(@Field("device_id") String device_id, @Field("accesstoken") String accesstoken, @Field("phone") String phone, @Field("ncode") String ncode);


}
