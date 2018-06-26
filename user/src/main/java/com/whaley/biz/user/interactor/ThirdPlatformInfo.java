package com.whaley.biz.user.interactor;

import android.app.Activity;

import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.R;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.utils.AuthManager;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.StrUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/7/18.
 */

public class ThirdPlatformInfo extends BaseUseCase<UserModel, ThirdPlatformInfo.ThirdPlatformInfoParam> {

    public ThirdPlatformInfo() {

    }

    public ThirdPlatformInfo(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }


    @Override
    public Observable<UserModel> buildUseCaseObservable(final UseCaseParam<ThirdPlatformInfoParam> param) {
        Observable observable = Observable.create(new ObservableOnSubscribe<UserModel>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<UserModel> e) throws Exception {
                if (param.getParam().type.equals(UserConstants.TYPE_AUTH_QQ)) {
                    if (!AuthManager.isInstallQQ(param.getParam().activity)) {
                        if (e.isDisposed())
                            return;
                        e.onError(new StatusErrorThrowable(UserConstants.THIRD_INSTALL, AppContextProvider.getInstance().getContext().getString(R.string.share_no_qq), null));
                        return;
                    }
                }
                if (param.getParam().type.equals(UserConstants.TYPE_AUTH_WX)) {
                    if (!AuthManager.isInstallWeixin(param.getParam().activity)) {
                        if (e.isDisposed())
                            return;
                        e.onError(new StatusErrorThrowable(UserConstants.THIRD_INSTALL, AppContextProvider.getInstance().getContext().getString(R.string.share_no_weixin), null));
                        return;
                    }
                }
                AuthManager.getPlatformInfo(param.getParam().type, param.getParam().activity, new AuthManager.OauthListener() {
                    @Override
                    public void onOauthComplete(String type, Map<String, String> map) {
                        final UserModel userBean = new UserModel();
                        if (UserConstants.TYPE_AUTH_WB.equals(type)) {
                            try {
                                String result = map.get("result");
                                if (StrUtil.isEmpty(result)) {
                                    userBean.setNickname(map.get("screen_name"));
                                    userBean.setAvatar(map.get("profile_image_url"));
                                    userBean.setOpenid(map.get("id"));
                                    userBean.setOrgin("wb");
                                } else {
                                    JSONObject resultJSON = new JSONObject(map.get("result"));
                                    userBean.setNickname(resultJSON.optString("screen_name"));
                                    userBean.setAvatar(resultJSON.optString("profile_image_url"));
                                    userBean.setOpenid(resultJSON.optString("id"));
                                    userBean.setOrgin("wb");
                                }
                            } catch (JSONException e) {
                                Log.e(e, "");
                            } catch (Exception e) {
                                Log.e(e, "");
                            }
                        } else if (UserConstants.TYPE_AUTH_WX.equals(type)) {
                            userBean.setNickname(map.get("screen_name"));
                            userBean.setAvatar(map.get("profile_image_url"));
                            userBean.setUnionid(map.get("unionid"));
                            userBean.setOpenid(map.get("openid"));
                            userBean.setGender(map.get("gender"));
                            userBean.setOrgin("wx");

                        } else {
//                            ThirdQQUnionid thirdQQUnionid=new ThirdQQUnionid();
//                            thirdQQUnionid.buildUseCaseObservable(map.get())
//                            userBean.setOpenid(map.get("unionid"));
                            userBean.setOpenid(map.get("openid"));
                            userBean.setOrgin("qq");
                            userBean.setNickname(map.get("screen_name"));
                            if (StrUtil.isEmpty(userBean.getNickname())) {
                                userBean.setNickname("qq");
                            }
                        }
                        if (e.isDisposed())
                            return;
                        e.onNext(userBean);
                        e.onComplete();

                    }


                    @Override
                    public void onOauthFaile(Throwable throwable) {
                        String mag;
                        if (throwable != null) {
                            mag = throwable.getMessage();
                        } else {
                            mag = "未知错误";
                        }
                        if (e.isDisposed())
                            return;
                        e.onError(new StatusErrorThrowable(UserConstants.THIRD_ERROR, mag, null));
                    }

                    @Override
                    public void onOauthCancel() {
                        if (e.isDisposed())
                            return;
                        e.onError(new StatusErrorThrowable(UserConstants.THIRD_CANCEL, "", null));
                    }

                });
//                UMShareAPI.get(AppContextProvider.getInstance().getContext()).getPlatformInfo(param.getParam().activity, getPlatform(param.getParam().type), new UMAuthListener() {
//                    @Override
//                    public void onStart(SHARE_MEDIA share_media) {
//
//                    }
//
//                    @Override
//                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                        final UserModel userBean = new UserModel();
//                        if (SHARE_MEDIA.SINA == share_media) {
//                            try {
//                                String result = map.get("result");
//                                if (StrUtil.isEmpty(result)) {
//                                    userBean.setNickname(map.get("screen_name"));
//                                    userBean.setAvatar(map.get("profile_image_url"));
//                                    userBean.setOpenid(map.get("id"));
//                                    userBean.setOrgin("wb");
//                                } else {
//                                    JSONObject resultJSON = new JSONObject(map.get("result"));
//                                    userBean.setNickname(resultJSON.optString("screen_name"));
//                                    userBean.setAvatar(resultJSON.optString("profile_image_url"));
//                                    userBean.setOpenid(resultJSON.optString("id"));
//                                    userBean.setOrgin("wb");
//                                }
//                            } catch (JSONException e) {
//                                Log.e(e, "");
//                            } catch (Exception e) {
//                                Log.e(e, "");
//                            }
//                        } else if (SHARE_MEDIA.WEIXIN == share_media) {
//                            userBean.setNickname(map.get("screen_name"));
//                            userBean.setAvatar(map.get("profile_image_url"));
//                            userBean.setUnionid(map.get("unionid"));
//                            userBean.setOpenid(map.get("openid"));
//                            userBean.setGender(map.get("gender"));
//                            userBean.setOrgin("wx");
//                        } else if (SHARE_MEDIA.QQ == share_media) {
//                            userBean.setOpenid(map.get("openid"));
//                            userBean.setOrgin("qq");
//                            if (StrUtil.isEmpty(userBean.getNickname())) {
//                                userBean.setNickname("qq");
//                            }
//                        }
//                        UMShareAPI.get(AppContextProvider.getInstance().getContext()).deleteOauth(param.getParam().activity, share_media, null);
//                        e.onNext(userBean);
//                        e.onComplete();
////                        if (oauthListener instanceof OauthBindListener) {
////                            thirdBind(userBean, (OauthBindListener) oauthListener, presenter);
////                        } else {
////                            thirdLogin(userBean, (OauthLoginListener) oauthListener, presenter);
////                        }
//
//                    }
//
//                    @Override
//                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//
//                    }
//
//                    @Override
//                    public void onCancel(SHARE_MEDIA share_media, int i) {
//
//                    }
//                });
            }
        }).observeOn(Schedulers.io());
        return observable;
    }

    public Observable<UserModel> buildUseCaseObservable(Activity activity, String type) {
        ThirdPlatformInfoParam param = new ThirdPlatformInfoParam(activity, type);
        return buildUseCaseObservable(new UseCaseParam(param));
    }


    static class ThirdPlatformInfoParam {
        private Activity activity;
        private String type;

        ThirdPlatformInfoParam(Activity activity, String type) {
            this.activity = activity;
            this.type = type;
        }

        public Activity getActivity() {
            return activity;
        }

        public void setActivity(Activity activity) {
            this.activity = activity;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
