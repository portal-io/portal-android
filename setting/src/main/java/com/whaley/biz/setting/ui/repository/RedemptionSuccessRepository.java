package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.setting.constant.ProgramConstants;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.model.RedemptionCodeModel;
import com.whaley.core.utils.DateUtils;

/**
 * Created by dell on 2017/9/4.
 */

public class RedemptionSuccessRepository extends MemoryRepository {

    private RedemptionCodeModel data;
    private String name;
    private String code;
    private String price;
    private String content;
    private boolean isLook;

    public RedemptionCodeModel getData() {
        return data;
    }

    public void setData(RedemptionCodeModel data) {
        this.data = data;
        if (data == null) {
            return;
        }
        setName(data.getDisplayName());
        setCode(data.getCouponSourceCode());
        setPrice(new StringBuilder().append("¥")
                .append(SettingUtil.fromFenToYuan(data.getPrice())).toString());
        setContent(new StringBuilder().append("购买于")
                .append(DateUtils.foramteToDate(data.getCreateTime()
                        , DateUtils.YYYYMMDD_NYR)).toString());
        if (ProgramConstants.TYPE_LIVE.equals(data.getRelatedType()) && data.getLiveStatus() == 0) {
            setLook(false);
        } else {
            setLook(true);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isLook() {
        return isLook;
    }

    public void setLook(boolean look) {
        isLook = look;
    }

}
