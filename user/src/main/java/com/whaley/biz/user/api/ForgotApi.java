package com.whaley.biz.user.api;


import com.whaley.biz.user.BaseUrls;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.model.response.WhaleyStringResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by YangZhi on 2016/8/29 18:23.
 */
@BaseUrl(BaseUrls.WHALEY_ACCOUNT + "forgot/")
public interface ForgotApi {

    /**
     * 根据帐户（手机或邮箱）获取用户信息接口
     *
     * @param account 安全手机号或安全邮箱
     */
    @FormUrlEncoded
    @POST("user.do")
    Observable<WhaleyResponse<UserModel>> user(@Field("account") String account);


    /**
     * 忘记密码--验证验证码接口
     */
    @FormUrlEncoded
    @POST("validate-captcha.do")
    Observable<WhaleyStringResponse> validateCaptcha(@Field("code") String code);


    /**
     * 忘记密码--获取短信验证码接口
     */
    @POST("sms-captcha.do")
    Observable<WhaleyStringResponse> smsCaptcha();


    /**
     * 忘记密码--重置密码接口
     *
     * @param code
     * @param password
     */
    @FormUrlEncoded
    @POST("reset.do")
    Observable<WhaleyStringResponse> reset(@Field("code") String code, @Field("password") String password);
}
