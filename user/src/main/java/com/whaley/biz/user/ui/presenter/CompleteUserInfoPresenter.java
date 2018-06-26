package com.whaley.biz.user.ui.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.user.R;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.event.LoginSuccessEvent;
import com.whaley.biz.user.interactor.CompleteUserinfo;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.ui.repository.CompleteUserInfoRepository;
import com.whaley.biz.user.ui.view.CompleteUserInfoView;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;

import io.reactivex.annotations.NonNull;

/**
 * Author: qxw
 * Date:2017/8/7
 * Introduction:
 */

public class CompleteUserInfoPresenter extends BaseUserPresenter<CompleteUserInfoView> {

    public final static String KEY_LOGIN_USERBEAN = "login_userbean";
    public final static String KEY_LOGIN_USERBEAN_JSON = "login_userbean_json";
    @Repository
    CompleteUserInfoRepository completeUserInfoRepository;

    @UseCase
    CompleteUserinfo completeUserinfo;


    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        if (arguments != null) {
            UserModel userModel = arguments.getParcelable(KEY_LOGIN_USERBEAN);
            if (userModel == null) {
                userModel = GsonUtil.getGson().fromJson(arguments.getString(KEY_LOGIN_USERBEAN_JSON), UserModel.class);
            }
            completeUserInfoRepository.setUserModel(userModel);
        }
    }

    public CompleteUserInfoPresenter(CompleteUserInfoView view) {
        super(view);
    }

    public void onClickRegistButton() {
        if (checkNickName(completeUserInfoRepository.getNickname()) || checkPassword(completeUserInfoRepository.getPassword())) {
            return;
        }
        toUpdateUserInfo();
    }

    private void toUpdateUserInfo() {
        completeUserinfo.executeComplete(new UpdateUIObserver<String>(getUIView(), true) {
            @Override
            public void onNext(@NonNull String s) {
                super.onNext(s);
                getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_register_success));
                EventController.postEvent(new LoginSuccessEvent(UserConstants.EVENT_LOGIN_SUCCESS));
                finish();
            }
        });
    }


    public void onNickNameChanged(String name) {
        completeUserInfoRepository.setNickname(name);
        checkRegisterButtonState();
    }

    public void onPasswordChanged(String password) {
        completeUserInfoRepository.setPassword(password);
        checkRegisterButtonState();
    }

    private void checkRegisterButtonState() {
        if (!StrUtil.isEmpty(completeUserInfoRepository.getNickname()) && !TextUtils.isEmpty(completeUserInfoRepository.getPassword())) {
            getUIView().enableRegisterButton();
        } else {
            getUIView().diableRegisterButton();
        }
    }

}
