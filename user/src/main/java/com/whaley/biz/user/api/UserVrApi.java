package com.whaley.biz.user.api;


import com.whaley.biz.user.BaseUrls;
import com.whaley.biz.user.model.AccessTokenModel;
import com.whaley.biz.user.model.LoginModel;
import com.whaley.biz.user.model.TokenModel;
import com.whaley.biz.user.model.response.SendSMSResponse;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.model.ImageModel;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.model.response.WhaleyStringResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by yangzhi on 16/9/1.
 */
@BaseUrl(BaseUrls.WHALEY_ACCOUNT + "unify/")
public interface UserVrApi {
    String USER = "user/";
    String LOGIN = "login/";

    /**
     * 获取发送短信发送 Token 接口
     *
     * @param device_id 设备id
     * @return
     */
    @FormUrlEncoded
    @POST("sms/gettoken.do")
    Observable<WhaleyResponse<TokenModel>> getToken(@Field("device_id") String device_id);


    /**
     * 发送短信验证码
     * device_id 必须 设备 id
     * ￼Token 必须 Sms token
     * mobile 必须 手机号码
     * nocde 必须 国际编号
     * Captcha 非必须 五次操作后需要提供
     *
     * @param device_id
     * @param Token
     * @param mobile
     * @param ncode
     * @param Captcha
     * @return
     */
    @FormUrlEncoded
    @POST("sms/login-code.do")
    Observable<SendSMSResponse> send(@Field("device_id") String device_id, @Field("sms_token") String Token, @Field("mobile") String mobile, @Field("ncode") String ncode, @Field("captcha") String Captcha);

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param from
     * @param device_id
     * @return
     */
    @FormUrlEncoded
    @POST(LOGIN + "direct.do")
    Observable<WhaleyResponse<LoginModel>> login(@Field("username") String username, @Field("password") String password, @Field("from") String from, @Field("device_id") String device_id);

    /**
     * 刷新token
     *
     * @param device_id
     * @param accesstoken
     * @param refreshtoken
     * @return
     */
    @FormUrlEncoded
    @POST(LOGIN + "refresh-token.do")
    Observable<WhaleyResponse<AccessTokenModel>> refreshToken(@Field("device_id") String device_id, @Field("accesstoken") String accesstoken, @Field("refreshtoken") String refreshtoken);


    /**
     * 短信登录注册一体化接口
     *
     * @param device_id
     * @param mobile
     * @param ncode
     * @param code
     * @param from
     * @return
     */
    @FormUrlEncoded
    @POST(LOGIN + "shortcut.do")
    Observable<WhaleyResponse<LoginModel>> login(@Field("mobile") String mobile, @Field("code") String code, @Field("ncode") String ncode, @Field("from") String from, @Field("device_id") String device_id, @Field("third_id") String third_id, @Field("unionid") String unionid);

    /**
     * 三方账号登录
     *
     * @param orgin    三方标示 wb qq wx
     * @param deviceId 设备 imei
     * @param openid   三方 openid
     * @param unionid  微信 unionid 非必须
     * @return
     */
    @FormUrlEncoded
    @POST(LOGIN + "third.do")
    Observable<WhaleyResponse<LoginModel>> thirdLogin(@Field("open_id") String openid, @Field("origin") String orgin, @Field("from") String from, @Field("device_id") String deviceId, @Field("nickname") String nickname, @Field("unionid") String unionid, @Field("avatar") String avatar, @Field("location") String location, @Field("gender") String gender);


    /**
     * 获取用户信息
     *
     * @param device_id
     * @param accesstoken
     * @return
     */
    @FormUrlEncoded
    @POST(USER + "getinfo.do")
    Observable<WhaleyResponse<UserModel>> info(@Field("device_id") String device_id, @Field("accesstoken") String accesstoken);


    /**
     * 完善用户信息接口
     *
     * @param device_id
     * @param nickname
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("user/nickname-and-pwd.do")
    Observable<WhaleyStringResponse> updateUserinfo(@Field("device_id") String device_id, @Field("accesstoken") String accesstoken, @Field("nickname") String nickname, @Field("password") String password);


    /**
     * 三方账号绑定
     *
     * @param origin  三方标示 wb qq wx
     * @param open_id 三方 open_id
     * @param unionid 微信 unionid 非必须
     * @return
     */
    @FormUrlEncoded
    @POST("user/bind.do")
    Observable<WhaleyStringResponse> bind(@Field("device_id") String device_id, @Field("accesstoken") String accesstoken, @Field("open_id") String open_id, @Field("origin") String origin, @Field("unionid") String unionid, @Field("nickname") String nickname, @Field("avatar") String avatar, @Field("location") String location, @Field("gender") String gender);

    /**
     * 三方账户解绑接口
     *
     * @param origin 三方标示 wb qq wx
     * @return
     */
    @FormUrlEncoded
    @POST("user/unbind.do")
    Observable<WhaleyStringResponse> unbind(@Field("device_id") String device_id, @Field("accesstoken") String accesstoken, @Field("origin") String origin);


    /**
     * 修改预览头像上传接口
     * RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
     * MultipartBody.Part imagePart = MultipartBody.Part.createFormData("Filedata", file.getName(), photoRequestBody);
     *
     * @param imagePart
     * @return
     */
    @Multipart
    @POST("user/change-new-avatar.do")
    Observable<WhaleyResponse<ImageModel>> changeNewAvatar(@Part MultipartBody.Part accesstokenPart, @Part MultipartBody.Part imagePart);


}
