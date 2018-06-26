package com.whaleyvr.biz.download;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BGService extends Service {

    List<ServiceManager> serviceManagers = new ArrayList<>();

    BGServiceBinder binder;

    @Override
    public void onCreate() {
        super.onCreate();
        DownloadServiceManager serviceManager = new DownloadServiceManager();
        serviceManagers.add(serviceManager);
        serviceManager.init();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null)
            binder = new BGServiceBinder();
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        for (ServiceManager manager : serviceManagers) {
            manager.onStart();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        for (ServiceManager manager : serviceManagers) {
            manager.onDesotry();
        }
    }


    public class BGServiceBinder extends Binder implements ServiceHandler {

        @Override
        public <R extends ServiceManager> R getServiceManager(Class clazz) {
            if (clazz != null) {
                R manager = null;
                for (ServiceManager serviceManager : serviceManagers) {
                    if (clazz.getName().equals(serviceManager.getClass().getName())) {
                        manager = (R) serviceManager;
                        break;
                    }
                }
                return manager;
            }
            return null;
        }
    }
}
