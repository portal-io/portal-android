package com.whaley.biz.user.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Author: qxw
 * Date: 2017/7/17
 */

public class EditNickNameRepository extends MemoryRepository {
    private String userName;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
