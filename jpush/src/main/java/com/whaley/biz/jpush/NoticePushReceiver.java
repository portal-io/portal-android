package com.whaley.biz.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.whaley.biz.jpush.model.NoticPushModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Router;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.GsonUtil;


import cn.jpush.android.api.JPushInterface;

/**
 * Author: qxw
 * Date: 2016/8/19
 */
public class NoticePushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.e("[NoticePushReceiver] onReceive - " + bundle.get(JPushInterface.EXTRA_ALERT) + " s=" + bundle.get(JPushInterface.EXTRA_EXTRA));
//        if (bundle.get(JPushInterface.EXTRA_ALERT) != null || bundle.get(JPushInterface.EXTRA_EXTRA) != null) {
//            SoundPoolManager.getInstance().playSound(SoundPoolType.TYPE_MESSAGE);
//        }
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Logger.i("[NoticePushReceiver] 用户点击打开了通知");
            try {

                if (AppUtil.isApplicationSentToBackground(context)) {
                    Intent i = new Intent();
                    i.setAction("com.snailvr.manager.main");
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.putExtra("notice", bundle.getString(JPushInterface.EXTRA_EXTRA));
                    context.startActivity(i);
                } else {
                    PushStartController.processNotice((Starter) AppContextProvider.getInstance().getContext(), bundle.getString(JPushInterface.EXTRA_EXTRA));
                }
            } catch (Exception e) {
                Log.e(e, "");
            }
        }
    }

//    // 打印所有的 intent extra 数据
//    private static String printBundle(Bundle bundle) {
//        StringBuilder sb = new StringBuilder();
//        for (String key : bundle.keySet()) {
//            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
//            } else {
//                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
//            }
//        }
//        return sb.toString();
//    }

//    private void processCustomMessage(Context context, Bundle bundle) {
////        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
//        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
////        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
////        if (AppUtil.isApplicationSentToBackground(context)) {
//        JPushLocalNotification notification = new JPushLocalNotification();
////            notification.setTitle(title);
////            notification.setExtras(extras);
//        notification.setContent(message);
////            notification.setBroadcastTime(System.currentTimeMillis() + 500);
////            int pt = -1;
////            try {
////                JSONObject object = new JSONObject(extras);
////                pt = object.optInt("pt");
////            } catch (JSONException e) {
////                // TODO Auto-generated catch block
////                e.printStackTrace();
////            }
////            if (pt != -1) {
////                if (pt == 0) {
////                    notification.setNotificationId(10000);
////                } else {
////                    notification.setNotificationId(pt);
////                }
////            }
////            if (pt == 2 || pt == 5) {
////            int pushNumber = getDefaultSharedPreferences(context).getInt("pushNumber", 0);
////            pushNumber += 1;
////            BadgeUtil.setBadgeCount(context, pushNumber);
////            getDefaultSharedPreferences(context).edit().putInt("pushNumber", pushNumber).commit();
////            }
//        JPushInterface.addLocalNotification(AppContextProvider.getInstance().getContext(),
//                notification);
////        } else {
////
////        }
//    }

//    public void startActivity() {
//        Intent intent = new Intent();
//        intent.setClass(VRApplication.getContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        VRApplication.getContext().startActivity(intent);
//    }
}
