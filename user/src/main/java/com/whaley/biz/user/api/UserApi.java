package com.whaley.biz.user.api;



import com.whaley.biz.user.BaseUrls;
import com.whaley.biz.user.WhaleyObserver;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import retrofit2.http.GET;

/**
 * Created by YangZhi on 2016/8/29 9:55.
 */
@BaseUrl(BaseUrls.WHALEY_ACCOUNT + "user/")
public interface UserApi {

    /**
     * 登出接口
     */
    @GET("login-out.do")
    WhaleyObserver<String> loginOut();




}
