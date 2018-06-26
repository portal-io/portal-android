package com.whaley.biz.program.uiview.viewmodel;

/**
 * Author: qxw
 * Date: 2017/3/16
 */

public interface SeverBindViewModel extends ClickableUIViewModel {
    <M> M getSeverModel();

    void convert(Object severModel);
}
