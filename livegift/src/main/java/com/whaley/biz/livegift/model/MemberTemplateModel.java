package com.whaley.biz.livegift.model;

import java.util.List;

/**
 * Author: qxw
 * Date:2017/10/13
 * Introduction:
 */

public class MemberTemplateModel {

    private String code;
    private String displayName;
    private List<MemberModel> memberTemplateRelDtos;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<MemberModel> getMemberTemplateRelDtos() {
        return memberTemplateRelDtos;
    }

    public void setMemberTemplateRelDtos(List<MemberModel> memberTemplateRelDtos) {
        this.memberTemplateRelDtos = memberTemplateRelDtos;
    }
}
