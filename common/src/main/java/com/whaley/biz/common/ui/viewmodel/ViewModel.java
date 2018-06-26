package com.whaley.biz.common.ui.viewmodel;

/**
 * Created by YangZhi on 2017/8/14 19:04.
 */

public abstract class ViewModel implements IViewModel{

    @Override
    public int getType() {
        return getClass().getSimpleName().hashCode();
    }
}
