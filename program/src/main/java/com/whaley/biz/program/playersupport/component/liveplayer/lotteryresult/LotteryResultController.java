package com.whaley.biz.program.playersupport.component.liveplayer.lotteryresult;

import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.program.constants.SettingRouterPath;
import com.whaley.biz.program.playersupport.event.LotteryFailEvent;
import com.whaley.biz.program.playersupport.event.LotterySuccessEvent;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.router.Router;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yangzhi on 2017/8/14.
 */

public class LotteryResultController extends BaseController<LotteryResultUIAdapter> {


    @Subscribe
    public void onLotterySuccessEvent(LotterySuccessEvent lotterySuccessEvent) {
        getUIAdapter().showLotterySuccess(lotterySuccessEvent.getTime(), lotterySuccessEvent.getName(), lotterySuccessEvent.getPicUrl());
    }

    @Subscribe
    public void onLotteryFailEvent(LotteryFailEvent lotteryFailEvent) {
        getUIAdapter().showLotteryFail(lotteryFailEvent.getTime());
    }

    public void onLotterySuccesButtonClick(){
        Router.getInstance().buildNavigation(SettingRouterPath.GIFT)
                //設置 starter
                .setStarter((Starter) getActivity())
                //設置 requestCode
                .setRequestCode(0)
                .withString("key_login_tips","请先登录后查看奖品")
                .withInt(RouterConstants.KEY_ACTIVITY_TYPE, RouterConstants.TITLE_BAR_ACTIVITY)
                .navigation();
        getUIAdapter().hide();
    }

    public void onLotteryFailButtonClick(){
        getUIAdapter().hide();
    }

    @Subscribe(priority = 10)
    public void onBackPressEvent(BackPressEvent backPressEvent) {
        if (getUIAdapter() != null && getUIAdapter().isOnShow()) {
            getUIAdapter().hide();
            getEventBus().cancelEventDelivery(backPressEvent);
        }
    }
}
