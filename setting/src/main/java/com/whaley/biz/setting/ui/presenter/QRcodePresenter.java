package com.whaley.biz.setting.ui.presenter;

import android.os.Bundle;

import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.setting.ui.repository.QRcodeRepository;
import com.whaley.biz.setting.ui.view.QRcodeView;
import com.whaley.core.inject.annotation.Repository;

/**
 * Created by dell on 2017/8/7.
 */

public class QRcodePresenter extends BasePagePresenter<QRcodeView> {

    public static final String STR_CALLBACK_ID = "str_callbackId";

    @Repository
    QRcodeRepository repository;

    public QRcodePresenter(QRcodeView view) {
        super(view);
    }

    public QRcodeRepository getQRcodeRepository(){
        return repository;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            getQRcodeRepository().setCallbackId(arguments.getString(STR_CALLBACK_ID));
        }
    }

}
