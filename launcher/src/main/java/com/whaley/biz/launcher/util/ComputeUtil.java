package com.whaley.biz.launcher.util;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.launcher.model.RecommendModel;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Router;
import com.whaley.core.utils.DateUtils;
import com.whaley.core.utils.GsonUtil;

/**
 * Author: qxw
 * Date:2018/1/18
 * Introduction:
 */

public class ComputeUtil {

    public static boolean isShowSplash(String keySting) {
        int type = SharedPreferencesUtil.getSplashType(keySting);
        long time = SharedPreferencesUtil.getSplashTime(keySting);
        Log.e("ComputeUtil", "keySting=" + keySting);
        switch (type) {
            case 0:
                if (time == 0) {
                    return true;
                }
                break;
            case 1:
                if (time == 0 || !DateUtils.isToday(time)) {
                    Log.e("ComputeUtil", "time=" + time);
                    return true;
                }
                break;
            case 2:
                return true;
        }
        return false;
    }

    public static void getOnClickBuilder(String keyString, String eventID) {
        RecommendModel recommendModel = GsonUtil.getGson().fromJson(SharedPreferencesUtil.getSplashParam(keyString), RecommendModel.class);
        String eventString = recommendModel.getLinkArrangeValue();
        if ("linkarrangetype_information".equals(recommendModel.getLinkArrangeType())) {
            eventString = recommendModel.getInfUrl();
        }
        if (recommendModel != null) {
            LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                    .setEventId(eventID)
                    .setCurrentPageId(BIConstants.ROOT_HOME_AD)
                    .putCurrentPagePropKeyValue(BIConstants.CURRENT_PROP_VIEW_PAGE_TYPE, getShowType(keyString))
                    .putCurrentPagePropKeyValue(BIConstants.CURRENT_PROP_VIEW_SHOW_TYPE, String.valueOf(SharedPreferencesUtil.getSplashType(keyString)))
                    .putEventPropKeyValue(BIConstants.EVENT_PROP_KEY_EVENT_ID, eventString);
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

    public static String getShowType(String keyString) {
        switch (keyString) {
            case CommonConstants.KEY_BOOT:
                return "13";
            case CommonConstants.KEY_POSTER:
                return "14";
            default:
                return "15";
        }
    }
}
