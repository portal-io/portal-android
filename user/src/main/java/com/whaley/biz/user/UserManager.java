package com.whaley.biz.user;

import android.content.SharedPreferences;

import com.whaley.biz.common.event.EventController;
import com.whaley.biz.user.event.SignOutEvent;
import com.whaley.biz.user.model.AccessTokenModel;
import com.whaley.biz.user.model.UserModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;


/**
 * Created by YangZhi on 2016/9/3 15:01.
 */
public class UserManager {
    static final String STR_SP_USER_BEAN = "sp_user_bean";

    private static class SingleTon {
        public static final UserManager instance = new UserManager();
    }

    public static UserManager getInstance() {
        return SingleTon.instance;
    }

    private SharedPreferences sp;

    private UserModel userModel;


    public static void signOut() {
        UserManager.getInstance().removeUser();
        EventController.postEvent(new SignOutEvent(UserConstants.EVENT_SIGN_OUT));
    }

    private UserManager() {
        sp = AppContextProvider.getInstance().getContext().getSharedPreferences("sp_user", AppContextProvider.getInstance().getContext().MODE_PRIVATE);
    }


//    private void initApi() {
//        if (userVrApi == null) {
//            userVrApi = HttpManager.getInstance().getRetrofit(UserVrApi.class).create(UserVrApi.class);
//        }
//    }
//
//
//    public void saveToken(String token) {
////        this.token=token;
////        saveTokenToSp();
//    }

//    private boolean saveTokenToSp() {
//        return sp.edit().putString(STR_SP_TOKEN, token).commit();
//    }


    public UserModel getUser() {
        UserModel userModel = this.userModel;
        if (userModel == null) {
            try {
                userModel = getUserBeanFromSp();
                if(userModel!=null) {
                    userModel.setAvatarTime(System.currentTimeMillis());
                }
            } catch (Throwable e) {
                Log.e(e, "userModel is null");
            }
        }
        if (userModel == null) {
            userModel = new UserModel();
            userModel.setAvatarTime(System.currentTimeMillis());
        }
        this.userModel = userModel;
        return userModel;
    }


    public void setAccessTokenBean(AccessTokenModel accessTokenModel) {
        getUser().setAccessTokenModel(accessTokenModel);
        saveUser(this.userModel);
    }

    public AccessTokenModel getAccessTokenBean() {
        return getUser().getAccessTokenModel();
    }

    public String getAccesstoken() {
        if (getAccessTokenBean() != null) {
            return getAccessTokenBean().getAccesstoken();
        }
        return "";
    }

    public String getDeviceId() {
        String deviceId = getUser().getDeviceId();
        if (StrUtil.isEmpty(deviceId)) {
            deviceId = AppUtil.getDeviceId();
            setDeviceId(deviceId);
        }
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        getUser().setDeviceId(deviceId);
        saveUser(this.userModel);
    }

    public String getRefreshtoken() {
        return getAccessTokenBean().getRefreshtoken();
    }

    private UserModel getUserBeanFromSp() throws Throwable {
        UserModel userModel = null;
        String userJson = sp.getString(STR_SP_USER_BEAN, null);
        if (!StrUtil.isEmpty(userJson)) {
            userModel = GsonUtil.getGson().fromJson(userJson, UserModel.class);
        }
        return userModel;
    }

    public void saveLoginUser(UserModel userModel) {
        if (userModel != null) {
            userModel.setDeviceId(this.userModel.getDeviceId());
            userModel.setAvatarTime(this.userModel.getAvatarTime());
            saveUser(userModel);
        }
    }

    public void saveUserInfo(UserModel userModel) {
        if (userModel != null) {
            userModel.setAccessTokenModel(this.userModel.getAccessTokenModel());
            userModel.setDeviceId(this.userModel.getDeviceId());
            userModel.setAvatarTime(this.userModel.getAvatarTime());
            saveUser(userModel);
        }
    }

    public boolean saveUser(UserModel userModel) {
        if (userModel != null) {
            this.userModel = userModel;
            try {
                return saveUserToSp(userModel);
            } catch (Throwable e) {
                Log.e(e, "");
            }
        }
        return false;
    }


    public void removeUser() {
        this.userModel = null;
//        CookieManager.getInstance().removeCookie();
        sp.edit().remove(STR_SP_USER_BEAN).apply();
    }


    private boolean saveUserToSp(UserModel userModel) throws Throwable {
        if (userModel == null) {
//            CookieManager.getInstance().removeCookie();
            return sp.edit().remove(STR_SP_USER_BEAN).commit();
        }
        String userJson = GsonUtil.getGson().toJson(userModel);
        if (!StrUtil.isEmpty(userJson))
            return sp.edit().putString(STR_SP_USER_BEAN, userJson).commit();
        return false;
    }


    public boolean isLogin() {
        UserModel userModel = getUser();
        if (userModel != null && userModel.getAccessTokenModel() != null) {
            return true;
        }
        return false;
    }

}
