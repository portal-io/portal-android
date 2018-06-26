package com.whaley.biz.user.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Author: qxw
 * Date:2017/8/8
 * Introduction:
 */

public class FindPasswordRepository extends MemoryRepository {

    private String phone;
    private String smsCode;
    private String validateCode;
    private String password1;
    private String password2;
    private boolean isSmsButtonLocked;

    public boolean isSmsButtonLocked() {
        return isSmsButtonLocked;
    }

    public void setSmsButtonLocked(boolean smsButtonLocked) {
        isSmsButtonLocked = smsButtonLocked;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
