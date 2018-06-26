package com.whaley.biz.user.ui.presenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.user.R;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.interactor.UpdatePassword;
import com.whaley.biz.user.ui.repository.EditPasswordRepository;
import com.whaley.biz.user.ui.view.EditPasswordView;
import com.whaley.biz.user.ui.view.FindPasswordFragment;
import com.whaley.biz.user.ui.view.LoginFragment;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.PermissionUtil;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;

/**
 * Author: qxw
 * Date:2017/8/5
 * Introduction:
 */

public class EditPasswordPresenter extends BaseUserPresenter<EditPasswordView> {


    @Repository
    EditPasswordRepository editPasswordRepository;

    @UseCase
    UpdatePassword updatePassword;


    public void onClickSaveButton() {
//        if (PermissionUtil.requestPermission(getUIView().getAttachActivity(), Manifest.permission.READ_PHONE_STATE, 1)) {
//            return;
//        }
        if (checkInputError()) {
            return;
        }
        showConfirmDialog();
    }
    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        regist();
    }
    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        unRegist();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (UserConstants.EVENT_SIGN_OUT.equals(event.getEventType())) {
            finish();
        }
    }
    public boolean checkInputError() {
        boolean hasError = false;
        if (TextUtils.isEmpty(editPasswordRepository.getPwdOri())) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_input_ori_password));
            hasError = true;
        } else if (editPasswordRepository.getPwdOri().equals(editPasswordRepository.getPwd1())) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_ori_new_password_same));
            hasError = true;
        } else if (TextUtils.isEmpty(editPasswordRepository.getPwd1())) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_input_new_password));
            hasError = true;
        } else if (TextUtils.isEmpty(editPasswordRepository.getPwd2())) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_input_new_password_again));
            hasError = true;
        } else if (!editPasswordRepository.getPwd1().equals(editPasswordRepository.getPwd2())) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_new_password_differ));
            hasError = true;
        } else {
            hasError = checkPassword(editPasswordRepository.getPwd2());
        }
        return hasError;
    }


    public EditPasswordPresenter(EditPasswordView view) {
        super(view);
    }


    public void showConfirmDialog() {
        DialogUtil.showDialog(getUIView().getAttachActivity(), AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_title_edit_pwd_success), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfirmChangePwd();
            }
        });
    }

    private void onConfirmChangePwd() {
        updatePassword.execute(new UpdateUIObserver<String>(getUIView(), true) {
            @Override
            public void onNext(@NonNull String s) {
                super.onNext(s);
                UserManager.signOut();
                getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.text_operator_success));
                LoginFragment.goPage(getStater());
                finish();
            }

            @Override
            public void onStatusError(int status, String message) {
                if (status == 148) {
                    message = AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_ori_password_err);
                }
                super.onStatusError(status, message);
            }
        }, editPasswordRepository.getPwdOri(), editPasswordRepository.getPwd1());
    }


    public void onPasswordOriTextChanged(String text) {
        editPasswordRepository.setPwdOri(text);
    }


    public void onPassword1Changed(String text) {
        editPasswordRepository.setPwd1(text);
    }


    public void onPassword2Changed(String text) {
        editPasswordRepository.setPwd2(text);
    }

    public void onForgetPwdClick() {
        FindPasswordFragment.goPage(getStater());
    }
}
