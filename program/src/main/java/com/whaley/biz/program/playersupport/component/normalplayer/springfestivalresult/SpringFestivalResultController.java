package com.whaley.biz.program.playersupport.component.normalplayer.springfestivalresult;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.model.hybrid.TitleBarModel;
import com.whaley.biz.common.model.hybrid.WebviewGoPageModel;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.FestivalModel;
import com.whaley.biz.program.playersupport.event.CardResultExitEvent;
import com.whaley.biz.program.playersupport.event.CardSuccessEvent;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.whaley.biz.program.constants.ProgramConstants.EVENT_LOGIN_SUCCESS;

/**
 * Created by dell on 2018/1/24.
 */

public class SpringFestivalResultController extends BaseController<SpringFestivalResultUIAdapter> {

    @Subscribe
    public void onCardSuccessEvent(CardSuccessEvent cardSuccessEvent) {
        String name = cardSuccessEvent.getName();
        String temp;
        SpannableString content;
        if (cardSuccessEvent.getFinished() == 1) {
            temp = String.format("您已集齐“%s”%s张卡片 去查看>", cardSuccessEvent.getTotleName(), getChineseNum(cardSuccessEvent.getTotalCount()));
            content = new SpannableString(temp);
            StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
            content.setSpan(styleSpan_B, 5, 5 + cardSuccessEvent.getTotleName().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            temp = String.format("集齐“%s”%s张福卡后开启%s", cardSuccessEvent.getTotleName(), getChineseNum(cardSuccessEvent.getTotalCount()), getGiftName(cardSuccessEvent.getType()));
            content = new SpannableString(temp);
        }
        String collect;
        String left;
        String title;
        if (cardSuccessEvent.getLeftCount() > 0) {
            collect = "继续收集";
            left = String.format("今日还有%s张福卡未领取", cardSuccessEvent.getLeftCount());
            title = "查看我的福卡 >";
        } else {
            collect = "查看我的福卡";
            left = "分享我的成果";
            title = "今天的福卡已经领完，分享后再领3张";
        }
        getUIAdapter().showResult(cardSuccessEvent.getFinished() == 1, cardSuccessEvent.getLeftCount(), title, name, content, collect, left);
        if (getPlayerController().isStarted()) {
            getPlayerController().pause();
        }
    }

    private String getGiftName(int type) {
        String giftName;
        switch (type) {
            case 1:
                giftName = "现金红包";
                break;
            case 2:
                giftName = "实物奖品";
                break;
            case 3:
                giftName = "节目兑换码";
                break;
            default:
                giftName = "奖品";
                break;
        }
        return giftName;
    }

    private String getChineseNum(int num) {
        String text;
        switch (num) {
            case 0:
                text = "零";
                break;
            case 1:
                text = "一";
                break;
            case 2:
                text = "二";
                break;
            case 3:
                text = "三";
                break;
            case 4:
                text = "四";
                break;
            case 5:
                text = "五";
                break;
            case 6:
                text = "六";
                break;
            case 7:
                text = "七";
                break;
            case 8:
                text = "八";
                break;
            case 9:
                text = "九";
                break;
            case 10:
                text = "十";
                break;
            default:
                text = "多";
                break;
        }
        return text;
    }

    public void onBackClick() {
        getUIAdapter().hide();
        emitEvent(new CardResultExitEvent());
        getPlayerController().start();
    }

    public void onViewCardClick() {
        Router.getInstance().buildExecutor("/launch/service/getfestivalmodel")
                .notTransParam()
                .callback(new Executor.Callback<FestivalModel>() {
                    @Override
                    public void onCall(FestivalModel festivalModel) {
                        if (festivalModel != null) {
                            goWeb(festivalModel);
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                        //
                    }
                })
                .excute();
    }

    private void goWeb(FestivalModel festivalModel) {
        GoPageUtil.goPage((Starter) getActivity(), FormatPageModel.getWebModel(festivalModel.getUrl(), festivalModel.getDisplayName()));
    }

    @Subscribe(priority = 10)
    public void onBackPressEvent(BackPressEvent backPressEvent) {
        if (getUIAdapter() != null && getUIAdapter().isOnShow()) {
            getUIAdapter().hide();
            getEventBus().cancelEventDelivery(backPressEvent);
            emitEvent(new CardResultExitEvent());
            getPlayerController().start();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case "activityShareCall":
                if(event.getObject()!=null) {
                    int leftCount = event.getObject();
                    if (getUIAdapter() != null && getUIAdapter().isOnShow()) {
                        String collect;
                        String left;
                        String title;
                        if (leftCount > 0) {
                            collect = "继续收集";
                            left = String.format("今日还有%s张福卡未领取", leftCount);
                            title = "查看我的福卡 >";
                        } else {
                            collect = "查看我的福卡";
                            left = "分享我的成果";
                            title = "今天的福卡已经领完，分享后再领3张";
                        }
                        getUIAdapter().fixResult(leftCount, title, collect, left);
                    }
                }
                break;
        }
    }

    @Override
    public void registEvents() {
        super.registEvents();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void unRegistEvents() {
        super.unRegistEvents();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}

