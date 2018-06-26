package com.whaley.biz.user.ui.presenter;

import android.text.TextUtils;

import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.biz.user.R;
import com.whaley.biz.user.UserConstants;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.utils.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: qxw
 * Date:2017/8/8
 * Introduction:
 */

public class BaseUserPresenter<VIEW extends BasePageView> extends BasePagePresenter<VIEW> {


    public BaseUserPresenter(VIEW view) {
        super(view);
    }


    /**
     * 验证手机号
     *
     * @return
     */
    public boolean checkPhone(String phone) {
        boolean hasError = false;
        if (TextUtils.isEmpty(phone)) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_empty_phone));
            hasError = true;
        } else if (!StrUtil.isMobileNo(phone)) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_error_phone));
            hasError = true;
        }
        return hasError;
    }

    /**
     * 图片验证码验证
     *
     * @return
     */
    public boolean checkValidateCode(String ImageCode) {
        boolean hasError = false;
        if (TextUtils.isEmpty(ImageCode)) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_empty_validate_code));
            hasError = true;
        }
        return hasError;
    }

    /**
     * 短信验证码
     *
     * @param smsCode
     * @return
     */
    protected boolean checkSMS(String smsCode) {

        boolean hasError = false;
        if (TextUtils.isEmpty(smsCode)) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_empty_smscode));
            hasError = true;
        } else {
            hasError = false;
        }
        return hasError;

    }

    protected boolean checkPassword(String pwd1, String pwd2) {
        boolean hasError;
        if (TextUtils.isEmpty(pwd1)) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_empty_password));
            hasError = true;
        } else if (TextUtils.isEmpty(pwd2)) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_input_new_password_again));
            hasError = true;
        } else if (!pwd1.equals(pwd2)) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_new_password_differ));
            hasError = true;
        } else {
            hasError = checkPassword(pwd1);
        }
        return hasError;
    }

    protected boolean checkPassword(String pwd) {
        if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 20) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_edit_pwd));
            return true;
        } else {
            Pattern p = Pattern.compile("[A-Za-z0-9_]+");
            Matcher m = p.matcher(pwd);
            if (m.matches()) {
                int countMatch = 0;
                Pattern p1 = Pattern.compile("[A-Za-z]+");
                Pattern p2 = Pattern.compile("[0-9]+");
                Pattern p3 = Pattern.compile("[_]+");
                Matcher m1 = p1.matcher(pwd);
                Matcher m2 = p2.matcher(pwd);
                Matcher m3 = p3.matcher(pwd);
                if (m1.find()) {
                    countMatch++;
                }
                if (m2.find()) {
                    countMatch++;
                }
                if (m3.find()) {
                    countMatch++;
                }
                if (countMatch > 1) {
                    return false;
                } else {
                    getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_edit_pwd));
                    return true;
                }

            } else {
                getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_edit_pwd));
                return true;
            }
        }
    }


    /**
     * 昵称验证
     *
     * @param nickname
     * @return
     */
    public boolean checkNickName(String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_edit_nickname));
            return true;
        } else {
            int length = StrUtil.lengthWithChineseSize3(nickname);
            if (length < 3 || length > 30) {
                getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_edit_nickname));
                return true;
            } else {
                Pattern pattern = Pattern.compile(UserConstants.REG_ILLEGAL);
                Matcher matcher = pattern.matcher(nickname);
                if (matcher.find()) {
                    getUIView().showToast(AppContextProvider.getInstance().getContext().getResources().getString(R.string.user_edit_nickname_illegal));
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

}
