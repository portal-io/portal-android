package com.whaley.biz.user.ui.presenter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.user.R;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.interactor.MergeThirdLogin;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.event.LoginSuccessEvent;
import com.whaley.biz.user.interactor.AccountLogin;
import com.whaley.biz.user.ui.repository.LoginRepository;
import com.whaley.biz.user.ui.view.CompleteUserInfoFragment;
import com.whaley.biz.user.ui.view.FindPasswordFragment;
import com.whaley.biz.user.ui.view.LoginView;
import com.whaley.biz.user.ui.view.PhoneLoginFragmengt;
import com.whaley.biz.user.ui.view.RegisterFragment;
import com.whaley.biz.user.ui.view.UserBindFragment;
import com.whaley.biz.user.utils.AuthManager;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date: 2017/7/17
 */

public class LoginPresenter extends BasePagePresenter<LoginView> {

    public static final String STR_CALLBACK_ID = "str_callbackId";
    public static final String STR_PATH = "str_path";

    @Repository
    LoginRepository loginRepository;
    @UseCase
    AccountLogin accountLogin;

    @UseCase
    MergeThirdLogin mergeThirdLogin;

    Disposable disposable;

    Intent resultIntent = new Intent();

    public LoginPresenter(LoginView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            loginRepository.setCallbackId(arguments.getString(STR_CALLBACK_ID));
            loginRepository.setRouterPath(arguments.getString(STR_PATH));
            resultIntent.putExtra(STR_CALLBACK_ID, loginRepository.getCallbackId());
        }
    }

    @Override
    public void onAttached() {
        super.onAttached();
        getUIView().getAttachActivity().setResult(Activity.RESULT_OK, resultIntent);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        regist();
    }

    public void onLoginOnClick() {
//        if (PermissionUtil.requestPermission(getUIView().getAttachActivity(), Manifest.permission.READ_PHONE_STATE, 1)) {
//            return;
//        }
        AccountLogin.AccountLoginParam param = new AccountLogin.AccountLoginParam();
        param.setUsername(loginRepository.getUserName());
        param.setPassword(loginRepository.getPassword());
        accountLogin.executeLogin(new UpdateUIObserver<UserModel>(getUIView(), true) {

            @Override
            public void onNext(@NonNull UserModel userModel) {
                if (!userModel.isAddInformation()) {
                    getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_login_success));
                    EventController.postEvent(new LoginSuccessEvent(UserConstants.EVENT_LOGIN_SUCCESS, loginRepository.getRouterPath()));
                    getUIView().getAttachActivity().setResult(Activity.RESULT_OK, new Intent());
                } else {
                    CompleteUserInfoFragment.goPage(getStater(), userModel);
                }
                finish();
            }

            @Override
            public void onStatusError(int status, String message) {
                super.onStatusError(status, message);
            }
        }, param);
    }

    public void onUserNameChanged(String userName) {
        loginRepository.setUserName(userName);
        checkAllFilled();
    }

    public void onPasswordChanged(String password) {
        loginRepository.setPassword(password);
        checkAllFilled();
    }


    public void checkAllFilled() {
        if (!StrUtil.isEmpty(loginRepository.getUserName()) && !StrUtil.isEmpty(
                loginRepository.getPassword())) {
            getUIView().enableLoginButton();
        } else {
            getUIView().disableLoginButton();
        }
    }

    public void onRegisterClick() {
        RegisterFragment.goPage(getStater());

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
                            EventController.postEvent(new LoginSuccessEvent(UserConstants.EVENT_LOGIN_SUCCESS, loginRepository.getRouterPath()));
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


    public void onSafeLonginClick() {
        PhoneLoginFragmengt.goPage(getStater());
    }

    public void onForgetPwdClick() {
        FindPasswordFragment.goPage(getStater());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginSuccessEvent event) {
        finish();
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        dispose();
        unRegist();
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

}
