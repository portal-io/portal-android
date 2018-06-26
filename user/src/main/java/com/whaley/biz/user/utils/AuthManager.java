package com.whaley.biz.user.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whaley.biz.user.R;
import com.whaley.biz.user.UserConstants;
import com.whaley.core.appcontext.AppContextProvider;


import java.util.Map;

/**
 * Author: qxw
 * Date:2017/8/25
 * Introduction:
 */

public class AuthManager {

    private static SHARE_MEDIA getPlatform(String type) {
        switch (type) {
            case UserConstants.TYPE_AUTH_QQ:
                return SHARE_MEDIA.QQ;
            case UserConstants.TYPE_AUTH_WX:
                return SHARE_MEDIA.WEIXIN;
            default:
                return SHARE_MEDIA.SINA;
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(AppContextProvider.getInstance().getContext()).onActivityResult(requestCode, resultCode, data);
    }


    /**
     * @author qxw
     * 判断微信是否安装
     * @time 2016/11/24 16:43
     */

    public static boolean isInstallWeixin(Activity activity) {
        if (!UMShareAPI.get(getContext()).isInstall(activity, SHARE_MEDIA.WEIXIN_CIRCLE)) {
            return false;
        }
        return true;
    }


    /**
     * @author qxw
     * 判断qq是否安装
     * @time 2016/11/24 16:45
     */
    public static boolean isInstallQQ(Activity activity) {
        if (!UMShareAPI.get(getContext()).isInstall(activity, SHARE_MEDIA.QQ)) {
            return false;
        }
        return true;
    }

    public static void getPlatformInfo(final String type, final Activity activity, final OauthListener oauthListener) {
        UMShareAPI.get(AppContextProvider.getInstance().getContext()).getPlatformInfo(activity, getPlatform(type), new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if (oauthListener != null) {
                    oauthListener.onOauthComplete(type, map);
                }
                UMShareAPI.get(AppContextProvider.getInstance().getContext()).deleteOauth(activity, share_media, null);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (oauthListener != null) {
                    oauthListener.onOauthFaile(throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if (oauthListener != null) {
                    oauthListener.onOauthCancel();
                }
            }
        });
    }

    public static Context getContext() {
        return AppContextProvider.getInstance().getContext();
    }


    public interface OauthListener {
        void onOauthCancel();

        void onOauthFaile(Throwable throwable);

        void onOauthComplete(String type, Map<String, String> map);
    }
}
