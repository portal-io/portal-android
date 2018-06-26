package com.whaley.biz.launcher.presenter;

import android.app.Activity;
import android.os.Bundle;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.launcher.activitys.MainActivity;
import com.whaley.biz.launcher.event.AdvertiseImageEvent;
import com.whaley.biz.launcher.repository.AdvertiseRepository;
import com.whaley.biz.launcher.util.ComputeUtil;
import com.whaley.biz.launcher.util.SharedPreferencesUtil;
import com.whaley.biz.launcher.view.AdvertiseView;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.router.Router;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by dell on 2017/9/5.
 */

public class AdvertisePresenter extends BasePagePresenter<AdvertiseView> {

    public static final String ADVERTISE_PATH = "path";

    @Repository
    AdvertiseRepository repository;

    public AdvertisePresenter(AdvertiseView view) {
        super(view);
    }

    public AdvertiseRepository getRepository() {
        return repository;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            getRepository().setPath(arguments.getString(ADVERTISE_PATH));
        }
        regist();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegist();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(AdvertiseImageEvent event) {
        getRepository().setBitmap(event.getBitmap());
    }

    public void startActivity() {
        if (getStater() != null) {
            getStater().finish();
            ((Activity) getStater()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        MainActivity.launch(getStater());
    }

    /**
     * @author qxw
     * @time 2016/9/27 16:06
     * 启动页广告跳转
     */
    public void onStartup() {
        ComputeUtil.getOnClickBuilder(CommonConstants.KEY_BOOT, BIConstants.AD_CLICK);
        startActivity();
        Router.getInstance().buildExecutor("/program/service/gorecommendpage").notTransParam().putObjParam(SharedPreferencesUtil.getSplashParam(CommonConstants.KEY_BOOT)).excute();
    }

    public void saveBI() {
        ComputeUtil.getOnClickBuilder(CommonConstants.KEY_BOOT, BIConstants.AD_CLOSE);
    }
}
