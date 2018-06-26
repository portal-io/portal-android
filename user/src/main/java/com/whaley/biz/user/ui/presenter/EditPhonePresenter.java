package com.whaley.biz.user.ui.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.utils.TimerUtil;
import com.whaley.biz.user.R;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.interactor.MergeUpdatePhone;
import com.whaley.biz.user.interactor.UpdatePhone;
import com.whaley.biz.user.model.CaptchaModel;
import com.whaley.biz.user.ui.repository.EditPhoneRepository;
import com.whaley.biz.user.ui.view.EditPhoneView;
import com.whaley.biz.user.ui.view.LoginFragment;
import com.whaley.biz.user.utils.StringUtils;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;


/**
 * Author: qxw
 * Date:2017/8/4
 * Introduction:
 */

public class EditPhonePresenter extends BasePagePresenter<EditPhoneView> {


    @Repository
    EditPhoneRepository editPhoneRepository;


    @UseCase
    MergeUpdatePhone mergeUpdatePhone;

    @UseCase
    UpdatePhone updatePhone;

    public EditPhonePresenter(EditPhoneView view) {
        super(view);
    }

    public EditPhoneRepository getEditPhoneRepository() {
        return editPhoneRepository;
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        regist();
        if (UserManager.getInstance().isLogin()) {
            editPhoneRepository.setPhone(StringUtils.getPhone(UserManager.getInstance().getUser().getMobile()));
            getUIView().showPhone(editPhoneRepository.getPhone());
        } else {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (UserConstants.EVENT_SIGN_OUT.equals(event.getEventType())) {
            finish();
        }
    }
    private void checkPhoneCode() {
        mergeUpdatePhone.executeSendSMS(
                new UpdateUIObserver<CaptchaModel>(getUIView(), true) {
                    @Override
                    public void onNext(@NonNull CaptchaModel captchaModel) {
                        super.onNext(captchaModel);
                        getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_sms_code_send));
                        editPhoneRepository.setSmsButtonLocked(true);
                        TimerUtil.timerSeconds(60, new TimerUtil.TimerNext() {
                            @Override
                            public void doNext(long number) {
                                if (number > 0) {
                                    getUIView().updateValidateCodeButton(number);
                                } else {
                                    editPhoneRepository.setSmsButtonLocked(false);
                                    getUIView().resetSmsButton();
                                    checkSmsButtonState();
                                }
                            }
                        });
                    }

                    @Override
                    public void onStatusError(int status, String message, CaptchaModel data) {
                        if (status == 102) {
                            getUIView().showToast(message);
                        } else if (status == 101) {
                            getUIView().showToast("验证码错误, 请重新输入,或点击刷新验证码");
                        } else if (status == 141) {
                            getUIView().showToast("请输入验证码");
                        } else {
                            getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_sms_code_err));
                        }
                        getUIView().removeLoading();
                        if (data != null && !StrUtil.isEmpty(data.getUrl())) {
                            editPhoneRepository.setImageCodeUrl(data.getUrl());
                            editPhoneRepository.setNeedValidateImage(true);
                            getUIView().showValidateInput();
                        }
                    }
                }
        );

    }

    public void checkSmsButtonState() {
        if (StrUtil.isMobileNo(editPhoneRepository.getNewPhone())
                && (!editPhoneRepository.isNeedValidateImage() || (editPhoneRepository.isNeedValidateImage() && StrUtil.isEmpty(editPhoneRepository.getImageCode())))
                && !editPhoneRepository.isSmsButtonLocked()) {
            getUIView().setSmsButtonEnable(true);
        } else {
            getUIView().setSmsButtonEnable(false);
        }
    }

    public void onClickSaveButton() {
//        if (PermissionUtil.requestPermission(getUIView().getAttachActivity(), Manifest.permission.READ_PHONE_STATE, 1)) {
//            return;
//        }
        if (checkInputError()) {
            return;
        }
        onDialogConfirm();
    }

    public boolean checkInputError() {
        boolean hasError = false;
        if (StrUtil.isEmpty(editPhoneRepository.getNewPhone())) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_empty_phone));
            hasError = true;
        } else if (!StrUtil.isMobileNo(editPhoneRepository.getNewPhone())) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_error_phone));
            hasError = true;
        } else if (TextUtils.isEmpty(editPhoneRepository.getSmsCode())) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_empty_smscode));
            hasError = true;
        }
        return hasError;
    }

    public void onDialogConfirm() {
        if (getUIView() != null) {
            getUIView().showReLoginDialog();
        }
    }


    public void updatePhone() {
        updatePhone.execute(new UpdateUIObserver<String>(getUIView(), true) {
                                @Override
                                public void onNext(@NonNull String s) {
                                    super.onNext(s);
                                    UserManager.signOut();
                                    LoginFragment.goPage(getStater());
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
                            }
        );
    }

    public void onNewPhoneChanged(String phone) {
        editPhoneRepository.setNewPhone(phone);
        checkSmsButtonState();
    }

    public void onImageCodeChanged(String imageCode) {
        editPhoneRepository.setImageCode(imageCode);
        checkSmsButtonState();
    }

    public void onSmsCodeChanged(String code) {
        editPhoneRepository.setSmsCode(code);
    }

    public void onClickSmsButton() {
        checkPhoneCode();
    }


    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        TimerUtil.cancel();
        unRegist();
    }

}
