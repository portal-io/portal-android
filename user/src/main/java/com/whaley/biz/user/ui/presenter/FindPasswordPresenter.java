package com.whaley.biz.user.ui.presenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.model.hybrid.TitleBarModel;
import com.whaley.biz.common.model.hybrid.WebviewGoPageModel;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.common.utils.TimerUtil;
import com.whaley.biz.user.R;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.interactor.CaptchaReset;
import com.whaley.biz.user.interactor.CaptchaValidate;
import com.whaley.biz.user.interactor.MergeFindPassword;
import com.whaley.biz.user.ui.repository.FindPasswordRepository;
import com.whaley.biz.user.ui.view.FindPasswordView;
import com.whaley.biz.user.ui.view.LoginFragment;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;

/**
 * Author: qxw
 * Date:2017/8/8
 * Introduction:
 */

public class FindPasswordPresenter extends BaseUserPresenter<FindPasswordView> {


    @Repository
    FindPasswordRepository findPasswordRepository;

    @UseCase
    MergeFindPassword mergeFindPassword;

    @UseCase
    CaptchaValidate captchaValidate;

    @UseCase
    CaptchaReset captchaReset;


    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        TimerUtil.cancel();
    }

    public FindPasswordPresenter(FindPasswordView view) {
        super(view);
    }


    public void onPhoneChanged(String phone) {
        findPasswordRepository.setPhone(phone);
        checkAllFilled();
        checkSmsButtonState();
    }

    public void onSMSChanged(String sms) {
        findPasswordRepository.setSmsCode(sms);
        checkAllFilled();
    }

    public void onValidateChanged(String validateCode) {
        findPasswordRepository.setValidateCode(validateCode);
    }

    public void onPassword1Changed(String password) {
        findPasswordRepository.setPassword1(password);
    }

    public void onPassword2Changed(String password) {
        findPasswordRepository.setPassword2(password);
    }

    public void checkSmsButtonState() {
        if (StrUtil.isMobileNo(findPasswordRepository.getPhone())
                && !findPasswordRepository.isSmsButtonLocked()) {
            getUIView().enableSmsButton();
        } else {
            getUIView().disableSmsButton();
        }
    }

    private void checkAllFilled() {
        if (!StrUtil.isEmpty(findPasswordRepository.getPhone()) && !TextUtils.isEmpty(findPasswordRepository.getSmsCode())) {
            getUIView().enableNextButton();
        } else {
            getUIView().disableNextButton();
        }
    }


    public void onNextButtonClicked() {
        if (!checkPhone(findPasswordRepository.getPhone()) && !checkSMS(findPasswordRepository.getSmsCode())) {
            toCheckCode();
        }
    }


    public void onClickConnectUs() {
        WebviewGoPageModel webviewGoPageModel = WebviewGoPageModel.createWebviewGoPageModel("http://www.whaley-vr.com/aboutus",
                TitleBarModel.createTitleBarModel(""));
        Router.getInstance().buildExecutor("/hybrid/service/goPage").putObjParam(webviewGoPageModel).excute();
    }

    public void onClickFetchCodeButton() {
        mergeFindPassword.executeMerge(new UpdateUIObserver<String>(getUIView(), true) {
            @Override
            public void onNext(@NonNull String s) {
                super.onNext(s);
                getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_sms_code_send));
                findPasswordRepository.setSmsButtonLocked(true);
                TimerUtil.timerSeconds(60, new TimerUtil.TimerNext() {
                    @Override
                    public void doNext(long number) {
                        if (number > 0) {
                            getUIView().updateValidateCodeButton(number);
                        } else {
                            findPasswordRepository.setSmsButtonLocked(false);
                            getUIView().resetSmsButton();
                            checkSmsButtonState();
                        }
                    }
                });
            }

            @Override
            public void onStatusError(int status, String message) {
                if (status == 106) {
                    message = AppContextProvider.getInstance().getContext().getString(R.string.user_phone_no_register);
                }
                super.onStatusError(status, message);
            }
        }, findPasswordRepository.getPhone());
    }

    private void toCheckCode() {
        captchaValidate.execute(new UpdateUIObserver<String>(getUIView(), true) {
            @Override
            public void onNext(@NonNull String s) {
                super.onNext(s);
                getUIView().showNext();
            }

            @Override
            public void onStatusError(int status, String message) {
                if (status == 104) {
                    getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_empty_sms_code_err));
                } else {
                    super.onStatusError(status, message);
                }

            }
        }, new UseCaseParam<>(findPasswordRepository.getSmsCode()));
    }

    public void onRightClick() {
        if (checkPassword(findPasswordRepository.getPassword1(), findPasswordRepository.getPassword2())) {
            return;
        }
        DialogUtil.showDialog(getUIView().getAttachActivity(), AppContextProvider.getInstance().getContext().getString(R.string.user_confirm_edit_pwd), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfirmChangePwd();
            }
        });
    }

    private void onConfirmChangePwd() {
        captchaReset.executeReset(new UpdateUIObserver<String>(getUIView(), true) {
                                      @Override
                                      public void onNext(@NonNull String s) {
                                          super.onNext(s);
                                          UserManager.signOut();
                                          getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_again_pwd_success));
                                          LoginFragment.goPage(getStater());
                                          finish();
                                      }

                                      @Override
                                      public void onStatusError(int status, String message) {
                                          removeLoading();
                                          if (status == 122) {
                                              getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_empty_password_old_error));
                                          } else {
                                              getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_empty_password_input_error));
                                          }
                                      }
                                  }
                , findPasswordRepository.getSmsCode(), findPasswordRepository.getPassword1());
    }




}
