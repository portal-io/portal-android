package com.whaley.biz.user.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Author: qxw
 * Date:2017/8/5
 * Introduction:
 */

public class EditPasswordRepository extends MemoryRepository{

    private String pwdOri;
    private String pwd1;
    private String pwd2;


    public String getPwdOri() {
        return pwdOri;
    }

    public void setPwdOri(String pwdOri) {
        this.pwdOri = pwdOri;
    }

    public String getPwd1() {
        return pwd1;
    }

    public void setPwd1(String pwd1) {
        this.pwd1 = pwd1;
    }

    public String getPwd2() {
        return pwd2;
    }

    public void setPwd2(String pwd2) {
        this.pwd2 = pwd2;
    }
}
