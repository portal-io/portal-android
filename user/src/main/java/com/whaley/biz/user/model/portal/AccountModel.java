package com.whaley.biz.user.model.portal;

/**
 * Created by dell on 2018/8/8.
 */

public class AccountModel {

    public AccountModel(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    private String accountId;

}
