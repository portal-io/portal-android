package com.whaley.biz.user.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Author: qxw
 * Date:2017/8/4
 * Introduction:
 */

public class EditPhoneRepository extends MemoryRepository {

    private String phone;
    private String newPhone;
    private String smsCode;
    private String imageCode;
    private boolean needValidateImage;
    private boolean isSmsButtonLocked;
    private String imageCodeUrl;


    public boolean isSmsButtonLocked() {
        return isSmsButtonLocked;
    }

    public void setSmsButtonLocked(boolean smsButtonLocked) {
        isSmsButtonLocked = smsButtonLocked;
    }

    public String getImageCodeUrl() {
        return imageCodeUrl;
    }

    public void setImageCodeUrl(String imageCodeUrl) {
        this.imageCodeUrl = imageCodeUrl;
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
        setNeedValidateImage(true);
        this.imageCode = imageCode;
    }

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getSmsCode() {
        return smsCode;
    }
}
