package com.whaley.biz.user.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.user.model.UserModel;

/**
 * Author: qxw
 * Date:2017/8/2
 * Introduction:
 */

public class RegisterRepository extends MemoryRepository {

    private UserModel userModel;
    private String phone;
    private boolean needValidateImage = false;
    private String imageCode;
    private String imageCodeUrl;
    private boolean smsButtonLocked = false;
    private String smsCode;
    private boolean isBind;

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getImageCodeUrl() {
        return imageCodeUrl;
    }

    public void setImageCodeUrl(String imageCodeUrl) {
        this.imageCodeUrl = imageCodeUrl;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isNeedValidateImage() {
        return needValidateImage;
    }

    public void setNeedValidateImage(boolean needValidateImage) {
        this.needValidateImage = needValidateImage;
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public boolean isSmsButtonLocked() {
        return smsButtonLocked;
    }

    public void setSmsButtonLocked(boolean smsButtonLocked) {
        this.smsButtonLocked = smsButtonLocked;
    }

}
