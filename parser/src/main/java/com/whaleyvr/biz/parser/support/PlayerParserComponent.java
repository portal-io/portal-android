package com.whaleyvr.biz.parser.support;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseModuleComponent;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/4 19:27.
 */

public class PlayerParserComponent extends BaseModuleComponent<Integer>{

    @Override
    protected BaseController onCreateController() {
        return new PlayerParserController(getData());
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
