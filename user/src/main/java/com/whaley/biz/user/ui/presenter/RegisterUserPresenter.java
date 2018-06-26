package com.whaley.biz.user.ui.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.utils.TimerUtil;
import com.whaley.biz.user.R;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.event.LoginSuccessEvent;
import com.whaley.biz.user.interactor.MergeThirdLogin;
import com.whaley.biz.user.interactor.MobilePhoneLogin;
import com.whaley.biz.user.model.CaptchaModel;
import com.whaley.biz.user.interactor.MergeSendSMS;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.ui.repository.RegisterRepository;
import com.whaley.biz.user.ui.view.CompleteUserInfoFragment;
import com.whaley.biz.user.ui.view.LoginFragment;
import com.whaley.biz.user.ui.view.RegisterView;
import com.whaley.biz.user.ui.view.UserBindFragment;
import com.whaley.biz.user.utils.AuthManager;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/2
 * Introduction:
 */

public class RegisterUserPresenter extends BaseUserPresenter<RegisterView> {

    public final static String KEY_LOGIN_USERBEAN = "login_userbean";
    public final static String KEY_LOGIN_USERBEAN_JSON = "login_userbean_json";
    public final static String KEY_THIRD_BIND = "third_bind";

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        if (arguments != null) {
            init(arguments);
        }
        regist();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginSuccessEvent event) {
        finish();
    }

    private void init(Bundle arguments) {
        UserModel userModel = arguments.getParcelable(KEY_LOGIN_USERBEAN);
        if (userModel == null) {
            userModel = GsonUtil.getGson().fromJson(arguments.getString(KEY_LOGIN_USERBEAN_JSON), UserModel.class);
        }
        registerRepository.setUserModel(userModel);
        registerRepository.setBind(arguments.getBoolean(KEY_THIRD_BIND, false));
    }

    @Repository
    RegisterRepository registerRepository;

    @UseCase
    MergeSendSMS mergeSendSMS;


    @UseCase
    MobilePhoneLogin mobilePhoneLogin;


    @UseCase
    MergeThirdLogin mergeThirdLogin;

    Disposable disposable;

    public RegisterUserPresenter(RegisterView view) {
        super(view);
    }


    public RegisterRepository getRegisterRepository() {
        return registerRepository;
    }


    public void onPhoneChanged(String phone) {
        registerRepository.setPhone(phone);
        checkSmsButtonState();
        checkNextButtonState();
    }

    public void onSMSCodeChanged(String smsCode) {
        registerRepository.setSmsCode(smsCode);
        checkNextButtonState();
    }

    public void onImageCodeChanged(String imgCode) {
        registerRepository.setImageCode(imgCode);
        checkSmsButtonState();
        checkNextButtonState();
    }

    public void onNextClick() {
        if (checkPhone(registerRepository.getPhone()) || checkSMS(registerRepository.getSmsCode())) {
            return;
        }

        if (registerRepository.isNeedValidateImage() && checkValidateCode(registerRepository.getImageCode())) {
            return;
        }
        toRegist();
    }

    public void onClickLoginButton() {
        LoginFragment.goPage(getStater());
    }

    public void onFetchCodeClick() {

        mergeSendSMS.executeSendSMS(new UpdateUIObserver<CaptchaModel>(getUIView(), true) {

            @Override
            public void onNext(@NonNull CaptchaModel captchaModel) {
                getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_sms_code_send));
                registerRepository.setSmsButtonLocked(true);
                TimerUtil.timerSeconds(60, new TimerUtil.TimerNext() {
                    @Override
                    public void doNext(long number) {
                        if (number > 0) {
                            getUIView().updateValidateCodeButton(number);
                        } else {
                            getUIView().resetSmsButton();
                            registerRepository.setSmsButtonLocked(false);
                        }
                    }
                });
            }

            @Override
            public void onStatusError(int status, String message, CaptchaModel data) {
                if (status == 101) {
                    getUIView().showToast("验证码错误, 请重新输入,或点击刷新验证码");
                } else if (status == 141) {
                    getUIView().showToast("请输入验证码");
                } else {
                    getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_sms_code_err));
                }
                if (!StrUtil.isEmpty(data.getUrl())) {
                    registerRepository.setImageCodeUrl(data.getUrl());
                    registerRepository.setNeedValidateImage(true);
                    getUIView().showValidateInput();
                }
                removeLoading();
            }
        });
    }

    private void toRegist() {
//        if (PermissionUtil.requestPermission(getUIView().getAttachActivity(), Manifest.permission.READ_PHONE_STATE, 1)) {
//            return;
//        }
        mobilePhoneLogin.executePhoneLogin(new UpdateUIObserver<UserModel>(getUIView(), true) {

            @Override
            public void onNext(@NonNull UserModel userModel) {
                super.onNext(userModel);
                if (!userModel.isAddInformation()) {
                    getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_login_success));
                    EventController.postEvent(new LoginSuccessEvent(UserConstants.EVENT_LOGIN_SUCCESS));
                } else {
                    CompleteUserInfoFragment.goPage(getStater(), userModel);
                }
                finish();
            }

            @Override
            public void onStatusError(int status, String message) {
                if (status == 104) {
                    getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_empty_sms_code_err));
                    removeLoading();
                } else {
                    super.onStatusError(status, message);
                }
            }
        });
    }


    public void checkSmsButtonState() {
        if (TextUtils.isEmpty(registerRepository.getPhone())
                || !StrUtil.isMobileNo(registerRepository.getPhone())
                || (registerRepository.isNeedValidateImage() && TextUtils.isEmpty(registerRepository.getImageCode()))
                || registerRepository.isSmsButtonLocked()) {
            getUIView().disableSmsButton();
        } else {
            getUIView().enableSmsButton();
        }
    }

    public void checkNextButtonState() {
        if (!TextUtils.isEmpty(registerRepository.getPhone()) && !TextUtils.isEmpty(registerRepository.getSmsCode())) {
            if (!registerRepository.isNeedValidateImage() || (registerRepository.isNeedValidateImage() && !TextUtils.isEmpty(registerRepository.getImageCode()))) {
                getUIView().enableNextButton();
            } else {
                getUIView().disableNextButton();
            }
        } else {
            getUIView().disableNextButton();
        }
    }


    public void onThirdQQClick() {
        thirdLogin(UserConstants.TYPE_AUTH_QQ);
    }

    public void onThirdWXClick() {
        thirdLogin(UserConstants.TYPE_AUTH_WX);
    }

    public void onThirdWBClick() {
        thirdLogin(UserConstants.TYPE_AUTH_WB);
    }


    private void thirdLogin(String type) {
        MergeThirdLogin.MergeThirdLoginParam param = new MergeThirdLogin.MergeThirdLoginParam(getUIView().getAttachActivity(), type);
        disposable = mergeThirdLogin.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<UserModel>(getUIView(), true) {
                    @Override
                    public void onNext(@NonNull UserModel userModel) {
                        super.onNext(userModel);
                        if (!userModel.isAddInformation()) {
                            getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_login_success));
                            EventController.postEvent(new LoginSuccessEvent(UserConstants.EVENT_LOGIN_SUCCESS));
                            getUIView().getAttachActivity().setResult(Activity.RESULT_OK, new Intent());
                        } else {
                            CompleteUserInfoFragment.goPage(getStater(), userModel);
                        }
                        finish();
                    }

                    @Override
                    public void onStatusError(int status, String message, UserModel data) {
                        if (status == UserConstants.THIRD_CANCEL) {
                            message = "取消登录";
                        }
                        if (status == UserConstants.THIRD_ERROR) {
                            message = "登录失败";
                        }
                        super.onStatusError(status, message, data);
                        if (status == 144) {
                            UserBindFragment.goPage(getStater(), data);
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AuthManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserConstants.THIRD_WEIBO && data == null) {
            if (getUIView() != null) {
                dispose();
                getUIView().removeLoading();
            }
        }
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        TimerUtil.cancel();
        unRegist();
        dispose();
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
