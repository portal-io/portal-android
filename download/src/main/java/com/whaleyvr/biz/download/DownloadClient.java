package com.whaleyvr.biz.download;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;

import java.util.Map;

public class DownloadClient implements Executor {

    private DownloadClientListner listner;

    private Callback callback;

    private boolean isConnected;

    public DownloadClient(){

    }

    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("DownloadClient","onServiceConnected");
            isConnected=true;
            ServiceHandler handle=(ServiceHandler) service;
            DownloadServiceManager manager=handle.getServiceManager(DownloadServiceManager.class);
            if(listner!=null){
                listner.onGetDownloadManager(manager);
            }
            if(callback!=null){
                callback.onCall(manager);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected=false;
        }
    };

    public void regist(Activity activity,DownloadClientListner listner){
        this.listner=listner;
        Intent intent=new Intent(activity,BGService.class);
        activity.bindService(intent,connection , Context.BIND_AUTO_CREATE);
    }

    public void regist(Activity activity,Callback callback){
        Log.d("DownloadClient","regist");
        this.callback=callback;
        Intent intent=new Intent(activity,BGService.class);
        activity.bindService(intent,connection , Context.BIND_AUTO_CREATE);
    }

    public void unRegist(Activity activity){
        Log.d("DownloadClient","unRegist");
        this.listner=null;
        if(isConnected) {
            activity.unbindService(connection);
            isConnected=false;
        }
    }

    @Override
    public void excute(Object o, Callback callback) {
        Map<String, Object> param = (Map<String, Object>)o;
        boolean isRegist = (Boolean) param.get("isRegist");
        Activity activity = (Activity)param.get("activity");
        if(isRegist){
            regist(activity, callback);
        }else{
            unRegist(activity);
        }
    }

    @Override
    public void init(Context context) {

    }


    public interface DownloadClientListner{
        void onGetDownloadManager(DownloadServiceManager downloadServiceManager);
    }

}
