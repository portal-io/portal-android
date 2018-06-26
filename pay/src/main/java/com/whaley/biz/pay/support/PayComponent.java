package com.whaley.biz.pay.support;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseModuleComponent;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.core.debug.logger.Log;

/**
 * Created by YangZhi on 2017/8/17 20:17.
 */

public class PayComponent extends BaseModuleComponent<Integer>{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new PayUIAdapter();
    }


    @Override
    protected BaseController onCreateController() {
        return new PayController(getData());
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
