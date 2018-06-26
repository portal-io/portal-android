package com.whaley.biz.user.ui.repository;

import android.graphics.Bitmap;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.user.ui.viewmodel.UserInfoViewModel;

import java.io.File;
import java.util.List;

/**
 * Author: qxw
 * Date:2017/8/3
 * Introduction:
 */

public class UserInfoRepository extends MemoryRepository {
    private String phone;
    private String nickname;
    private String smsCode;
    private String validateCode;
    private String password;
    private String imageCodeUrl;
    private String imageCode;
    private String avatarUrl;
    private boolean isBindQQ;
    private boolean isBindWB;
    private boolean isBindWX;
    private boolean isBind;

    private Bitmap photo;
    private File mPhotoFile;

    public File getmPhotoFile() {
        return mPhotoFile;
    }

    public void setmPhotoFile(File mPhotoFile) {
        this.mPhotoFile = mPhotoFile;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageCodeUrl() {
        return imageCodeUrl;
    }

    public void setImageCodeUrl(String imageCodeUrl) {
        this.imageCodeUrl = imageCodeUrl;
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isBindQQ() {
        return isBindQQ;
    }

    public void setBindQQ(boolean bindQQ) {
        isBindQQ = bindQQ;
    }

    public boolean isBindWB() {
        return isBindWB;
    }

    public void setBindWB(boolean bindWB) {
        isBindWB = bindWB;
    }

    public boolean isBindWX() {
        return isBindWX;
    }

    public void setBindWX(boolean bindWX) {
        isBindWX = bindWX;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
